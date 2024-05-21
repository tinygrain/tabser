package app.tabser.view.model.geometry;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.tabser.model.Bar;
import app.tabser.model.Note;
import app.tabser.model.Song;
import app.tabser.view.render.RenderIterator;

public final class LineMetrics {

    public static LineMetrics calculateLine(RenderIterator renderIterator) {
        //Rect viewPort = renderIterator.getModel().sheet.getViewPort();
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        /*
         * Vertical axis analysis (width)
         */
        float blockWidth = renderIterator.getModel().getBlockWidth();
        //float fullWidth = tabView.getTabViewWidth() - design.getxStart() - xStart;
        float defaultWidth = metrics.yIncrement * 2;
//        float defaultWidth = design.getYIncrement() * 2;
        float calculatedWidth = 0f;
        float xStart = renderIterator.xPosition;
        int xElements = 0;
//        int yElements = 0;
        renderIterator.calculatedBarCount = 0;
        renderIterator.calculatedBeatCount = 0;
        Song model = renderIterator.getModel().song;
        ArrayList<Bar> bars = model.getBars(renderIterator.sequenceKey);
        boolean eol = false;
        LineMetrics lineMetrics = new LineMetrics();
        lineMetrics.width = blockWidth;
        if (renderIterator.barOffset < bars.size()) {
            outer:
            /*
             * loop through bars
             */
            for (int barIndex = renderIterator.barOffset; barIndex < bars.size(); barIndex++) {
                Bar bar = bars.get(barIndex);
                List<Map<Integer, Note>> notes = bar.getNotes();
                if (barIndex == renderIterator.barOffset) {
                    /*
                     * in first round test if the bar is wider than the line width
                     */
                    if (notes.size() * defaultWidth > blockWidth) {
                        for (int i = notes.size() - 1; i > -1; i--) {
                            if (i * defaultWidth < blockWidth) {
                                xElements = renderIterator.calculatedBeatCount = i + 1;
                                eol = true;
                                break outer;
                            }
                        }
                    }
                }
                /*
                 * default bar width
                 */
                float barWidth = notes.size() * defaultWidth;
                if (barWidth + calculatedWidth > blockWidth) {
                    eol = true;
                    break;
                } else {
                    renderIterator.calculatedBarCount++;
                    calculatedWidth += barWidth;
                    xElements += notes.size();
                }
            }
//        } else {
//            renderIterator.endReached = true;
        }
        lineMetrics.xStart = xStart;
        if (eol) {
            /*
             * actual element width > default width
             *
             * beat, rhythm, break,
             */
            lineMetrics.xIncrement = blockWidth / xElements;
        } else {
            renderIterator.endReached = true;
            lineMetrics.xIncrement = defaultWidth;
        }
        lineMetrics.xEnd = xStart + blockWidth;
        /*
         * horizontal axis analysis (height)
         */
        float yInc = metrics.yIncrement;
        /*
         * Y element in block
         */
        float yCursor = 3 * yInc;
        /*
         * Y block on page
         */
        lineMetrics.yPage = renderIterator.yPosition;
        /*
         * 1. Staff lines required
         */
        int staffLineMin = 0;// on bottom line
        int staffLineMax = 8;// on top line (nr 5 in 1/2 line steps)

        if (renderIterator.calculatedBeatCount > 0) {
            List<Map<Integer, Note>> notes = bars.get(renderIterator.barOffset).getNotes();
            for (int beatIndex = renderIterator.beatOffset;
                 beatIndex < renderIterator.beatOffset + renderIterator.calculatedBeatCount;
                 beatIndex++) {
                for (Map<Integer, Note> beatNotes : notes) {
                    for (Integer key : beatNotes.keySet()) {
                        int notePosition = beatNotes.get(key).position(model.getClef());
                        staffLineMin = Math.min(staffLineMin, notePosition);
                        staffLineMax = Math.max(staffLineMax, notePosition);
                    }
                }
            }
        } else if (renderIterator.calculatedBarCount > 0) {
            for (int barIndex = renderIterator.barOffset;
                 barIndex < renderIterator.barOffset + renderIterator.calculatedBarCount;
                 barIndex++) {
                Bar bar = bars.get(barIndex);
                List<Map<Integer, Note>> notes = bar.getNotes();
                for (Map<Integer, Note> beatNotes : notes) {
                    for (Integer key : beatNotes.keySet()) {
                        int notePosition = beatNotes.get(key).position(model.getClef());
                        staffLineMin = Math.min(staffLineMin, notePosition);
                        staffLineMax = Math.max(staffLineMax, notePosition);
                    }
                }
            }
        }
        lineMetrics.staffTotal = Math.abs(staffLineMin - staffLineMax);
        lineMetrics.yStaffStart = yCursor + (staffLineMax - 8) * (yInc / 2);
        /*
         * 2 Song text position
         */
        lineMetrics.yTextStart = lineMetrics.yStaffStart + 4 * yInc
                + (Math.abs(staffLineMin) * yInc / 2);
        /*
         * 3 Tab position
         */
        lineMetrics.yTabStart = lineMetrics.yTextStart + 3 * yInc;
        lineMetrics.height = lineMetrics.yTabStart + ((model.getTuning().getStringCount() - 1) * yInc);
        lineMetrics.tabTotal = model.getTuning().getStringCount();
        //renderIterator.yPosition += lineDimensions.height;
        return lineMetrics;
    }

    private LineMetrics() {
    }

    public int staffTotal;
    public int tabTotal;
    public float width;
    public float xStart;
    public float xEnd;
    public float xIncrement;
    public float yPage;
    public float height;
    public float yStaffStart;
    public float yTextStart;
    public float yTabStart;

    public Rect getBounds() {
        return new Rect((int) xStart, (int) yPage, (int) xEnd, (int) (yPage + height));
    }
}