package app.tabser.view.viewmodel.blocks;

import android.graphics.Rect;

import app.tabser.view.viewmodel.RenderModel;
import app.tabser.view.render.RenderBlock;
import app.tabser.view.viewmodel.geometry.SheetMetrics;
import app.tabser.view.viewmodel.geometry.ViewPort;

public class CommentBlock extends AbstractBlock implements RenderBlock {
    public CommentBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    @Override
    protected Rect calculate(RenderModel.RenderIterator iterator) {
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
