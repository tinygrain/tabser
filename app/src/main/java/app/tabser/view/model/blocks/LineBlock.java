package app.tabser.view.model.blocks;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import app.tabser.model.Song;
import app.tabser.view.SheetView;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.geometry.LineMetrics;

public class LineBlock extends AbstractBlock implements RenderBlock {
    private static class LineLayersCache {
        Bitmap backgroundLayer;
        Bitmap cursorLayer;
        Bitmap staffLayer;
        Bitmap notesLayer;
    }
    public LineMetrics metrics;
    public int lineIndex;
    public int barOffset;
    public int barCount;
    public int beatOffset;
    public int beatCount;
    public boolean lastLine;
    public LineLayersCache cachedLine = new LineLayersCache();

    public LineBlock(RenderIterator renderIterator) {
//        metrics = LineMetrics.calculateLine(fullWidth, renderIterator, design, model);
        lineIndex = renderIterator.lineOffset;
        barOffset = renderIterator.barOffset;
        barCount = renderIterator.calculatedBarCount;
        beatOffset = renderIterator.beatOffset;
        beatCount = renderIterator.calculatedBeatCount;
    }

    @Override
    public void calculate(RenderIterator renderIterator) {
        metrics = LineMetrics.calculateLine(renderIterator);
        renderIterator.incrementModel(this);
    }

    @Override
    public Rect getBounds() {
        return metrics.getBounds();
    }

    @Override
    public <T> T touch(float x, float y, boolean longClick, Class<T> answerClass) {
        if (answerClass == SheetView.ModelCursor.class) {

        }
        return null;
    }

    public void cache(){

    }

    @Override
    public void render(RenderIterator iterator) {
        Sheet sheet = iterator.getModel().sheet;
        sheet.setForegroundColor(Color.MAGENTA);
        sheet.drawRect(getBounds());
        sheet.resetParameters();
        iterator.incrementModel(this);
    }
}
