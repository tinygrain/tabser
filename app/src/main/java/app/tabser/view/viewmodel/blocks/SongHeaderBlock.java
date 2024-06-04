package app.tabser.view.viewmodel.blocks;

import android.graphics.Color;
import android.graphics.Rect;

import app.tabser.view.viewmodel.RenderModel;
import app.tabser.view.render.Sheet;
import app.tabser.view.viewmodel.geometry.SheetMetrics;
import app.tabser.view.viewmodel.geometry.ViewPort;

public class SongHeaderBlock extends AbstractBlock implements RenderBlock {

    public SongHeaderBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    protected Rect calculate(RenderModel.RenderIterator renderIterator) {
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        int height = (int) (metrics.yIncrement * 3);
        return boundsOnPage(renderIterator, height);
    }

    protected void draw(RenderModel.RenderIterator iterator) {
        Sheet sheet = iterator.getModel().sheet;
        sheet.setForegroundColor(Color.CYAN);
        sheet.drawRect(getRelativeBounds());
        sheet.resetParameters();
    }
}
