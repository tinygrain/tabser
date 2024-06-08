package app.tabser.rendering.blocks;

import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.RenderModel;

public interface RenderBlock {

    Rectangle getRelativeBounds();

    //    void calculate(RenderIterator renderIterator);
    Rectangle getBounds();

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
