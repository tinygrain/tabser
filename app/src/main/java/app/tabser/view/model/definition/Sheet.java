package app.tabser.view.model.definition;

import android.graphics.Rect;

import app.tabser.view.model.geometry.SheetMetrics;

public interface Sheet {

    enum Stroke {
        FILL, OUTLINE, DASHED_OUTLINE
    }
    Rect getViewPort();
    boolean isMultiPage();
    void drawVector(int resId, float x, float y, float width, float height);

    void drawLine(float xStart, float yStart, float xEnd, float yEnd);
    void drawRect(Rect bounds);

    void drawText(float xStart, float yStart, String text);

    default SheetMetrics getMetrics() {
        return SheetMetrics.DEFAULT_METRICS;
    }

    void setForegroundColor(int color);

    void setTextSize(float height);

    void setStroke(Stroke stroke);

    void resetParameters();
}
