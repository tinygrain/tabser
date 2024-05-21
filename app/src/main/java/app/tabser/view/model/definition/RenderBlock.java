package app.tabser.view.model.definition;

import android.graphics.Rect;

import app.tabser.view.render.RenderIterator;

public interface RenderBlock {

    Rect getRelativeBounds();

    //    void calculate(RenderIterator renderIterator);
    Rect getBounds();

    default boolean contains(float x, float y) {
        return getBounds().contains((int) x, (int) y);
    }

    default boolean containsRelative(float x, float y) {
        return getRelativeBounds().contains((int) x, (int) y);
    }

    void touch(float x, float y, boolean longClick);

    void render(RenderIterator iterator);

    void invalidate();

    boolean isValid();
}
