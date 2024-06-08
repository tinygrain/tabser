package app.tabser.rendering.blocks;

import app.tabser.rendering.RenderModel;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;

public class CommentBlock extends AbstractBlock implements RenderBlock {
    public CommentBlock(Rectangle viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    @Override
    protected Rectangle calculate(RenderModel.RenderIterator iterator) {
        SheetMetrics sheetMetrics = iterator.options.sheetMetrics;
        int left = (int) sheetMetrics.pageSideMargin;
        int right = (int) iterator.getModel().getBlockWidth() + left;
        int top = (int) iterator.yPosition;
        int bottom = top + (int) (3 * sheetMetrics.yIncrement);
        int height = top-bottom;
        return boundsOnPage(iterator, height);
    }

    @Override
    protected void draw(RenderModel.RenderIterator iterator) {

    }
}
