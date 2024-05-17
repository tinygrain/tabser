package app.tabser.view.model.blocks;

import android.graphics.Rect;

import app.tabser.model.Song;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.render.RenderModel;

public interface RenderBlock {

    void calculate(RenderIterator renderIterator);
    Rect getBounds();

    default boolean contains(float x, float y) {
        return getBounds().contains((int) x, (int) y);
    }

    <T> T touch(float x, float y, boolean longClick, Class<T> resultClass);

    void render(RenderIterator iterator);

    void invalidate();

    boolean isValid();
}
