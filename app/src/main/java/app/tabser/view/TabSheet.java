package app.tabser.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Objects;

import app.tabser.R;
import app.tabser.model.Bar;
import app.tabser.model.Note;
import app.tabser.model.Sequence;
import app.tabser.model.TabModel;

class TabSheet {

    private final class RenderResult {
        public ArrayList<ModelCursor> cursorPositions;
        int barOffset;
        int beatOffset;
        int lineOffset;
        private float yPosition;
        private boolean endReached;
        private int calculatedBarCount;

        float calculateLine(float xStart, RenderResult renderResult) {
            float fullWidth = tabView.getTabViewWidth() - design.getxStart() - xStart;
            float defaultWidth = design.getYIncrement() * 2;
            float calculatedWidth = 0f;
            int xElements = 0;
            calculatedBarCount = 0;
            ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
            boolean eol = false;
            for (int barIndex = renderResult.barOffset; barIndex < bars.size(); barIndex++) {
                Bar bar = bars.get(barIndex);
                ArrayList<Note[]> notes = bar.getNotes();
                float barWidth = notes.size() * defaultWidth;
                if (barWidth + calculatedWidth > fullWidth) {
                    eol = true;
                    break;
                } else {
                    calculatedBarCount++;
                    calculatedWidth += barWidth;
                    xElements += notes.size();
                }
            }
            if (eol) {
                return fullWidth / xElements;
            } else {
                endReached = true;
                return defaultWidth;
            }
        }
    }

    public enum Mode {
        EDIT, VIEW;
    }

    final class ModelCursor {
        String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;
        int barIndex;
        int beatIndex;
        private Rect selectionArea;
        //private Rect responseArea;
        private int lineIndex;

        private void setRect(float left, float top, float right, float bottom) {
            setRect((int) left, (int) top, (int) right, (int) bottom);
        }

        private void setRect(int left, int top, int right, int bottom) {
            selectionArea = new Rect(left, top, right, bottom);
        }

        public boolean isTrailing() {
            ArrayList<Bar> bars = model.getBars(sequenceKey);
            int lastBarSize = bars.size() > 0 ? bars.get(bars.size() - 1).size() : 0;
            return barIndex >= bars.size() - 1 && beatIndex == lastBarSize;
        }
    }

    final class Navigation {
        void scrollToLine(int lineIndex, long animationTime) {
            float deltaY1 = deltaY;
            float deltaY2 = -(headerHeight + lineIndex * lineHeight);
            long frameLength = 50L;
            float distance = deltaY2 - deltaY1;
            float increment = distance / animationTime * frameLength;
            boolean down = deltaY2 > deltaY1;
            int nSteps = (int) (animationTime / frameLength);
            float[] progress = {0f};
            new Thread(() -> {
                for (long i = 0; i < nSteps; i++) {
                    progress[0] = i * increment;
                    deltaY = (int) (deltaY1 + progress[0]);
                    tabView.invalidate();
                    try {
                        Thread.sleep(frameLength);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                deltaY = (int) deltaY2;
                tabView.invalidate();
            }).start();
        }

        public void start() {
            modelCursor.barIndex = 0;
            modelCursor.beatIndex = 0;
            //scrollToLine(0, design.getAnimationDuration());
        }

        public void previousBeat() {
            if (modelCursor.beatIndex > 0) {
                modelCursor.beatIndex--;
            } else if (modelCursor.barIndex > 0) {
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
            ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
            if (bars.size() > 0) {
                if (modelCursor.beatIndex == bars.get(modelCursor.barIndex).size() - 1
                        && modelCursor.barIndex == bars.size() - 1) {
                    /*
                     * set new (non existent) index (end)
                     */
                    modelCursor.beatIndex++;
                } else if (modelCursor.beatIndex < bars.get(modelCursor.barIndex).size() - 1) {
                    /*
                     * set existent index
                     */
                    modelCursor.beatIndex++;
                } else {
                    nextBar();
                }
            }
        }

        public void nextBar() {
            if (modelCursor.barIndex < model.getBars(modelCursor.sequenceKey).size() - 1) {
                modelCursor.barIndex++;
                modelCursor.beatIndex = 0;
            }
        }

        public void end() {
            if (model.getBars(modelCursor.sequenceKey).size() > 0) {
                modelCursor.barIndex = model.getBars(modelCursor.sequenceKey).size() - 1;
                modelCursor.beatIndex = model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size();
                //scrollToLine(lineCount - 1, design.getAnimationDuration());
            }
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
    }

    final class Settings {
        private boolean insert;
        private boolean compact;
        private boolean autoNext;
        private boolean autoBar;
        private Mode mode;

        public void setUp(SharedPreferences preferences) {
            insert = preferences.getBoolean("insert", false);
            compact = preferences.getBoolean("compact", true);
            autoBar = preferences.getBoolean("auto-bar", true);
            autoNext = preferences.getBoolean("auto-next", true);
        }

        public boolean isInsert() {
            return insert;
        }

        public void setInsert(boolean insert) {
            this.insert = insert;
        }

        public boolean toggleInsert() {
            return (this.insert = !this.insert);
        }

        public boolean isCompact() {
            return compact;
        }

        public boolean isAutoBar() {
            return autoBar;
        }

        public boolean isAutoNext() {
            return autoNext;
        }

        public boolean toggleCompact() {
            return (this.compact = !this.compact);
        }

        public boolean toggleAutoNext() {
            return (this.autoNext = !this.autoNext);
        }

        public boolean toggleAutoBar() {
            return (this.autoBar = !this.autoBar);
        }

        public Mode getMode() {
            return mode;
        }

        public void setMode(Mode mode) {
            this.mode = mode;
        }
    }

    final Navigation nav = new Navigation();
    final Settings settings = new Settings();
    private final Design design;
    private final TabView tabView;
    private TabModel model;
    private Context context;
    private ModelCursor[] displayedCursorPositions;
    private ModelCursor modelCursor;

    private int deltaY;
    private int downY;
    private int initialY;

    private int lineCount;
    private float lineHeight;

    private float headerHeight;

    private Rect viewPort = new Rect();

    TabSheet(TabView tabView, Design design) {
        this.design = design;
        this.context = tabView.getContext();
        this.tabView = tabView;
        //this.backgroundColor = design.getBackgroundColorSheet();
        // this.foregroundColorActive = design.getForegroundColorActiveSheet();
        //this.foregroundColor = design.getForegroundColorInactiveSheet();
        modelCursor = new ModelCursor();
        deltaY = 0;
    }

    void loadModel(TabModel model) {
        this.model = model;
    }

    void drawSheet(Canvas canvas, Paint paint) {
        RenderResult renderResult = new RenderResult();
        renderResult.yPosition = deltaY;
        renderResult.lineOffset =0;
        renderResult.cursorPositions = new ArrayList<>();
        lineCount = 0;
        lineHeight = 0;
        while (!renderResult.endReached) {
            drawLine(canvas, paint, renderResult);
            lineCount++;
        }
        displayedCursorPositions = renderResult.cursorPositions.toArray(new ModelCursor[0]);
        /*
         * Draw active cursor position
         */
        int foregroundColorActive = design.getForegroundColorActiveSheet();
        Color rgbForegroundActive = Color.valueOf(foregroundColorActive);
        paint.setARGB(32, (int) rgbForegroundActive.red(),
                (int) rgbForegroundActive.green(), (int) rgbForegroundActive.blue());
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
            //paint.setStyle(Paint.Style.FILL);
        }
        if (!viewPort.contains(modelCursor.selectionArea)) {
            nav.scrollToLine(modelCursor.lineIndex, design.getAnimationDuration());
        }
    }

    private void drawLine(Canvas canvas, Paint paint, RenderResult renderResult) {
        // float yStart = this.yStart;
        // Rect all = new Rect(0, 0, tabView.getTabViewWidth(), tabView.getTabViewHeight());
        int stringCount = model.getTuning().getStringCount();
        paint.setStrokeWidth(design.getStrokeWidth());
        int a = 80;
        int foregroundColor = design.getForegroundColorInactiveSheet();
        Color rgbForeground = Color.valueOf(foregroundColor);
        paint.setARGB(a, (int) rgbForeground.red(), (int) rgbForeground.green(), (int) rgbForeground.blue());
        float yIncrement = design.getYIncrement();
        float xStart = design.getxStart();
        paint.setTextSize(yIncrement);
        //float defWidth = paint.measureText("88")+xStart;
        float yCursor = renderResult.yPosition + (design.getYIncrement() * 3);
        float yStart = yCursor;
        float xCursor = 0;
        if (!settings.isCompact()) {
            /*
             * Draw Staff Lines
             */
            for (int i = 0; i < 5; i++) {
                canvas.drawLine(xStart, yCursor, tabView.getTabViewWidth() - xStart, yCursor, paint);
                yCursor += yIncrement;
            }
            int clefStart = drawClef(canvas, paint, model.getClef(), yStart);
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
        float tabClefWidth = drawClef(canvas, paint, TabModel.Clef.TAB, yTabStart, stringCount);
        /*
         * Draw beginning bar line
         */
        canvas.drawLine(xStart, yStart, xStart, yEnd, paint);
        paint.setColor(foregroundColor);
        /*
         * Notes
         */
        xCursor = Math.max(xCursor, xStart * 2 + tabClefWidth + xStart);
        if (renderResult.barOffset == 0) {
            lineHeight = yEnd - renderResult.yPosition;
        }
        drawNotes(canvas, paint, xCursor, yEnd, a, rgbForeground, yTabStart, renderResult);
        renderResult.yPosition = yEnd;
        renderResult.lineOffset++;
    }

    private void drawNotes(Canvas canvas, Paint paint, float xCursor, float yEnd, int a,
                           Color rgbForeground, float yTabStart, RenderResult renderResult) {
        //float yStart = renderResult.yPosition;
        float yStart = renderResult.yPosition + (design.getYIncrement() * 3);
        int barIndex = renderResult.barOffset;
        int beatIndex = renderResult.beatOffset;
        int stringCount = model.getTuning().getStringCount();
        /*
         * Set initial cursor position
         */
        // this.modelCursor.x = xCursor;
        if (model.getBars(modelCursor.sequenceKey).size() == 0) {
            modelCursor.setRect((int) xCursor, (int) yStart, (int) xCursor, (int) yEnd);
        }
        ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
        float renderWidth = renderResult.calculateLine(xCursor, renderResult);
        //float defaultWidth = design.getYIncrement() * 1.5f;
        int calculatedBars = renderResult.calculatedBarCount;
        /*
         * the max index + 1 for bars in sequence for this line
         */
        int lineBars = barIndex + calculatedBars;
        for (int i = barIndex; i < lineBars && i < bars.size(); i++) {
            Bar bar = bars.get(i);
            int foregroundColor = design.getForegroundColorInactiveSheet();
            float yIncrement = design.getYIncrement();
            int backgroundColor = design.getBackgroundColorSheet();
            if (beatIndex == 0) {
                paint.setARGB(a, (int) rgbForeground.red(), (int) rgbForeground.green(), (int) rgbForeground.blue());
                // Bar Number
                canvas.drawText(String.valueOf(barIndex + 1), xCursor, (int) (yStart - yIncrement * 0.666), paint);
                paint.setColor(foregroundColor);
            }
            /*
             * loop through notes (per string) of this beat
             */
            for (Note[] notes : bar.getNotes()) {
                for (Note n : notes) {
                    if (Objects.nonNull(n)) {
                        int string = n.getString();
                        String fret = n.getFret() > -1 ? String.valueOf(n.getFret()) : "X";
                        Rect fretRect = new Rect();
                        paint.getTextBounds(fret, 0, fret.length(), fretRect);
                        float y = yTabStart + yIncrement * (stringCount - 1 - string) + yIncrement / 3;
                        paint.setColor(backgroundColor);
                        float textX = xCursor + renderWidth / 2 - fretRect.right / 2f;
                        Rect blankRect = new Rect((int) textX, (int) y + fretRect.top, (int) (textX + fretRect.right), (int) (y));
                        canvas.drawRect(blankRect, paint);
                        paint.setColor(foregroundColor);
                        canvas.drawText(fret, textX, y, paint);
                    }
                }
                if (modelCursor.barIndex == barIndex && modelCursor.beatIndex == beatIndex) {
                    modelCursor.setRect((int) xCursor, (int) yStart, (int) (xCursor + renderWidth), (int) yEnd);
                    renderResult.cursorPositions.add(modelCursor);
                    modelCursor.lineIndex = renderResult.lineOffset;
                } else {
                    ModelCursor newPos = new ModelCursor();
                    newPos.barIndex = barIndex;
                    newPos.beatIndex = beatIndex;
                    newPos.setRect((int) xCursor, (int) yStart, (int) (xCursor + renderWidth), (int) yEnd);
                    newPos.lineIndex = renderResult.lineOffset;
                    renderResult.cursorPositions.add(newPos);
                }
                //float actualWidth = Math.max(maxWidth, defaultWidth);
                xCursor += renderWidth;
                beatIndex++;
                renderResult.beatOffset = beatIndex;
            }
            /*
             * Draw Bar Line
             */
            if (Objects.nonNull(bar.getSeparator())) {
                paint.setARGB(a, (int) rgbForeground.red(), (int) rgbForeground.green(), (int) rgbForeground.blue());
                canvas.drawLine(
                        xCursor,
                        yStart,
                        xCursor,
                        yTabStart + yIncrement * (model.getTuning().getStringCount() - 1), paint);
                paint.setColor(foregroundColor);
            }
            barIndex++;
            beatIndex = 0;
            renderResult.barOffset = barIndex;
            renderResult.beatOffset = 0;
        }
        /*
         * set trailing cursor
         */
        if (modelCursor.isTrailing()) {
            //this.xCursor = xCursor;
            modelCursor.lineIndex = renderResult.lineOffset;
            modelCursor.setRect(xCursor + design.getxStart(), yStart, xCursor + design.getxStart(), yEnd);
        }
    }

    private int drawClef(Canvas canvas, Paint paint, TabModel.Clef clef, float yStart) {
        return drawClef(canvas, paint, clef, yStart, -1);
    }

    private int drawClef(Canvas canvas, Paint paint, TabModel.Clef clef, float yStart, int stringCount) {
        int width;
        float xStart = design.getxStart();
        int x = (int) (xStart * 2);
        float yIncrement = design.getYIncrement();
        if (clef == TabModel.Clef.BASS) {
            width = (int) (yIncrement * (3.3 / 20 * 18));
            Drawable bassClef = ViewUtils.getDrawable(context, R.drawable.f_clef, x, (int) yStart,
                    width, (int) (yIncrement * 3.3));
            bassClef.setTint(paint.getColor());
            bassClef.draw(canvas);
        } else if (clef == TabModel.Clef.TREBLE) {
            width = (int) (yIncrement * (7 / 165.6 * 58.6));
            Drawable trebleClef = ViewUtils.getDrawable(context, R.drawable.g_clef, x,
                    (int) (yStart - yIncrement * 1.33), width, (int) (yIncrement * 7));
            trebleClef.setTint(paint.getColor());
            trebleClef.draw(canvas);
        } else {
            int tabClefHeight = (int) ((stringCount - 1) * yIncrement);
            int tabClefWidth = (int) (tabClefHeight / 112.3f * 27.7f);
            Drawable tabClef = ViewUtils.getDrawable(context, R.drawable.tab_clef, (int) (xStart * 2),
                    (int) (yStart + tabClefHeight * 0.05), tabClefWidth, (int) (tabClefHeight * 0.9));
            tabClef.setTint(paint.getColor());
            tabClef.draw(canvas);
            width = tabClefWidth;
        }
        return width;
    }

    public ModelCursor getModelCursor() {
        return modelCursor;
    }

    void setViewPort(int left, int top, int right, int bottom){
        viewPort.set(left, top, right, bottom);
    }
    void onTouch(MotionEvent event, boolean longClick) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        ModelCursor tmpModelCursor = null;
        if (Objects.nonNull(displayedCursorPositions)) {
            for (ModelCursor c : displayedCursorPositions) {
                if (c.selectionArea.contains(x, y)) {
                    tmpModelCursor = c;
                    break;
                }
            }
            if (Objects.isNull(tmpModelCursor)) {
                nav.end();
            } else {
                modelCursor = tmpModelCursor;
            }
            tabView.invalidate();
        }
    }

    public void startMove() {
        this.initialY = deltaY;
    }

    public void move(Point pointDown, Point pointTo) {
        downY = pointDown.y;
        deltaY = initialY + (pointTo.y - downY);
        float yMax = 0f;
        deltaY = Math.min((int) yMax, deltaY);
        /*
         * TODO Debug yMin
         */
        float yMin = ((lineCount - 1) * lineHeight) * -1;
        deltaY = Math.max((int) yMin, deltaY);
        tabView.invalidate();
    }
}
