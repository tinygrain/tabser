package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Objects;

import app.tabser.R;
import app.tabser.model.Bar;
import app.tabser.model.Note;
import app.tabser.model.TabModel;

class TabSheet {
    private static final int CURSOR_COLOR = Color.GREEN;
    private static final int FOREGROUND_COLOR = Color.BLACK;
    private static final int BACKGROUND_COLOR = Color.WHITE;
    private boolean insert;
    private int barIndex;
    private int beatIndex;
    private final TabView tabView;
    private float strokeWidth = 5f;
    private float yIncrement = 50f;
    private float yStart = yIncrement * 2;
    private float xStart = 25f;
    private TabModel model;
    private Context context;
    private boolean compact;
    private boolean autoNext;
    private boolean autoBar;
    private float xCursor;

    TabSheet(TabView tabView) {
        this.context = tabView.getContext();
        this.tabView = tabView;
    }

    void loadModel(TabModel model) {
        this.model = model;
    }

    void drawSheet(Canvas canvas, Paint paint, boolean fullView, int stringCount) {
        float yPos = yStart;
        Rect all = new Rect(0, 0, tabView.getTabViewWidth(), tabView.getTabViewHeight());
        paint.setColor(BACKGROUND_COLOR);
        canvas.drawRect(all, paint);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(FOREGROUND_COLOR);
        float xCursor = 0;
        if (fullView) {
            for (int i = 0; i < 5; i++) {
                canvas.drawLine(xStart, yPos, tabView.getTabViewWidth() - xStart, yPos, paint);
                yPos += yIncrement;
            }
            int x = (int) (xStart * 2);
            int width = 0;
            if (model.getClef() == TabModel.Clef.BASS) {
                width = (int) (yIncrement * (3.3 / 20 * 18));
                Drawable bassClef = ViewUtils.getDrawable(context, R.drawable.f_clef, x, (int) yStart,
                        width, (int) (yIncrement * 3.3));
                bassClef.draw(canvas);
            } else {
                width = (int) (yIncrement * (7 / 165.6 * 58.6));
                Drawable trebleClef = ViewUtils.getDrawable(context, R.drawable.g_clef, x,
                        (int) (yStart - yIncrement * 1.33), width, (int) (yIncrement * 7));
                trebleClef.draw(canvas);
            }
            yPos += 3 * yIncrement;
            xCursor = 2 * xStart + width + xStart;
        }
        float yTab = yPos;
        for (int i = 0; i < stringCount; i++) {
            canvas.drawLine(xStart, yPos, tabView.getTabViewWidth() - xStart, yPos, paint);
            if (i + 1 < stringCount) {
                yPos += yIncrement;
            }
        }
        int tabClefHeight = (int) ((stringCount - 1) * yIncrement);
        int tabClefWidth = (int) (tabClefHeight / 112.3f * 27.7f);
        Drawable tabClef = ViewUtils.getDrawable(context, R.drawable.tab_clef, (int) (xStart * 2),
                (int) (yTab + tabClefHeight * 0.05), tabClefWidth, (int) (tabClefHeight * 0.9));
        tabClef.draw(canvas);
        canvas.drawLine(xStart, yStart, xStart, yPos, paint);
        /*
         * Notes
         */
        paint.setTextSize(yIncrement);
        xCursor = Math.max(xCursor, xStart * 2 + tabClefWidth + xStart);
        int barIndex = 0;
        int beatIndex = 0;
        for (Bar bar : model.getBars()) {
            beatIndex = 0;
            for (Note[] notes : bar.getNotes()) {
                int maxWidth = 0;
                for (Note n : notes) {
                    if (Objects.nonNull(n)) {
                        int string = n.getString();
                        String fret = n.getFret() > -1 ? String.valueOf(n.getFret()) : "X";
                        Rect fretRect = new Rect();
                        paint.getTextBounds(fret, 0, fret.length(), fretRect);
                        float y = yTab + yIncrement * (stringCount - 1 - string) + yIncrement / 3;
                        paint.setColor(BACKGROUND_COLOR);
                        Rect blankRect = new Rect((int) xCursor, (int) y + fretRect.top, (int) (xCursor + fretRect.right), (int) (y));
                        canvas.drawRect(blankRect, paint);
                        paint.setColor(FOREGROUND_COLOR);
                        canvas.drawText(fret, xCursor, y, paint);
                        maxWidth = Math.max(maxWidth, fretRect.right);
                    }
                }
                if (this.barIndex == barIndex && this.beatIndex == beatIndex) {
                    this.xCursor = xCursor;
                }
                xCursor += maxWidth + xStart;
                beatIndex++;
            }
            canvas.drawLine(this.xCursor, yStart, this.xCursor, yTab + yIncrement * (model.getTuning().getStringCount() - 1), paint);
            xCursor += xStart;
            barIndex++;
        }
        if (this.barIndex == barIndex - 1 && this.beatIndex == beatIndex) {
            this.xCursor = xCursor;
        }
        paint.setColor(CURSOR_COLOR);
        canvas.drawLine(this.xCursor, yStart, this.xCursor, yTab + yIncrement * (model.getTuning().getStringCount() - 1), paint);
    }

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public boolean isAutoNext() {
        return autoNext;
    }

    public void setAutoNext(boolean autoNext) {
        this.autoNext = autoNext;
    }

    public boolean isAutoBar() {
        return autoBar;
    }

    public void setAutoBar(boolean autoBar) {
        this.autoBar = autoBar;
    }

    public boolean toggleInsert() {
        return (this.insert = !this.insert);
    }

    public void start() {
        barIndex = 0;
        beatIndex = 0;
    }

    public void previousBeat() {
        if (beatIndex > 0) {
            beatIndex--;
        } else {
            previousBar();
        }
    }

    public void previousBar() {
        if (barIndex > 0) {
            barIndex--;
            beatIndex = model.getBars().get(barIndex).size() - 1;
        }
    }

    public void nextBeat() {
        if (beatIndex < model.getBars().get(model.getBars().size() - 1).size()) {
            beatIndex++;
        }
    }

    public void nextBar() {
        barIndex++;
        beatIndex = 0;
    }

    public void end() {
        barIndex = model.getBars().size() - 1;
        beatIndex = model.getBars().get(barIndex).size();
    }

    float getXStart() {
        return xStart;
    }

    float getStrokeWidth() {
        return strokeWidth;
    }

    public int getBarIndex() {
        return barIndex;
    }

    public int getBeatIndex() {
        return beatIndex;
    }

}
