package app.tabser.view.model.blocks;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import app.tabser.model.Song;
import app.tabser.view.model.definition.RenderBlock;
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
        super(renderIterator.options.viewPort);
        //calculate(renderIterator);
    }

    @Override
    protected Rect calculate(RenderIterator renderIterator) {
        metrics = LineMetrics.calculateLine(renderIterator);
        lineIndex = renderIterator.lineOffset;
        barOffset = renderIterator.barOffset;
        barCount = renderIterator.calculatedBarCount;
        beatOffset = renderIterator.beatOffset;
        beatCount = renderIterator.calculatedBeatCount;
        return metrics.getBounds();
    }

    @Override
    protected void draw(RenderIterator iterator) {
        Sheet sheet = iterator.getModel().sheet;
        Design design = iterator.getModel().design;
        boolean compact = iterator.getModel().getSheetMetrics().compact;
        if (!compact) {
            drawStaffLines(sheet);
            drawClef(sheet, iterator.getModel().song.getClef(), metrics.yStaffStart);
        }
        //drawStaffLines(sheet);
//        sheet.setForegroundColor(Color.MAGENTA);
//        sheet.drawRect(getRelativeBounds());
        sheet.resetParameters();
    }

    @Override
    public void cache(RenderIterator iterator) {

    }

    private void drawStaffLines(Sheet sheet) {
        final Rect bounds = getRelativeBounds();
        final float xStart = bounds.left;
        final float xEnd = bounds.right;
        final float yStart = bounds.top + metrics.yStaffStart;
        float yCursor = yStart;
        final float yIncrement = sheet.getMetrics().yIncrement;
        // float yIncrement = design.getYIncrement();
        for (int i = 0; i < 5; i++) {
            sheet.drawLine(xStart, yCursor, xEnd, yCursor);
            yCursor += yIncrement;
        }
        //int clefStart = drawClef(canvas, paint, model.getClef(), yStart);
        //yCursor += 3 * yIncrement;
        //float xCursor = 2 * xStart + clefStart + xStart;
        //return new float[]{xCursor, yCursor};
    }

    private void drawTabulatureLines(Sheet sheet) {
        float yCursor = metrics.yTabStart;
        float xStart = metrics.xStart;
        float yIncrement = sheet.getMetrics().yIncrement;
        int stringCount = metrics.tabTotal;
        for (int i = 0; i < stringCount; i++) {
            sheet.drawLine(xStart, yCursor, metrics.xEnd, yCursor);
            if (i + 1 < stringCount) {
                yCursor += yIncrement;
            }
        }
    }

    private int drawClef(Sheet sheet, Song.Clef clef, float yStart) {
        return drawClef(sheet, clef, yStart, -1);
    }

    private int drawClef(Sheet sheet, Song.Clef clef, float yStart, int stringCount) {
        int width;
        float xStart = metrics.xStart;
        int x = (int) xStart;
        float yIncrement = sheet.getMetrics().yIncrement;
        if (clef == Song.Clef.BASS) {
            width = (int) (yIncrement * (3.3 / 20 * 18));
//            Drawable bassClef = ViewUtils.getDrawable(context, R.drawable.f_clef, x, (int) yStart,
//                    width, (int) (yIncrement * 3.3));
//            bassClef.setTint(design.getBackgroundColorSheet());
//            bassClef.draw(canvas);
//            bassClef.setTint(paint.getColor());
//            bassClef.draw(canvas);
        } else if (clef == Song.Clef.TREBLE) {
            width = (int) (yIncrement * (7 / 165.6 * 58.6));
//            Drawable trebleClef = ViewUtils.getDrawable(context, R.drawable.g_clef, x,
//                    (int) (yStart - yIncrement * 1.33), width, (int) (yIncrement * 7));
////            trebleClef.setTint(design.getBackgroundColorSheet());
////            trebleClef.draw(canvas);
//            trebleClef.setTint(paint.getColor());
//            trebleClef.draw(canvas);
        } else {
            int tabClefHeight = (int) ((stringCount - 1) * yIncrement);
            int tabClefWidth = (int) (tabClefHeight / 112.3f * 27.7f);
//            Drawable tabClef = ViewUtils.getDrawable(context, R.drawable.tab_clef, (int) (xStart * 2),
//                    (int) (yStart + tabClefHeight * 0.05), tabClefWidth, (int) (tabClefHeight * 0.9));
////            tabClef.setTint(design.getBackgroundColorSheet());
////            tabClef.draw(canvas);
//            tabClef.setTint(paint.getColor());
//            tabClef.draw(canvas);
            width = tabClefWidth;
        }
        return width;
    }
}
