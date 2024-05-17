package app.tabser.view.render;

import android.graphics.Rect;

import java.util.ArrayList;

import app.tabser.model.Sequence;
import app.tabser.view.SheetView;
import app.tabser.view.model.blocks.LineBlock;

public final class RenderIterator {
    //    @Deprecated
//    public ArrayList<SheetView.ModelCursor> cursorPositions;
    public int barOffset;
    public int beatOffset;
    public int lineOffset;
    public int pageOffset;
    public float yPosition;
    public boolean pageEndReached;
    public boolean endReached;
    public int calculatedBarCount;
    public int calculatedBeatCount;
    public String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;

    private final RenderModel renderModel;

    RenderIterator(RenderModel renderModel) {
        this.renderModel = renderModel;
    }

    public RenderModel getModel() {
        return renderModel;
    }

    public boolean hasNext() {
        return !endReached;
    }

    public void incrementYPosition(Rect bounds) {
        yPosition = bounds.bottom;
    }

    public void incrementModel(LineBlock lineBlock) {
        if (lineBlock.beatCount > 0) {
            beatOffset+= lineBlock.beatCount;
        } else if (lineBlock.barCount > 0) {
            barOffset+=lineBlock.barCount;
        }
        incrementYPosition(lineBlock.getBounds());
    }
}