package app.tabser.rendering.geometry;

public class Rectangle {
    public float left;
    public float top;
    public float right;
    public float bottom;

    public Rectangle(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rectangle() {
    }

    public boolean contains(float x, float y) {
        return x >= left && y >= top && x <= right && y <= bottom;
    }

    public void set(Rectangle bounds) {
        this.left = bounds.left;
        this.top = bounds.top;
        this.right = bounds.right;
        this.bottom = bounds.bottom;
    }

    public boolean intersect(Rectangle area) {
        return contains(area.left, top) || contains(area.left, area.bottom)
                || contains(area.right, area.top) || contains(area.right, area.bottom);
    }

    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rectangle translate(Rectangle bounds) {
        return new Rectangle(bounds.left-left, bounds.top-top, bounds.right-left, bounds.bottom-top);
    }
}
