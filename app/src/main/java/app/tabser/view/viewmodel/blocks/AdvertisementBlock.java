package app.tabser.view.viewmodel.blocks;

import android.graphics.Rect;

import app.tabser.view.viewmodel.RenderModel;
import app.tabser.view.render.RenderBlock;
import app.tabser.view.viewmodel.geometry.SheetMetrics;
import app.tabser.view.viewmodel.geometry.ViewPort;

public class AdvertisementBlock extends AbstractBlock implements RenderBlock {
    public AdvertisementBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderModel.RenderIterator iterator) {

    }

    @Override
    protected Rect calculate(RenderModel.RenderIterator renderIterator) {
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
