package app.tabser.view.model.blocks;

import android.graphics.Rect;

import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.geometry.ViewPort;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public class PageBlock extends AbstractBlock implements RenderBlock {
    public PageBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderIterator iterator) {

    }

    @Override
    protected Rect calculate(RenderIterator renderIterator) {
        float fullWidth = renderIterator.getModel().getBlockWidth();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        float height = 2 * metrics.yIncrement;
        // setBounds((int) metrics.xMargin, (int) renderIterator.yPosition, (int) fullWidth, (int) height);
        return boundsOnPage(renderIterator, (int) height);
    }

    @Override
    protected void draw(RenderIterator iterator) {

    }
}
