package app.tabser.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Objects;

import app.tabser.dom.Bar;
import app.tabser.dom.Song;
import app.tabser.rendering.RenderModel;
import app.tabser.rendering.RenderOptions;
import app.tabser.rendering.SheetCursor;
import app.tabser.rendering.Theme;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;
import app.tabser.view.render.display.DisplaySheet;
import app.tabser.view.render.display.DisplaySongRenderer;

public class SheetView extends View implements View.OnScrollChangeListener, View.OnGenericMotionListener {
    private int width;
    private int height;
    public final Navigation nav = new Navigation();
    public final Settings settings = new Settings();
    private final Theme theme;
    private Song model;
    private final Context context;
    //private ModelCursor[] displayedCursorPositions;
    private SheetCursor sheetCursor;
    private boolean searchCursor;
    public final Rect keyboardRect = new Rect();
    public final Rect playerRect = new Rect();
    private DisplaySongRenderer renderer;
    private final RenderOptions options;
    public final SheetController controller;
    private MenuAnimator menuAnimator;
    private Rectangle viewPort = new Rectangle();

//    private final DisplaySheet sheet;

        //private List<RenderedLine> renderedLines = new ArrayList<>();
//    Theme theme, SharedPreferences preferences,
//    SheetController sheetController
    public SheetView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.theme = Theme.PAPER_MODE;
        this.controller = new SheetController(context, this, theme);
//        settings.setUp(preferences);
        sheetCursor = new SheetCursor();
//        viewPort = new ViewPort();
//        viewPort.area = viewPort;
        this.options = RenderOptions.builder()
                .setViewPort(viewPort)
                .setForm(RenderOptions.Form.FULL)
                .setFormat(RenderOptions.SheetFormat.Element.NOTES,
                        RenderOptions.SheetFormat.Element.SONG_TEXT,
                        RenderOptions.SheetFormat.Element.TAB)
                .setSheetMetrics(SheetMetrics.getDefaultMetrics(viewPort))
                .setCursor(sheetCursor)
                .setBuild(RenderOptions.Build.VERTICAL)
                .setCache(false)
                .setSongPages(RenderOptions.SongPages.SINGLE)
                .setCompilation(RenderOptions.Compilation.SEQUENCE)
                .build();
        setOnTouchListener(controller);
        setOnLongClickListener(controller);
        setBackgroundColor(Color.YELLOW);
//        setOnGenericMotionListener(this);
        setOnScrollChangeListener(this);
    }

    public void setMenuAnimator(MenuAnimator menuAnimator) {
        this.menuAnimator = menuAnimator;
    }

    public Theme getTheme() {
        return theme;
    }

    public void loadModel(Song song) {
        this.model = song;
        DisplaySheet displaySheet = new DisplaySheet(options.sheetMetrics);
        RenderModel renderModel = new RenderModel(song, displaySheet, theme, options);
        this.renderer = new DisplaySongRenderer(renderModel, displaySheet, getContext());
//        this.renderer = (DisplaySongRenderer) SongRendererFactory.create(model, displaySheet, theme, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        if (Objects.nonNull(model)) {
            renderer.setUp(canvas, paint);
            Rectangle dims = renderer.renderDocument(options);
//            setMeasuredDimension((int) dims.right, (int) dims.bottom);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        int left = 0;
        int top = height / 3 * 2;
        int right = width;
        int bottom = height;
        setViewPort(0, 0, w, h);
        keyboardRect.set(left, top, right, bottom);
        playerRect.set(0, height - height / 14, width, height);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec+2000);
    }

    public SheetCursor getSheetCursor() {
        return sheetCursor;
    }

    void setViewPort(float left, float top, float right, float bottom) {
        viewPort.set(left, top, right, bottom);
    }


//    public void startMove() {
//        this.initialY = viewPort.deltaY;
//    }

//    public void moveVertical(Point pointDown, Point pointTo) {
//        downY = pointDown.y;
//        viewPort.deltaY = initialY + (pointTo.y - downY);
//        float yMax = 0f;
//        viewPort.deltaY = Math.min((int) yMax, viewPort.deltaY);
//////        RenderedLine l = renderedLines.get(renderedLines.size() - 1);
//        float lineOffset = renderer.getYMin();
////        //float yMin = ((lineCount - 1) * l..height) * -1;
//        viewPort.deltaY = Math.max(lineOffset, viewPort.deltaY);
//        invalidate();
//    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        Log.d("app.tabser", "onScrollChange: " + i + ", " + i1 + ", " + i2 + ", " + i3);
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {
        Log.d("app.tabser", "onGenericMotion: " + motionEvent.toString());
//        return super.onGenericMotionEvent(motionEvent);
        return false;
    }

    public SheetMetrics getMetrics() {
        return renderer.getSheet().getMetrics();
    }

    public enum Mode {
        EDIT, VIEW;
    }

    public final class Settings {
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

    public final class Navigation { //implements ValueAnimator.AnimatorUpdateListener {
        float animationStart;
        ValueAnimator animator;

//        @Override
//        public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
//            int value = (int) valueAnimator.getAnimatedValue();
//            viewPort.deltaY = animationStart + value;
//            invalidate();
//        }

//        void startAnimation(int yDelta1, int yDelta2, long animationTime) {
//            animationStart = viewPort.deltaY;
//            int animationGoal = yDelta2 - yDelta1;
//            animator = ValueAnimator.ofInt(0, animationGoal);
//            //animator.setDuration(design.getAnimationDuration() * Math.abs((long) (animationGoal / lineHeight)));
//            animator.setDuration(animationTime);
//            //animator.setDuration(design.getAnimationDuration());
//            animator.addUpdateListener(this);
//            animator.start();
//        }

//        void scrollToLine(int lineIndex, long animationTime) {
//            float deltaY1 = viewPort.deltaY;
//            float deltaY2 = -(renderer.getBlockOffsetY(lineIndex));
//            startAnimation((int) deltaY1, (int) deltaY2, animationTime);
//        }

        public void start() {
            sheetCursor.barIndex = 0;
            sheetCursor.beatIndex = 0;
            searchCursor = true;
        }

        public void previousBeat() {
            if (sheetCursor.beatIndex > 0) {
                sheetCursor.beatIndex--;
            } else if (sheetCursor.barIndex > 0) {
                previousBar();
                if (model.getBars(sheetCursor.sequenceKey).get(sheetCursor.barIndex).size() > 0) {
                    sheetCursor.beatIndex = model.getBars(sheetCursor.sequenceKey).get(sheetCursor.barIndex).size() - 1;
                }
            }
        }

        public void previousBar() {
            if (sheetCursor.barIndex > 0) {
                sheetCursor.barIndex--;
                sheetCursor.beatIndex = 0;
                searchCursor = true;
            }
        }

        public void nextBeat() {
            List<Bar> bars = model.getBars(sheetCursor.sequenceKey);
            if (bars.size() > 0) {
                if (sheetCursor.beatIndex == bars.get(sheetCursor.barIndex).size() - 1
                        && sheetCursor.barIndex == bars.size() - 1) {
                    /*
                     * set new (non existent) index (end)
                     */
                    sheetCursor.beatIndex++;
                } else if (sheetCursor.beatIndex < bars.get(sheetCursor.barIndex).size() - 1) {
                    /*
                     * set existent index
                     */
                    sheetCursor.beatIndex++;
                } else {
                    nextBar();
                }
            }
        }

        public void nextBar() {
            if (sheetCursor.barIndex < model.getBars(sheetCursor.sequenceKey).size() - 1) {
                sheetCursor.barIndex++;
                sheetCursor.beatIndex = 0;
                searchCursor = true;
            }
        }

        public void end() {
            if (model.getBars(sheetCursor.sequenceKey).size() > 0) {
                sheetCursor.barIndex = model.getBars(sheetCursor.sequenceKey).size() - 1;
                sheetCursor.beatIndex = model.getBars(sheetCursor.sequenceKey).get(sheetCursor.barIndex).size();
                searchCursor = true;
            }
        }

        public void newBar() {
            /*
             * TODO dont add too many bars
             */
            List<Bar> bars = model.getBars(sheetCursor.sequenceKey);
            if (sheetCursor.barIndex == bars.size() - 1
                    && sheetCursor.beatIndex == bars.get(sheetCursor.barIndex).size() && sheetCursor.beatIndex > 0) {
                bars.get(bars.size() - 1).setSeparator(Bar.SeparatorBar.NORMAL);
                model.addBar(sheetCursor.sequenceKey);
                sheetCursor.barIndex++;
                sheetCursor.beatIndex = 0;
                searchCursor = true;
            }
        }
    }
}
