package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.tabser.model.TabModel;

public class TabView extends View implements View.OnTouchListener, View.OnLongClickListener {
    private int width;
    private int height;
    private TabModel model;
    private final Rect keyboardRect;
    private final Context context;
    private final TabSheet sheet;
    private final TabKeyboard keyboard;
    private boolean longClick;

    private final GestureDetector gestureDetector;
    private final Handler handler = new Handler();
    Runnable touchScheduler = new Runnable() {
        public void run() {
            longClick = true;
        }
    };


    public TabView(Context context, AttributeSet attr) {
        super(context, attr);
        this.keyboardRect = new Rect();
        setOnTouchListener(this);
        setOnLongClickListener(this);
        this.context = context;
        setBackgroundColor(Color.WHITE);
        Design design = Design.PAPER_MODE;
        sheet = new TabSheet(this, design);
        keyboard = new TabKeyboard(keyboardRect, sheet, context, design);
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
    }

    public void loadModel(TabModel model) {
        this.model = model;
        keyboard.loadModel(model);
        sheet.loadModel(model);
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
            float y = 120;
            int[] offsetBarBeat = {0, 0};
            y = sheet.drawSheet(canvas, paint, !sheet.isCompact(), model.getTuning().getStringCount(),
                    y, false, offsetBarBeat);

            y = sheet.drawSheet(canvas, paint, !sheet.isCompact(), model.getTuning().getStringCount(),
                    y, true, offsetBarBeat);
            y = sheet.drawSheet(canvas, paint, !sheet.isCompact(), model.getTuning().getStringCount(),
                    y, true, offsetBarBeat);
            keyboard.drawControls(canvas, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        int left = (int) 0;
        int top = height / 3 * 2;
        int right = (int) (width);
        int bottom = (int) (height);
        keyboardRect.set(left, top, right, bottom);
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            handler.postDelayed(touchScheduler, ViewConfiguration.getLongPressTimeout());
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (keyboardRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                //keyboard.touch(view, motionEvent, longClick);
                Toast.makeText(context, keyboard.touch(view, motionEvent, longClick) + (longClick ? " -L" : ""), Toast.LENGTH_LONG).show();
            } else {
                sheet.onTouch(motionEvent, longClick);
            }
            if (longClick) {
                // Log.d("TOUCH", "LONG");
                longClick = false;
            } else {
                // Log.d("TOUCH", "SHORT");
            }
        }
        if ((motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                || (motionEvent.getAction() == MotionEvent.ACTION_UP)) {
            handler.removeCallbacks(touchScheduler);
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
