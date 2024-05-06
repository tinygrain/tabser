package app.tabser.view;

import android.graphics.Color;

public class Design {
    public static Design BRIGHT_MODE = new Design(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.LTGRAY, Color.BLACK, Color.GREEN
    );
    public static Design DARK_MODE = new Design(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.DKGRAY, Color.LTGRAY, Color.GREEN
    );
    public static Design PAPER_MODE = new Design(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.WHITE, Color.BLACK, Color.GREEN
    );
    public static Design MICKEY_MOUSE = new Design(
            Color.BLACK, Color.CYAN, Color.MAGENTA,
            Color.CYAN, Color.BLUE, Color.MAGENTA
    );

    private final float strokeWidth = 5f;
    private final float yIncrement = 50f;
    private final float yStart = yIncrement * 2;
    private final float xStart = 25f;

    private final int backgroundColorKeyboard;
    private final int foregroundColorInactiveKeyboard;
    private final int foregroundColorActiveKeyboard;
    private final int backgroundColorSheet;
    private final int foregroundColorInactiveSheet;
    private final int foregroundColorActiveSheet;

    public Design(int backgroundColorKeyboard, int foregroundColorInactiveKeyboard,
                  int foregroundColorActiveKeyboard, int backgroundColorSheet,
                  int foregroundColorInactiveSheet, int foregroundColorActiveSheet) {
        this.backgroundColorKeyboard = backgroundColorKeyboard;
        this.foregroundColorInactiveKeyboard = foregroundColorInactiveKeyboard;
        this.foregroundColorActiveKeyboard = foregroundColorActiveKeyboard;
        this.backgroundColorSheet = backgroundColorSheet;
        this.foregroundColorInactiveSheet = foregroundColorInactiveSheet;
        this.foregroundColorActiveSheet = foregroundColorActiveSheet;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public float getYIncrement() {
        return yIncrement;
    }

    public float getYStart() {
        return yStart;
    }

    public float getxStart() {
        return xStart;
    }

    public int getBackgroundColorKeyboard() {
        return backgroundColorKeyboard;
    }

    public int getForegroundColorInactiveKeyboard() {
        return foregroundColorInactiveKeyboard;
    }

    public int getForegroundColorActiveKeyboard() {
        return foregroundColorActiveKeyboard;
    }

    public int getBackgroundColorSheet() {
        return backgroundColorSheet;
    }

    public int getForegroundColorInactiveSheet() {
        return foregroundColorInactiveSheet;
    }

    public int getForegroundColorActiveSheet() {
        return foregroundColorActiveSheet;
    }
}