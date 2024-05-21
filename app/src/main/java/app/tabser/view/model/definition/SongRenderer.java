package app.tabser.view.model.definition;

import app.tabser.view.render.RenderIterator;
import app.tabser.view.render.RenderOptions;

public interface SongRenderer {
    void renderDocument(RenderOptions options);

    default void preProcess(RenderIterator iterator) {
    }

    default void postProcess(RenderIterator iterator) {
    }

    default void newPage(RenderIterator iterator) {
    }

    int getYMin();

    Sheet getSheet();

    float getBlockOffsetY(int lineIndex);
}
