package app.tabser.view.model.blocks;

import android.graphics.Rect;

import app.tabser.view.render.RenderIterator;

public abstract class AbstractBlock implements RenderBlock {
    private final Rect bounds = new Rect();
    private boolean invalid = true;

    protected void setBounds(int left, int top, int width, int height) {
        bounds.set(left, top, left + width, top + height);
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public void invalidate() {
        this.invalid = true;
    }

    @Override
    public boolean isValid() {
        return !invalid;
    }
}
