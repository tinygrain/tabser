package app.tabser.view.render;

import android.graphics.Color;

public final class Theme {
    public static final Theme BRIGHT_MODE = new Theme(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.LTGRAY, Color.BLACK, Color.GREEN
    );
    public static final Theme DARK_MODE = new Theme(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.DKGRAY, Color.LTGRAY, Color.GREEN
    );
    public static  final Theme PAPER_MODE = new Theme(
            Color.BLACK, Color.LTGRAY, Color.GREEN,
            Color.WHITE, Color.BLACK, Color.GREEN
    );
    public static final Theme MICKEY_MOUSE = new Theme(
            Color.BLACK, Color.CYAN, Color.MAGENTA,
            Color.CYAN, Color.BLUE, Color.MAGENTA
    );

    public final int backgroundColorKeyboard;
    public final int foregroundColorInactiveKeyboard;
    public final int foregroundColorActiveKeyboard;
    public final int backgroundColorSheet;
    public final int foregroundColorInactiveSheet;
    public final int foregroundColorActiveSheet;

    public Theme(int backgroundColorKeyboard, int foregroundColorInactiveKeyboard,
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