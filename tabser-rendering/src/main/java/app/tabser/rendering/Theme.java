package app.tabser.rendering;

public final class Theme {
    public static final Theme BRIGHT_MODE = new Theme(
            ThemeColor.BLACK, ThemeColor.LTGRAY, ThemeColor.GREEN,
            ThemeColor.LTGRAY, ThemeColor.BLACK, ThemeColor.GREEN
    );
    public static final Theme DARK_MODE = new Theme(
            ThemeColor.BLACK, ThemeColor.LTGRAY, ThemeColor.GREEN,
            ThemeColor.DKGRAY, ThemeColor.LTGRAY, ThemeColor.GREEN
    );
    public static  final Theme PAPER_MODE = new Theme(
            ThemeColor.BLACK, ThemeColor.LTGRAY, ThemeColor.GREEN,
            ThemeColor.WHITE, ThemeColor.BLACK, ThemeColor.GREEN
    );
    public static final Theme MICKEY_MOUSE = new Theme(
            ThemeColor.BLACK, ThemeColor.CYAN, ThemeColor.MAGENTA,
            ThemeColor.CYAN, ThemeColor.BLUE, ThemeColor.MAGENTA
    );

    public final ThemeColor backgroundColorKeyboard;
    public final ThemeColor foregroundColorInactiveKeyboard;
    public final ThemeColor foregroundColorActiveKeyboard;
    public final ThemeColor backgroundColorSheet;
    public final ThemeColor foregroundColorInactiveSheet;
    public final ThemeColor foregroundColorActiveSheet;

    public Theme(ThemeColor backgroundColorKeyboard, ThemeColor foregroundColorInactiveKeyboard,
                 ThemeColor foregroundColorActiveKeyboard, ThemeColor backgroundColorSheet,
                 ThemeColor foregroundColorInactiveSheet, ThemeColor foregroundColorActiveSheet) {
        this.backgroundColorKeyboard = backgroundColorKeyboard;
        this.foregroundColorInactiveKeyboard = foregroundColorInactiveKeyboard;
        this.foregroundColorActiveKeyboard = foregroundColorActiveKeyboard;
        this.backgroundColorSheet = backgroundColorSheet;
        this.foregroundColorInactiveSheet = foregroundColorInactiveSheet;
        this.foregroundColorActiveSheet = foregroundColorActiveSheet;
    }

    public ThemeColor getBackgroundColorKeyboard() {
        return backgroundColorKeyboard;
    }

    public ThemeColor getForegroundColorInactiveKeyboard() {
        return foregroundColorInactiveKeyboard;
    }

    public ThemeColor getForegroundColorActiveKeyboard() {
        return foregroundColorActiveKeyboard;
    }

    public ThemeColor getBackgroundColorSheet() {
        return backgroundColorSheet;
    }

    public ThemeColor getForegroundColorInactiveSheet() {
        return foregroundColorInactiveSheet;
    }

    public ThemeColor getForegroundColorActiveSheet() {
        return foregroundColorActiveSheet;
    }

}