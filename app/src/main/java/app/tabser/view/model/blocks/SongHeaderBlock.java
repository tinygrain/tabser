package app.tabser.view.model.blocks;

import android.graphics.Color;

import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;

public class SongHeaderBlock extends AbstractBlock implements RenderBlock {
    public SongHeaderBlock() {
    }

    @Override
    public void calculate(RenderIterator renderIterator) {
        float fullWidth = renderIterator.getModel().getFullWidth();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        int height = (int) (metrics.yIncrement * 3);
        setBounds((int) metrics.xMargin, (int) metrics.yMargin, (int) fullWidth, height);
        renderIterator.incrementYPosition(getBounds());
    }

    @Override
    public <T> T touch(float x, float y, boolean longClick, Class<T> resultClass) {
        return null;
    }

    @Override
    public void render(RenderIterator iterator) {
        Sheet sheet = iterator.getModel().sheet;
        sheet.setForegroundColor(Color.CYAN);
        sheet.drawRect(getBounds());
        sheet.resetParameters();
//        sheet.drawText();
        iterator.incrementYPosition(getBounds());
    }
}
