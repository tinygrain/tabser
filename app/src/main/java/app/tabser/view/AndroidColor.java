package app.tabser.view;

import android.graphics.Color;

import app.tabser.rendering.ThemeColor;

public final class AndroidColor {
    public static int getInt(ThemeColor themeColor) {
        switch (themeColor) {
            case CYAN:
                return Color.CYAN;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case DKGRAY:
                return Color.DKGRAY;
            case GREEN:
                return Color.GREEN;
            case LTGRAY:
                return Color.LTGRAY;
            case MAGENTA:
                return Color.MAGENTA;
            default:
                return Color.WHITE;
        }
    }
}
