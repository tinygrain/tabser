package app.tabser.view.render;

import app.tabser.view.viewmodel.RenderModel;

public interface SongRenderer {
    void renderDocument(RenderOptions options);

    /**
     * Optional operation, by default no-op
     *
     * @param iterator
     */
    default void preparePage(RenderModel.RenderIterator iterator) {
    }

    /**
     * Optional operation, by default no-op
     *
     * @param iterator
     */
    default void postProcessDocument(RenderModel.RenderIterator iterator) {
    }


    /**
     * Optional operation, by default no-op
     *
     * @param iterator
     */
    default void newPage(RenderModel.RenderIterator iterator) {
    }

    int getYMin();

    Sheet getSheet();

    float getBlockOffsetY(int lineIndex);
}
