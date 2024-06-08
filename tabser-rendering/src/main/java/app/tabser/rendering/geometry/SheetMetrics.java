package app.tabser.rendering.geometry;

public class SheetMetrics {
    public static final SheetMetrics getDefaultMetrics(Rectangle viewPort) {
        return new SheetMetrics(5f, 50f, 100f, 25f,
                333L, false, 25f,
                25f,25f, viewPort);
    }
    public final float strokeWidth;
    public final float yIncrement;
    public final float yMargin;

    public final float xMargin;

    public  final long animationDurationPerLine;

    public final boolean compact;

    public final float pageTopMargin;
    public final float pageBottomMargin;
    public final float pageSideMargin;
    public final Rectangle viewPort;


    public SheetMetrics(float strokeWidth, float yIncrement, float yMargin, float xMargin,
                        long animationDurationPerLine, boolean compact, float pageTopMargin,
                        float pageBottomMargin, float pageSideMargin, Rectangle viewPort) {
        this.strokeWidth = strokeWidth;
        this.yIncrement = yIncrement;
        this.yMargin = yMargin;
        this.xMargin = xMargin;
        this.animationDurationPerLine = animationDurationPerLine;
        this.compact = compact;
        this.pageTopMargin = pageTopMargin;
        this.pageBottomMargin = pageBottomMargin;
        this.pageSideMargin = pageSideMargin;
        this.viewPort = viewPort;
    }

    public long getAnimationDuration(float lineCount) {
        return (long) (animationDurationPerLine * lineCount);
    }
}
