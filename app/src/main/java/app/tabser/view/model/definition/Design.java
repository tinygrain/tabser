package app.tabser.view.model.definition;

import android.graphics.Color;

import app.tabser.view.model.geometry.SheetMetrics;

public final class Design {
    public static final Design BRIGHT_MODE = new Design(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.LTGRAY, Color.BLACK, Color.GREEN
    );
    public static final Design DARK_MODE = new Design(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.DKGRAY, Color.LTGRAY, Color.GREEN
    );
    public static  final Design PAPER_MODE = new Design(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.WHITE, Color.BLACK, Color.GREEN
    );
    public static final Design MICKEY_MOUSE = new Design(
            Color.BLACK, Color.CYAN, Color.MAGENTA,
            Color.CYAN, Color.BLUE, Color.MAGENTA
    );

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