package app.tabser.view;

import android.graphics.Rect;

import java.util.ArrayList;

import app.tabser.model.Bar;
import app.tabser.model.Sequence;
import app.tabser.model.Song;
import app.tabser.view.render.RenderOptions;

public final class SongCursor {
    public String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;
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
        ArrayList<Bar> bars = model.getBars(sequenceKey);
        int lastBarSize = bars.size() > 0 ? bars.get(bars.size() - 1).size() : 0;
        return barIndex >= bars.size() - 1 && beatIndex == lastBarSize;
    }
}

