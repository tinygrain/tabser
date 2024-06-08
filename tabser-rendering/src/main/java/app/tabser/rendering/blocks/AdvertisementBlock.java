package app.tabser.rendering.blocks;

import app.tabser.rendering.RenderModel;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;

public class AdvertisementBlock extends AbstractBlock implements RenderBlock {
    public AdvertisementBlock(Rectangle viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    @Override
    protected Rectangle calculate(RenderModel.RenderIterator renderIterator) {
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        float height = metrics.yIncrement * 3;
        float fullWidth = renderIterator.getModel().getBlockWidth();
        //setBounds((int) metrics.xMargin, (int) renderIterator.yPosition, (int) fullWidth, (int) height);
        return boundsOnPage(renderIterator, (int) height);
    }

    @Override
    protected void draw(RenderModel.RenderIterator iterator) {

    }
}
