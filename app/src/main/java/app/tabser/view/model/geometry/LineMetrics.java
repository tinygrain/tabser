package app.tabser.view.model.geometry;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.tabser.model.Bar;
import app.tabser.model.Note;
import app.tabser.model.Song;
import app.tabser.view.model.definition.Design;
import app.tabser.view.render.RenderIterator;

public final class LineMetrics {
    public static LineMetrics calculateLine(RenderIterator renderIterator) {
        //Rect viewPort = renderIterator.getModel().sheet.getViewPort();
        float fullWidth = renderIterator.getModel().getFullWidth();
        //float fullWidth = tabView.getTabViewWidth() - design.getxStart() - xStart;
        SheetMetrics metrics = renderIterator.getModel().getSheetMetrics();
        float defaultWidth = metrics.yIncrement * 2;
//        float defaultWidth = design.getYIncrement() * 2;
        float calculatedWidth = 0f;
        float xStart = metrics.xMargin;
        int xElements = 0;
        int yElements = 0;
        renderIterator.calculatedBarCount = 0;
        renderIterator.calculatedBeatCount = 0;
        Song model = renderIterator.getModel().song;
        ArrayList<Bar> bars = model.getBars(renderIterator.sequenceKey);
        boolean eol = false;
        LineMetrics lineDimensions = new LineMetrics();
        lineDimensions.yOffset = renderIterator.yPosition;
        lineDimensions.width = fullWidth;
        if (renderIterator.barOffset < bars.size()) {
            outer:
            for (int barIndex = renderIterator.barOffset; barIndex < bars.size(); barIndex++) {
                Bar bar = bars.get(barIndex);
                List<Map<Integer, Note>> notes = bar.getNotes();
                if (barIndex == renderIterator.barOffset) {
                    if (notes.size() * defaultWidth > fullWidth) {
                        for (int i = notes.size() - 1; i > -1; i--) {
                            if (i * defaultWidth < fullWidth) {
                                xElements = renderIterator.calculatedBeatCount = i + 1;
                                eol = true;
                                break outer;
                            }
                        }
                    }
                }
                float barWidth = notes.size() * defaultWidth;
                if (barWidth + calculatedWidth > fullWidth) {
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
        lineDimensions.xStart = xStart;
        if (eol) {
            lineDimensions.xIncrement = fullWidth / xElements;
        } else {
            renderIterator.endReached = true;
            lineDimensions.xIncrement = defaultWidth;
        }
        float yInc = metrics.yIncrement;
        float yCursor = 3 * yInc;

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
        lineDimensions.staffTotal = Math.abs(staffLineMin - staffLineMax);
        lineDimensions.yStaffStart = yCursor + (staffLineMax - 8) * (yInc / 2);
        lineDimensions.yTextStart = lineDimensions.yStaffStart + 4 * yInc
                + (Math.abs(staffLineMin) * (yInc) / 2);
        lineDimensions.yTabStart = lineDimensions.yTextStart + 3 * yInc;
        lineDimensions.height = lineDimensions.yTabStart + ((model.getTuning().getStringCount() - 1) * yInc);
        ;
        lineDimensions.tabTotal = model.getTuning().getStringCount();
        renderIterator.yPosition += lineDimensions.height;
        return lineDimensions;
    }

    private LineMetrics() {
    }

    public int staffTotal;
    public int tabTotal;
    public float width;
    public float xStart;
    public float xIncrement;
    public float yOffset;
    public float height;
    public float yStaffStart;
    public float yTextStart;
    public float yTabStart;

    public Rect getBounds() {
        return new Rect((int) xStart, (int) yOffset, (int) (xStart + width), (int) (yOffset + height));
    }
}