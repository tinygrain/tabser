package app.tabser.view.model.blocks;

import android.graphics.Rect;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.geometry.ViewPort;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public abstract class AbstractBlock implements RenderBlock {
    /**
     * bounds relative to page / static
     */
    private final Rect bounds = new Rect();
    /**
     * Viewport & Delta relative / mutable
     */
    private final ViewPort viewPort;
    private boolean invalid = true;
    private final Map<Rect, Consumer<RenderBlock>> touchTargets = new HashMap<>();
    private final Map<Rect, Consumer<RenderBlock>> longTouchTargets = new HashMap<>();

    public AbstractBlock(ViewPort viewPort) {
        this.viewPort = viewPort;
    }

    public void registerTouchTarget(Rect area, Consumer<RenderBlock> action) {
        touchTargets.put(area, action);
    }

    public void registerLongTouchTarget(Rect area, Consumer<RenderBlock> action) {
        longTouchTargets.put(area, action);
    }

    public void clearAllTouchTargets() {
        longTouchTargets.clear();
        touchTargets.clear();
    }

    protected void setBounds(Rect bounds) {
        this.bounds.set(bounds);
    }

    @Override
    public Rect getRelativeBounds() {
        return viewPort.translate(getBounds());
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public void touch(float x, float y, boolean longClick) {
        Map<Rect, Consumer<RenderBlock>> targets;
        if (longClick) {
            targets = longTouchTargets;
        } else {
            targets = touchTargets;
        }
        for (Rect target : targets.keySet()) {
            if (target.contains((int) x, (int) y)) {
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
    public void render(RenderIterator iterator) {
        if (invalid) {
            setBounds(calculate(iterator));
            validate();
            if (iterator.options.cache) {
                cache(iterator);
            }
        }
        if (isInView()) {
            Log.d("RENDER", "IN VIEW: " + this.toString());
            draw(iterator);
        } else {
            Log.d("RENDER", "OUTSIDE VIEW: " + this.toString());
        }
        iterator.increment(this);
    }

    private boolean isInView() {
        return viewPort.isInView(getBounds());
    }

    protected Rect boundsOnPage(RenderIterator renderIterator, int height) {
        float blockWidth = renderIterator.getModel().getBlockWidth();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        return new Rect((int) renderIterator.xPosition, (int) renderIterator.yPosition,
                (int) (renderIterator.xPosition + blockWidth), (int) (renderIterator.yPosition + height));
    }

    protected abstract void cache(RenderIterator iterator);

    protected abstract void draw(RenderIterator iterator);

    protected abstract Rect calculate(RenderIterator iterator);
}
