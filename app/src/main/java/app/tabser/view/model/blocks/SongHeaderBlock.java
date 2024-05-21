package app.tabser.view.model.blocks;

import android.graphics.Color;
import android.graphics.Rect;

import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.ViewPort;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public class SongHeaderBlock extends AbstractBlock implements RenderBlock {

    public SongHeaderBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderIterator iterator) {

    }

    protected Rect calculate(RenderIterator renderIterator) {
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        int height = (int) (metrics.yIncrement * 3);
        return boundsOnPage(renderIterator, height);
    }

    protected void draw(RenderIterator iterator) {
        Sheet sheet = iterator.getModel().sheet;
        sheet.setForegroundColor(Color.CYAN);
        sheet.drawRect(getRelativeBounds());
        sheet.resetParameters();
    }
}
