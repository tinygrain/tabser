package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import app.tabser.R;
import app.tabser.model.Bar;
import app.tabser.model.Note;
import app.tabser.model.Sequence;
import app.tabser.model.TabModel;

class TabSheet {

    public enum Mode {
        EDIT, VIEW;
    }

    private final class ModelCursor {
        private String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;
        private int barIndex;
        private int beatIndex;
        private Rect selectionArea;

        private void setRect(float left, float top, float right, float bottom) {
            setRect((int) left, (int) top, (int) right, (int) bottom);
        }

        private void setRect(int left, int top, int right, int bottom) {
            selectionArea = new Rect(left, top, right, bottom);
        }
    }

    private final int foregroundColorActive;
    private final int foregroundColor;
    private final int backgroundColor;
    private final Design design;
    private boolean insert;
    //private int barIndex;
    //private int beatIndex;
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
    // private float xCursor;
    //private String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;
    private Mode mode = Mode.EDIT;

    private ModelCursor[] displayedCursorPositions;
    private ModelCursor modelCursor;

    TabSheet(TabView tabView, Design design) {
        this.design = design;
        this.context = tabView.getContext();
        this.tabView = tabView;
        this.backgroundColor = design.getBackgroundColorSheet();
        this.foregroundColorActive = design.getForegroundColorActiveSheet();
        this.foregroundColor = design.getForegroundColorInactiveSheet();
        modelCursor = new ModelCursor();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    void loadModel(TabModel model) {
        this.model = model;
    }

    float drawSheet(Canvas canvas, Paint paint, boolean fullView, int stringCount, float yStart, boolean appendControls, int[] offsetBarBeat) {
        // float yStart = this.yStart;
        // Rect all = new Rect(0, 0, tabView.getTabViewWidth(), tabView.getTabViewHeight());
        paint.setStrokeWidth(strokeWidth);
        int a = 80;
        Color rgb = Color.valueOf(foregroundColor);
        paint.setARGB(a, (int) rgb.red(), (int) rgb.green(), (int) rgb.blue());
        paint.setTextSize(yIncrement);
        //float defWidth = paint.measureText("88")+xStart;
        float yCursor = yStart;
        float xCursor = 0;
        if (fullView) {
            /*
             * Draw Staff Lines
             */
            for (int i = 0; i < 5; i++) {
                canvas.drawLine(xStart, yCursor, tabView.getTabViewWidth() - xStart, yCursor, paint);
                yCursor += yIncrement;
            }
            int clefStart = drawClef(canvas, paint, model.getClef());
            yCursor += 3 * yIncrement;
            xCursor = 2 * xStart + clefStart + xStart;
        }
        /*
         * Draw tabulature lines
         */
        float yTabStart = yCursor;
        for (int i = 0; i < stringCount; i++) {
            canvas.drawLine(xStart, yCursor, tabView.getTabViewWidth() - xStart, yCursor, paint);
            if (i + 1 < stringCount) {
                yCursor += yIncrement;
            }
        }
        float yEnd = yCursor;
        /*
         * Draw Tab Clef
         */
        int tabClefHeight = (int) ((stringCount - 1) * yIncrement);
        int tabClefWidth = (int) (tabClefHeight / 112.3f * 27.7f);
        {
            Drawable tabClef = ViewUtils.getDrawable(context, R.drawable.tab_clef, (int) (xStart * 2),
                    (int) (yTabStart + tabClefHeight * 0.05), tabClefWidth, (int) (tabClefHeight * 0.9));
            tabClef.setTint(paint.getColor());
            tabClef.draw(canvas);
        }
        /*
         * Draw beginning bar line
         */
        canvas.drawLine(xStart, yStart, xStart, yCursor, paint);
        paint.setColor(foregroundColor);
        /*
         * Notes
         */
        xCursor = Math.max(xCursor, xStart * 2 + tabClefWidth + xStart);
        drawNotes(canvas, paint, xCursor, yStart, yEnd, appendControls, a, rgb, yTabStart, stringCount, offsetBarBeat);
        /*
         * Draw active cursor position
         */
        Color rgb2 = Color.valueOf(foregroundColorActive);

        paint.setARGB(32, (int) rgb2.red(), (int) rgb2.green(), (int) rgb2.blue());
        if (modelCursor.selectionArea.left == modelCursor.selectionArea.right) {
            PathEffect paintEffect = paint.getPathEffect();
            paint.setPathEffect(new DashPathEffect(new float[]{15f, 5f}, 0f));
            canvas.drawLine(
                    modelCursor.selectionArea.left,
                    modelCursor.selectionArea.top,
                    modelCursor.selectionArea.left,
                    modelCursor.selectionArea.bottom, paint);
            paint.setPathEffect(paintEffect);
        } else {
            //paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(modelCursor.selectionArea, paint);
            paint.setStyle(Paint.Style.FILL);
        }
        return yEnd + yIncrement * 3;
    }

    private void drawNotes(Canvas canvas, Paint paint, float xCursor, float yStart, float yEnd,
                           boolean appendControls, int a, Color rgb, float yTabStart, int stringCount, int[] offsetBarBeat) {
        int barIndex = offsetBarBeat[0];
        int beatIndex = offsetBarBeat[1];
        /*
         * Set initial cursor position
         */
        // this.modelCursor.x = xCursor;
        if (!appendControls) {
            modelCursor.setRect((int) xCursor, (int) yStart, (int) xCursor, (int) yEnd);
        }
        List<ModelCursor> cursorPositions;
        if (appendControls && Objects.nonNull(displayedCursorPositions)) {
            cursorPositions = new ArrayList<>(Arrays.asList(displayedCursorPositions));
        } else {
            cursorPositions = new ArrayList<>();
        }
        ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
        for (int i = barIndex; i < bars.size(); i++) {
            if (xCursor > tabView.getTabViewWidth() * 0.75) {
                return;
            }
            Bar bar = bars.get(i);
            if (beatIndex == 0) {
                paint.setARGB(a, (int) rgb.red(), (int) rgb.green(), (int) rgb.blue());
                // Bar Number
                canvas.drawText(String.valueOf(barIndex + 1), xCursor, (int) (yStart - yIncrement * 0.666), paint);
                paint.setColor(foregroundColor);
            }
            int maxWidth = 0;
            for (Note[] notes : bar.getNotes()) {
                for (Note n : notes) {
                    if (Objects.nonNull(n)) {
                        int string = n.getString();
                        String fret = n.getFret() > -1 ? String.valueOf(n.getFret()) : "X";
                        Rect fretRect = new Rect();
                        paint.getTextBounds(fret, 0, fret.length(), fretRect);
                        float y = yTabStart + yIncrement * (stringCount - 1 - string) + yIncrement / 3;
                        paint.setColor(backgroundColor);
                        Rect blankRect = new Rect((int) xCursor, (int) y + fretRect.top, (int) (xCursor + fretRect.right), (int) (y));
                        canvas.drawRect(blankRect, paint);
                        paint.setColor(foregroundColor);
                        canvas.drawText(fret, xCursor, y, paint);
                        maxWidth = Math.max(maxWidth, fretRect.right);
                    }
                }
                if (modelCursor.barIndex == barIndex && modelCursor.beatIndex == beatIndex) {
                    //modelCursor.xCursor = xCursor;
                    modelCursor.setRect((int) xCursor, (int) yStart, (int) (xCursor + maxWidth), (int) yEnd);
                    cursorPositions.add(modelCursor);
                } else {
                    ModelCursor newPos = new ModelCursor();
                    newPos.barIndex = barIndex;
                    newPos.beatIndex = beatIndex;
                    newPos.setRect((int) xCursor, (int) yStart, (int) (xCursor + maxWidth), (int) yEnd);
                    cursorPositions.add(newPos);
                }
                xCursor += maxWidth + xStart;
                beatIndex++;
                offsetBarBeat[1] = beatIndex;
            }
            this.displayedCursorPositions = cursorPositions.toArray(new ModelCursor[0]);
            /*
             * Draw Bar Line
             */
            if (Objects.nonNull(bar.getSeparator())) {
                paint.setARGB(a, (int) rgb.red(), (int) rgb.green(), (int) rgb.blue());
                canvas.drawLine(
                        xCursor,
                        yStart,
                        xCursor,
                        yTabStart + yIncrement * (model.getTuning().getStringCount() - 1), paint);
                paint.setColor(foregroundColor);
                xCursor += xStart;
            }
            barIndex++;
            beatIndex = 0;
            offsetBarBeat[0] = barIndex;
            offsetBarBeat[1] = 0;
        }
        if (modelCursor.barIndex == barIndex - 1 && modelCursor.beatIndex == beatIndex) {
            //this.xCursor = xCursor;
            modelCursor.setRect(xCursor, yStart, xCursor, yEnd);
        }
    }

    private int drawClef(Canvas canvas, Paint paint, TabModel.Clef clef) {
        int width;
        int x = (int) (xStart * 2);
        if (clef == TabModel.Clef.BASS) {
            width = (int) (yIncrement * (3.3 / 20 * 18));
            Drawable bassClef = ViewUtils.getDrawable(context, R.drawable.f_clef, x, (int) yStart,
                    width, (int) (yIncrement * 3.3));
            bassClef.setTint(paint.getColor());
            bassClef.draw(canvas);
        } else {
            width = (int) (yIncrement * (7 / 165.6 * 58.6));
            Drawable trebleClef = ViewUtils.getDrawable(context, R.drawable.g_clef, x,
                    (int) (yStart - yIncrement * 1.33), width, (int) (yIncrement * 7));
            trebleClef.setTint(paint.getColor());
            trebleClef.draw(canvas);
        }
        return width;
    }


    public String getSequenceKey() {
        return modelCursor.sequenceKey;
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
        modelCursor.barIndex = 0;
        modelCursor.beatIndex = 0;
    }

    public void previousBeat() {
        if (modelCursor.beatIndex > 0) {
            modelCursor.beatIndex--;
        } else {
            previousBar();
            if (model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size() > 0) {
                modelCursor.beatIndex = model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size() - 1;
            }
        }
    }

    public void previousBar() {
        if (modelCursor.barIndex > 0) {
            modelCursor.barIndex--;
            modelCursor.beatIndex = 0;
        }
    }

    public void nextBeat() {
        if (modelCursor.beatIndex == model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size() - 1
                && modelCursor.barIndex == model.getBars(modelCursor.sequenceKey).size() - 1) {
            /*
             * set new (non existent) index (end)
             */
            modelCursor.beatIndex++;
        } else if (modelCursor.beatIndex < model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size() - 1) {
            /*
             * set existent index
             */
            modelCursor.beatIndex++;
        } else {
            nextBar();
        }
    }

    public void nextBar() {
        if (modelCursor.barIndex < model.getBars(modelCursor.sequenceKey).size() - 1) {
            modelCursor.barIndex++;
            modelCursor.beatIndex = 0;
        }
    }

    public void end() {
        modelCursor.barIndex = model.getBars(modelCursor.sequenceKey).size() - 1;
        modelCursor.beatIndex = model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size();
    }

    public void newBar() {
        ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
        if (modelCursor.barIndex == bars.size() - 1
                && modelCursor.beatIndex == bars.get(modelCursor.barIndex).size() && modelCursor.beatIndex > 0) {
            bars.get(bars.size() - 1).setSeparator(Bar.SeparatorBar.NORMAL);
            model.addBar(modelCursor.sequenceKey);
            modelCursor.barIndex++;
            modelCursor.beatIndex = 0;
        }
    }

    float getXStart() {
        return xStart;
    }

    float getStrokeWidth() {
        return strokeWidth;
    }

    public int getBarIndex() {
        return modelCursor.barIndex;
    }

    public int getBeatIndex() {
        return modelCursor.beatIndex;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    void onTouch(MotionEvent event, boolean longClick) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        for (ModelCursor c : displayedCursorPositions) {
            if (c.selectionArea.contains(x, y)) {
                modelCursor = c;
                break;
            }
        }
        tabView.invalidate();
    }
}
