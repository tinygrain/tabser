package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import app.tabser.model.TabModel;

public class TabView extends View implements View.OnTouchListener {
    private int width;
    private int height;
    private TabModel model;
    private final Rect keyboardRect;
    private final Context context;
    private final TabSheet sheet;
    private final TabKeyboard keyboard;

    public TabView(Context context, AttributeSet attr) {
        super(context, attr);
        this.keyboardRect = new Rect();
        setOnTouchListener(this);
        this.context = context;
        setBackgroundColor(Color.WHITE);
        sheet = new TabSheet(this);
        keyboard = new TabKeyboard(keyboardRect, sheet);
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
            sheet.drawSheet(canvas, paint, !sheet.isCompact(), model.getTuning().getStringCount());
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
        if (keyboardRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            Toast.makeText(context, keyboard.touch(view, motionEvent), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public TabModel getModel() {
        return model;
    }
}
