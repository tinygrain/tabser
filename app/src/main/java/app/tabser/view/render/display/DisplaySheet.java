package app.tabser.view.render.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import app.tabser.rendering.AbstractSheet;
import app.tabser.rendering.Sheet;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;
import app.tabser.view.ViewUtils;

public class DisplaySheet extends AbstractSheet implements Sheet {
    private Canvas canvas;
    private Paint paint;

    private float defaultTextSize;
    private int defaultColor;
    private PathEffect defaultPathEffect;
    private Paint.Style defaultStyle;
    private Context context;


    public DisplaySheet(SheetMetrics sheetMetrics) {
        super(sheetMetrics);
    }

    public void setUp(Canvas canvas, Paint paint, Context context) {
        this.canvas = canvas;
        this.paint = paint;
        this.context = context;
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
        Drawable drawable = ViewUtils.getDrawable(context, resId, (int) x, (int) y, (int) width, (int) height);
        drawable.draw(canvas);
    }

    @Override
    public void drawRect(Rectangle bounds) {
        Rect boundsA = new Rect((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
        canvas.drawRect(boundsA, paint);
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
