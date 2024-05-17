package app.tabser.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

import app.tabser.model.Bar;
import app.tabser.model.Sequence;
import app.tabser.model.Song;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.display.DisplaySheet;
import app.tabser.view.model.display.DisplaySongRenderer;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.render.SongRendererFactory;

public class SheetView  implements View.OnScrollChangeListener, View.OnGenericMotionListener {
    public final Navigation nav = new Navigation();
    public final Settings settings = new Settings();
    private final Design design;
    private final TabView tabView;
    private Song model;
    private Context context;
    //private ModelCursor[] displayedCursorPositions;
    private ModelCursor modelCursor;

    private float deltaY;
    private float downY;
    private float initialY;

   // private int lineCount;
    // private float lineHeight;
    private float ySheetEnd;

    private boolean searchCursor;

    private Rect viewPort = new Rect();

    private DisplaySongRenderer renderer;

//    private final DisplaySheet sheet;

    //private List<RenderedLine> renderedLines = new ArrayList<>();

    SheetView(TabView tabView, Design design) {
        this.design = design;
        this.context = tabView.getContext();
        this.tabView = tabView;
        modelCursor = new ModelCursor();
        deltaY = 0;
    }

    void loadModel(Song model) {
        this.model = model;
        Sheet displaySheet = new DisplaySheet(context, viewPort, SheetMetrics.DEFAULT_METRICS);
        this.renderer = (DisplaySongRenderer) SongRendererFactory.create(model, displaySheet, design);
    }

    void drawSheet(Canvas canvas, Paint paint, boolean dragging) {
        renderer.setUp(canvas, paint);
        RenderIterator iterator = renderer.iterator();
        iterator.yPosition = deltaY;
        //iterator.cursorPositions = new ArrayList<>();
        renderer.renderDocument(iterator);
    }

    public ModelCursor getModelCursor() {
        return modelCursor;
    }

    void setViewPort(int left, int top, int right, int bottom) {
        viewPort.set(left, top, right, bottom);
    }

    String touch(MotionEvent event, boolean longClick) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        ModelCursor tmpModelCursor = null;
        String message = "No Action";

//        if (Objects.nonNull(displayedCursorPositions)) {
//            for (ModelCursor c : displayedCursorPositions) {
//                if (c.selectionArea.contains(x, y)) {
//                    tmpModelCursor = c;
//                    searchCursor = true;
//                    break;
//                }
//            }
//            if (Objects.isNull(tmpModelCursor)) {
//                if (ySheetEnd < event.getY()) {
//                    nav.end();
//                    message = "End";
//                }
//            } else {
//                modelCursor = tmpModelCursor;
//                message = "Cursor: bar(" + modelCursor.barIndex + ") / beat(" + modelCursor.beatIndex + ")";
//            }
//            tabView.invalidate();
//        }
        return message;
    }

    public void startMove() {
        this.initialY = deltaY;
    }

    public void move(Point pointDown, Point pointTo) {
        downY = pointDown.y;
        deltaY = initialY + (pointTo.y - downY);
        float yMax = 0f;
        deltaY = Math.min((int) yMax, deltaY);
//        RenderedLine l = renderedLines.get(renderedLines.size() - 1);
        int lineOffset = renderer.getYMin();
        //float yMin = ((lineCount - 1) * l..height) * -1;
        deltaY = Math.max(lineOffset, deltaY);
        tabView.invalidate();
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        Log.d("app.tabser", "onScrollChange: " + i + ", " + i1 + ", " + i2 + ", " + i3);
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {
        Log.d("app.tabser", "onGenericMotion: " + motionEvent.toString());
        return false;
    }

    public SheetMetrics getMetrics() {
        return renderer.getSheet().getMetrics();
    }

    public enum Mode {
        EDIT, VIEW;
    }

    public final class ModelCursor {
        public String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;
        public int barIndex;
        public int beatIndex;
        public Rect selectionArea;
        //private Rect responseArea;
        public int lineIndex;

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

    final class Navigation implements ValueAnimator.AnimatorUpdateListener {
        float animationStart;
        ValueAnimator animator;

        @Override
        public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
            int value = (int) valueAnimator.getAnimatedValue();
            deltaY = animationStart + value;
            tabView.invalidate();
        }

        void startAnimation(int yDelta1, int yDelta2, long animationTime) {
            animationStart = deltaY;
            int animationGoal = yDelta2 - yDelta1;
            animator = ValueAnimator.ofInt(0, animationGoal);
            //animator.setDuration(design.getAnimationDuration() * Math.abs((long) (animationGoal / lineHeight)));
            animator.setDuration(animationTime);
            //animator.setDuration(design.getAnimationDuration());
            animator.addUpdateListener(this);
            animator.start();
        }

        void scrollToLine(int lineIndex, long animationTime) {
            float deltaY1 = deltaY;
            float deltaY2 = -(renderer.getHeaderHeight() + renderer.getLine(lineIndex).metrics.yOffset);
            startAnimation((int) deltaY1, (int) deltaY2, animationTime);
        }

        public void start() {
            modelCursor.barIndex = 0;
            modelCursor.beatIndex = 0;
            searchCursor = true;
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
                searchCursor = true;
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
                searchCursor = true;
            }
        }

        public void end() {
            if (model.getBars(modelCursor.sequenceKey).size() > 0) {
                modelCursor.barIndex = model.getBars(modelCursor.sequenceKey).size() - 1;
                modelCursor.beatIndex = model.getBars(modelCursor.sequenceKey).get(modelCursor.barIndex).size();
                searchCursor = true;
            }
        }

        public void newBar() {
            /*
             * TODO dont add too many bars
             */
            ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
            if (modelCursor.barIndex == bars.size() - 1
                    && modelCursor.beatIndex == bars.get(modelCursor.barIndex).size() && modelCursor.beatIndex > 0) {
                bars.get(bars.size() - 1).setSeparator(Bar.SeparatorBar.NORMAL);
                model.addBar(modelCursor.sequenceKey);
                modelCursor.barIndex++;
                modelCursor.beatIndex = 0;
                searchCursor = true;
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
            compact = preferences.getBoolean("compact", false);
            autoBar = preferences.getBoolean("auto-bar", false);
            autoNext = preferences.getBoolean("auto-next", false);
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
}
