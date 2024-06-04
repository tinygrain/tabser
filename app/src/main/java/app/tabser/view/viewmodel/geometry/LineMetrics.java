package app.tabser.view.viewmodel.geometry;

import android.graphics.Rect;

import java.util.List;
import java.util.Map;

import app.tabser.dom.Bar;
import app.tabser.dom.Note;
import app.tabser.dom.Song;
import app.tabser.view.viewmodel.RenderModel;

public final class LineMetrics {

    public static LineMetrics calculateLine(RenderModel.RenderIterator renderIterator) {
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
        int calculatedBarCount = 0;
        int calculatedBeatCount = 0;
        Song model = renderIterator.getModel().song;
        List<Bar> bars = model.getBars(renderIterator.sequenceKey);
        boolean eol = false;
//        LineMetrics lineMetrics = new LineMetrics();
        float width = blockWidth;
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
                                xElements = calculatedBeatCount = i + 1;
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
                    calculatedBarCount++;
                    calculatedWidth += barWidth;
                    xElements += notes.size();
                }
            }
//        } else {
//            renderIterator.endReached = true;
        }
//        float xStart = xStart;
        float xIncrement;
        boolean finalLine = false;
        if (eol) {
            /*
             * actual element width > default width
             *
             * beat, rhythm, break,
             */
            xIncrement = blockWidth / xElements;
        } else {
            finalLine = true;
            xIncrement = defaultWidth;
        }
        float xEnd = xStart + blockWidth;
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
        float yPage = renderIterator.yPosition;
        /*
         * 1. Staff lines required
         */
        int staffLineMin = 0;// on bottom line
        int staffLineMax = 8;// on top line (nr 5 in 1/2 line steps)

        if (calculatedBeatCount > 0) {
            List<Map<Integer, Note>> notes = bars.get(renderIterator.barOffset).getNotes();
            for (int beatIndex = renderIterator.beatOffset;
                 beatIndex < renderIterator.beatOffset + calculatedBeatCount;
                 beatIndex++) {
                for (Map<Integer, Note> beatNotes : notes) {
                    for (Integer key : beatNotes.keySet()) {
                        int notePosition = beatNotes.get(key).position(model.getClef());
                        staffLineMin = Math.min(staffLineMin, notePosition);
                        staffLineMax = Math.max(staffLineMax, notePosition);
                    }
                }
            }
        } else if (calculatedBarCount > 0) {
            for (int barIndex = renderIterator.barOffset;
                 barIndex < renderIterator.barOffset + calculatedBarCount;
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
        int staffTotal = Math.abs(staffLineMin - staffLineMax);
        float yStaffStart = yCursor + (staffLineMax - 8) * (yInc / 2);
        /*
         * 2 Song text position
         */
        float yTextStart = yStaffStart + 4 * yInc
                + (Math.abs(staffLineMin) * yInc / 2);
        /*
         * 3 Tab position
         */
        float yTabStart = yTextStart + 3 * yInc;
        float height = yTabStart + ((model.getTuning().getStringCount() - 1) * yInc);
        int tabTotal = model.getTuning().getStringCount();
        //renderIterator.yPosition += lineDimensions.height;
        return new LineMetrics(staffTotal, tabTotal, width, xStart, xEnd, xIncrement, yPage, height,
                yStaffStart, yTextStart, yTabStart, calculatedBeatCount, calculatedBarCount, finalLine);
    }

    public final int staffTotal;
    public  final int tabTotal;
    public  final float width;
    public final float xStart;
    public final float xEnd;
    public final float xIncrement;
    public final float yPage;
    public final float height;
    public final float yStaffStart;
    public final float yTextStart;
    public final float yTabStart;
    public final int calculatedBeatCount;
    public final int calculatedBarCount;
    public final boolean finalLine;

    private LineMetrics(int staffTotal, int tabTotal, float width, float xStart, float xEnd,
                        float xIncrement, float yPage, float height, float yStaffStart,
                        float yTextStart, float yTabStart, int calculatedBeatCount,
                        int calculatedBarCount, boolean finalLine) {
        this.staffTotal = staffTotal;
        this.tabTotal = tabTotal;
        this.width = width;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.xIncrement = xIncrement;
        this.yPage = yPage;
        this.height = height;
        this.yStaffStart = yStaffStart;
        this.yTextStart = yTextStart;
        this.yTabStart = yTabStart;
        this.calculatedBeatCount = calculatedBeatCount;
        this.calculatedBarCount = calculatedBarCount;
        this.finalLine = finalLine;
    }

    public Rect getBounds() {
        return new Rect((int) xStart, (int) yPage, (int) xEnd, (int) (yPage + height));
    }
}