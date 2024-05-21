package app.tabser.view.model.geometry;

import android.graphics.Rect;

public final class ViewPort {
    public float deltaX;
    public float deltaY;
    public Rect area = new Rect();

    public Rect translate(Rect original) {
        return new Rect((int) (original.left + deltaX), (int) (original.top + deltaY),
                (int) (original.right + deltaX), (int) (original.bottom + deltaY));
    }

    public boolean isInView(Rect blockBoundsOnPage) {
        return translate(blockBoundsOnPage).intersect(area);
    }
}
