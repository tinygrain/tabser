package app.tabser.view;

import android.graphics.Rect;

import java.util.List;

import app.tabser.dom.Bar;
import app.tabser.dom.Sequence;
import app.tabser.dom.Song;
import app.tabser.view.render.RenderOptions;

public final class SheetCursor {
    public String sequenceKey = Sequence.DEFAULT_SEQUENCE_NAME;
    public int barIndex;
    public int beatIndex;
    public Rect selectionArea;
    public RenderOptions.CursorType type = RenderOptions.CursorType.ITEM;
    //private Rect responseArea;
    public int lineIndex;

    private void setRect(float left, float top, float right, float bottom) {
        setRect((int) left, (int) top, (int) right, (int) bottom);
    }

    private void setRect(int left, int top, int right, int bottom) {
        selectionArea = new Rect(left, top, right, bottom);
    }

    public boolean isTrailing(Song model) {
        List<Bar> bars = model.getBars(sequenceKey);
        int lastBarSize = bars.size() > 0 ? bars.get(bars.size() - 1).size() : 0;
        return barIndex >= bars.size() - 1 && beatIndex == lastBarSize;
    }
}

