package app.tabser.view.model.definition;

import app.tabser.view.model.blocks.LineBlock;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.render.RenderModel;

public interface SongRenderer {
    void renderDocument(RenderIterator iterator);
    void calculateModel();

    default void preparePage(RenderIterator iterator) {
    }

    default void newPage(RenderIterator iterator) {
    }

    void renderLine(RenderIterator iterator);

    int getYMin();

    LineBlock getLine(int i);

    Sheet getSheet();

    RenderIterator iterator();

    float getHeaderHeight();
}
