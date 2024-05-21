package app.tabser.view.model.blocks;

import android.graphics.Rect;

import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.geometry.ViewPort;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public class AdvertisementBlock extends AbstractBlock implements RenderBlock {
    public AdvertisementBlock(ViewPort viewPort) {
        super(viewPort);
    }

    @Override
    protected void cache(RenderIterator iterator) {

    }

    @Override
    protected Rect calculate(RenderIterator renderIterator) {
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        float height = metrics.yIncrement * 3;
        float fullWidth = renderIterator.getModel().getBlockWidth();
        //setBounds((int) metrics.xMargin, (int) renderIterator.yPosition, (int) fullWidth, (int) height);
        return boundsOnPage(renderIterator, (int) height);
    }

    @Override
    protected void draw(RenderIterator iterator) {
    }
}
