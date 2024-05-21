package app.tabser.view.model.blocks;

import android.graphics.Rect;

import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.geometry.ViewPort;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public class CommentBlock extends AbstractBlock implements RenderBlock {
    public CommentBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderIterator iterator) {

    }

    @Override
    protected Rect calculate(RenderIterator iterator) {
        SheetMetrics sheetMetrics = iterator.options.sheetMetrics;
        int left = (int) sheetMetrics.pageSideMargin;
        int right = (int) iterator.getModel().getBlockWidth() + left;
        int top = (int) iterator.yPosition;
        int bottom = top + (int) (3 * sheetMetrics.yIncrement);
        int height = top-bottom;
        return boundsOnPage(iterator, height);
    }

    @Override
    protected void draw(RenderIterator iterator) {

    }
}
