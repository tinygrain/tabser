package app.tabser.view.viewmodel.blocks;

import android.graphics.Rect;

import app.tabser.view.viewmodel.RenderModel;

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

    void render(RenderModel.RenderIterator iterator);

    void invalidate();

    boolean isValid();
}
