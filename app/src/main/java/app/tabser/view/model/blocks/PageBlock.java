package app.tabser.view.model.blocks;

import android.graphics.Rect;

import app.tabser.model.Song;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public class PageBlock extends AbstractBlock implements RenderBlock {
    @Override
    public void calculate( RenderIterator renderIterator) {
//        Rect viewPort = renderIterator.getModel().sheet.getViewPort();
        float fullWidth= renderIterator.getModel().getFullWidth();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        float height = 2 * metrics.yIncrement;
        setBounds((int) metrics.xMargin, (int) renderIterator.yPosition, (int) fullWidth, (int) height);
        renderIterator.incrementYPosition(getBounds());
    }

    @Override
    public <T> T touch(float x, float y, boolean longClick, Class<T> resultClass) {
        return null;
    }

    @Override
    public void render(RenderIterator iterator) {

        iterator.incrementYPosition(getBounds());
    }
}
