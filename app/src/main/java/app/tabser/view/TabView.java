package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import java.util.Objects;

import app.tabser.model.TabModel;

public class TabView extends View implements View.OnTouchListener, View.OnLongClickListener {
    private final TabViewControls viewControls;
    private int width;
    private int height;
    private TabModel model;
    private final Rect keyboardRect;
    private final Context context;
    private final TabSheet sheet;
    private final TabKeyboard keyboard;
    private boolean longClick;
    //private final GestureDetector gestureDetector;
    private final Handler handler = new Handler();
    Runnable touchScheduler = new Runnable() {
        public void run() {
            longClick = true;
        }
    };
    private final Design design;

    public TabView(Context context, AttributeSet attr) {
        super(context, attr);
        this.keyboardRect = new Rect();
        setOnTouchListener(this);
        setOnLongClickListener(this);
        this.context = context;
        setBackgroundColor(Color.WHITE);
        design = Design.PAPER_MODE;
        sheet = new TabSheet(this, design);
        keyboard = new TabKeyboard(keyboardRect, sheet, context, design);
        viewControls = new TabViewControls(sheet, context, design, this);
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

    public void loadModel(TabModel model) {
        this.model = model;
        keyboard.loadModel(model);
        sheet.loadModel(model);
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
            sheet.drawSheet(canvas, paint);
            if (sheet.settings.getMode() == TabSheet.Mode.EDIT) {
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
        keyboardRect.set(left, top, right, bottom);
        invalidate();
    }

    private Point pointDown = new Point();

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            pointDown.set((int) motionEvent.getX(), (int) motionEvent.getY());
            handler.postDelayed(touchScheduler, ViewConfiguration.getLongPressTimeout());
            sheet.startMove();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (sheet.settings.getMode() == TabSheet.Mode.EDIT
                    && keyboardRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                keyboard.touch(view, motionEvent, longClick);
                // Toast.makeText(context, keyboard.touch(view, motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
            } else if (!viewControls.touch(motionEvent, longClick)) {
                sheet.onTouch(motionEvent, longClick);
            }
            if (longClick) {
                longClick = false;
            }
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                || (motionEvent.getAction() == MotionEvent.ACTION_UP)) {
            handler.removeCallbacks(touchScheduler);
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)) {
            sheet.move(pointDown, new Point((int) motionEvent.getX(), (int) motionEvent.getY()));
        }
        return false;
    }

    public TabModel getModel() {
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
