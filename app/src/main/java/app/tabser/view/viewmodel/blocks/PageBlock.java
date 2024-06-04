package app.tabser.view.viewmodel.blocks;

import android.graphics.Rect;

import app.tabser.view.viewmodel.RenderModel;
import app.tabser.view.viewmodel.geometry.ViewPort;
import app.tabser.view.viewmodel.geometry.SheetMetrics;

public class PageBlock extends AbstractBlock implements RenderBlock {
    public PageBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    @Override
    protected Rect calculate(RenderModel.RenderIterator renderIterator) {
        float fullWidth = renderIterator.getModel().getBlockWidth();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        float height = 2 * metrics.yIncrement;
        // setBounds((int) metrics.xMargin, (int) renderIterator.yPosition, (int) fullWidth, (int) height);
        return boundsOnPage(renderIterator, (int) height);
    }

    @Override
    protected void draw(RenderModel.RenderIterator iterator) {

    }
}
