package app.tabser.rendering.blocks;

import app.tabser.rendering.RenderModel;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;

public class PageBlock extends AbstractBlock implements RenderBlock {
    public PageBlock(Rectangle viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    @Override
    protected Rectangle calculate(RenderModel.RenderIterator renderIterator) {
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
