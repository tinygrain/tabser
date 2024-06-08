package app.tabser.rendering.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import app.tabser.rendering.RenderModel;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;

public abstract class AbstractBlock implements RenderBlock {
    /**
     * bounds relative to page / static
     */
    private final Rectangle bounds = new Rectangle();
    /**
     * Viewport & Delta relative / mutable
     */
    private final Rectangle viewPort;
    private boolean invalid = true;
    private final Map<Rectangle, Consumer<RenderBlock>> touchTargets = new HashMap<>();
    private final Map<Rectangle, Consumer<RenderBlock>> longTouchTargets = new HashMap<>();

    public AbstractBlock(Rectangle viewPort) {
        this.viewPort = viewPort;
    }

    public void registerTouchTarget(Rectangle area, Consumer<RenderBlock> action) {
        touchTargets.put(area, action);
    }

    public void registerLongTouchTarget(Rectangle area, Consumer<RenderBlock> action) {
        longTouchTargets.put(area, action);
    }

    public void clearAllTouchTargets() {
        longTouchTargets.clear();
        touchTargets.clear();
    }

    protected void setBounds(Rectangle bounds) {
        this.bounds.set(bounds);
    }

    @Override
    public Rectangle getRelativeBounds() {
        return viewPort.translate(getBounds());
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void touch(float x, float y, boolean longClick) {
        Map<Rectangle, Consumer<RenderBlock>> targets;
        if (longClick) {
            targets = longTouchTargets;
        } else {
            targets = touchTargets;
        }
        for (Rectangle target : targets.keySet()) {
            if (target.contains(x, y)) {
                targets.get(target).accept(this);
            }
        }
    }

    @Override
    public void invalidate() {
        this.invalid = true;
    }

    private void validate() {
        this.invalid = false;
    }

    @Override
    public boolean isValid() {
        return !invalid;
    }

    @Override
    public void render(RenderModel.RenderIterator iterator) {
        if (invalid) {
            setBounds(calculate(iterator));
            validate();
            if (iterator.options.cache) {
                cache(iterator);
            }
        }
        if (isInView()) {
//            Log.d("RENDER", "IN VIEW: " + this.toString());
            draw(iterator);
        } else {
//            Log.d("RENDER", "OUTSIDE VIEW: " + this.toString());
        }
    }

    private boolean isInView() {
        return viewPort.intersect(getBounds());
    }

    protected Rectangle boundsOnPage(RenderModel.RenderIterator renderIterator, int height) {
        float blockWidth = renderIterator.getModel().getBlockWidth();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        return new Rectangle((int) renderIterator.xPosition, (int) renderIterator.yPosition,
                (int) (renderIterator.xPosition + blockWidth), (int) (renderIterator.yPosition + height));
    }

    protected abstract void cache(RenderModel.RenderIterator iterator);

    protected abstract void draw(RenderModel.RenderIterator iterator);

    protected abstract Rectangle calculate(RenderModel.RenderIterator iterator);
}
