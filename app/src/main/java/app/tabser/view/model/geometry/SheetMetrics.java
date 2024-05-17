package app.tabser.view.model.geometry;

public class SheetMetrics {
    public static final SheetMetrics DEFAULT_METRICS =
            new SheetMetrics(5f, 50f, 100f, 25f, 333L, false);
    public final float strokeWidth;
    public final float yIncrement;
    public final float yMargin;

    public final float xMargin;

    public  final long animationDurationPerLine;

    public final boolean compact;

    public SheetMetrics(float strokeWidth, float yIncrement, float yMargin, float xMargin,
                        long animationDurationPerLine, boolean compact) {
        this.strokeWidth = strokeWidth;
        this.yIncrement = yIncrement;
        this.yMargin = yMargin;
        this.xMargin = xMargin;
        this.animationDurationPerLine = animationDurationPerLine;
        this.compact = compact;
    }

    public long getAnimationDuration(float lineCount) {
        return (long) (animationDurationPerLine * lineCount);
    }
}
