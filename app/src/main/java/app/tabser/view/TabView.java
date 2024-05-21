package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Objects;

import app.tabser.model.Song;
import app.tabser.view.input.TabKeyboard;
import app.tabser.view.input.TabViewControls;
import app.tabser.view.model.definition.Design;

public class TabView extends View implements View.OnTouchListener, View.OnLongClickListener {
    private int width;
    private int height;
    private Song model;
    private final Rect keyboardRect;
    private final Rect playerRect;
    private final Context context;
    private final SongView sheetView;
    private final TabKeyboard keyboard;
    private final TabViewControls viewControls;
    private boolean longClick;
    //private final GestureDetector gestureDetector;
    private final Handler handler = new Handler();
    Runnable touchScheduler = new Runnable() {
        public void run() {
            longClick = true;
        }
    };
    private final Design design;
    private boolean dragging;

    public TabView(Context context, AttributeSet attr) {
        super(context, attr);
        this.keyboardRect = new Rect();
        this.playerRect = new Rect();
        setOnTouchListener(this);
        setOnLongClickListener(this);
        this.context = context;
        setBackgroundColor(Color.WHITE);
        design = Design.PAPER_MODE;
        sheetView = new SongView(this, design);
        keyboard = new TabKeyboard(keyboardRect, sheetView, context, design);
        viewControls = new TabViewControls(sheetView, context, design, this);
        setOnScrollChangeListener(sheetView);
        setOnGenericMotionListener(sheetView);

        //setKeepScreenOn(true);
        /*
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(@NonNull MotionEvent e) {
                Log.d("GestureListener", e.toString());
            }

            @Override
            public boolean onContextClick(@NonNull MotionEvent e) {
                Log.d("GestureListener", e.toString());
                return false;
            }
        });
         */
    }

    public void loadModel(String mode, Song model) {
        this.model = model;
        keyboard.loadModel(model);
        sheetView.loadModel(model);
        sheetView.settings.setMode(SongView.Mode.valueOf(mode));
        viewControls.loadModel(model);
        invalidate();
    }

    int getTabViewWidth() {
        return width;
    }

    int getTabViewHeight() {
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if (Objects.nonNull(model)) {
            sheetView.drawSheet(canvas, paint, dragging);
            if (sheetView.settings.getMode() == SongView.Mode.EDIT) {
                keyboard.drawControls(canvas, paint);
            } else {
                viewControls.drawMenu(canvas, paint);
            }
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
        sheetView.setViewPort(0, 0, w, h);
        keyboardRect.set(left, top, right, bottom);
        playerRect.set(0, height - height / 14, width, height);
        invalidate();
    }

    private Point pointDown = new Point();

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        String message = "";
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            pointDown.set((int) motionEvent.getX(), (int) motionEvent.getY());
            handler.postDelayed(touchScheduler, ViewConfiguration.getLongPressTimeout());
            sheetView.startMove();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (dragging) {
                dragging = false;
                float deltaDragX = pointDown.x - motionEvent.getX();
                float deltaDragY = pointDown.y - motionEvent.getY();
                float tolerance = 15.0f;
                if (Math.abs(deltaDragX) > tolerance || Math.abs(deltaDragY) > tolerance) {
                    //Log.d("app.tabser", "onTouch: DRAG: dX=" + deltaDragX + " & dY=" + deltaDragY);
                    log(message);
                    return false;
                } else {
                    //Log.d("app.tabser", "onTouch: NO DRAG: dX=" + deltaDragX + " & dY=" + deltaDragY);
                }
            }
            if (sheetView.settings.getMode() == SongView.Mode.EDIT
                    && keyboardRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                message = keyboard.touch(view, motionEvent, longClick);
                //Toast.makeText(context, keyboard.touch(view, motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
            } else if (sheetView.settings.getMode() == SongView.Mode.VIEW
                    && playerRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                message = viewControls.touch(motionEvent, longClick);
                //Toast.makeText(context, viewControls.touch(motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
            } else {
                message = sheetView.touch(motionEvent, longClick);
                //Toast.makeText(context, sheet.onTouch(motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
            }
            if (longClick) {
                longClick = false;
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !dragging) {
            dragging = true;
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                || (motionEvent.getAction() == MotionEvent.ACTION_UP)) {
            handler.removeCallbacks(touchScheduler);
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)) {
            sheetView.moveVertical(pointDown, new Point((int) motionEvent.getX(), (int) motionEvent.getY()));
        }
        log(message);
        return false;
    }

    private void log(String message) {
        if (Objects.nonNull(message) && message.length() > 0) {
            Log.d("app.tabser", message);
        }
    }

    public Song getModel() {
        return model;
    }

    public boolean isInSubMenu() {
        return keyboard.isInSubMenu();
    }

    public void showKeyboardMainMenu() {
        keyboard.showMainMenu(this);
    }

    @Override
    public boolean onLongClick(View view) {
        this.longClick = true;
        return false;
    }
}
