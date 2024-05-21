package app.tabser.view.render.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import app.tabser.view.ViewUtils;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.AbstractSheet;

public class DisplaySheet extends AbstractSheet implements Sheet {
    private Canvas canvas;
    private Paint paint;

    private float defaultTextSize;
    private int defaultColor;
    private PathEffect defaultPathEffect;
    private Paint.Style defaultStyle;


    public DisplaySheet(Context context, SheetMetrics sheetMetrics) {
        super(context, sheetMetrics);
    }

    public void setUp(Canvas canvas, Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        defaultColor = paint.getColor();
        defaultTextSize = paint.getTextSize();
        defaultPathEffect = paint.getPathEffect();
        defaultStyle = paint.getStyle();

    }

    @Override
    public void setForegroundColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void setTextSize(float height) {
        paint.setTextSize(height);
    }

    @Override
    public void setStroke(Stroke stroke) {
        switch (stroke) {
            case FILL:
                paint.setStyle(Paint.Style.FILL);
                paint.setPathEffect(defaultPathEffect);
                break;
            case DASHED_OUTLINE:
                paint.setPathEffect(new DashPathEffect(new float[]{15f, 5f}, 0f));
            default:
                paint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void resetParameters() {
        paint.setStyle(defaultStyle);
        paint.setTextSize(defaultTextSize);
        paint.setColor(defaultColor);
        paint.setPathEffect(defaultPathEffect);
    }

    @Override
    public void drawVector(int resId, float x, float y, float width, float height) {
        Drawable drawable = ViewUtils.getDrawable(getContext(), resId, (int) x, (int) y, (int) width, (int) height);
        drawable.draw(canvas);
    }

    @Override
    public void drawRect(Rect bounds) {
        canvas.drawRect(bounds, paint);
    }

    @Override
    public void drawLine(float xStart, float yStart, float xEnd, float yEnd) {
        canvas.drawLine(xStart, yStart, xEnd, yEnd, paint);
    }

    @Override
    public void drawText(float xStart, float yStart, String text) {
        canvas.drawText(text, xStart, yStart, paint);
    }
}
