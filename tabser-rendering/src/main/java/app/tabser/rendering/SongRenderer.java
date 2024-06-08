package app.tabser.rendering;

import app.tabser.rendering.geometry.Rectangle;

public interface SongRenderer {
    /**
     * Renders the document
     * @param options the opts
     * @return the document size
     */
    Rectangle renderDocument(RenderOptions options);

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

    float getYMin();

    Sheet getSheet();

    float getBlockOffsetY(int lineIndex);
}
