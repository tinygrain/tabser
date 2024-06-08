package app.tabser.rendering.blocks;

import app.tabser.rendering.RenderModel;
import app.tabser.rendering.Sheet;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;

public class SongHeaderBlock extends AbstractBlock implements RenderBlock {

    public SongHeaderBlock(Rectangle viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    protected Rectangle calculate(RenderModel.RenderIterator renderIterator) {
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        int height = (int) (metrics.yIncrement * 3);
        return boundsOnPage(renderIterator, height);
    }

    protected void draw(RenderModel.RenderIterator iterator) {
        Sheet sheet = iterator.getModel().sheet;
//        sheet.setForegroundColor(Color.CYAN);
        sheet.drawRect(getRelativeBounds());
        sheet.resetParameters();
    }
}
