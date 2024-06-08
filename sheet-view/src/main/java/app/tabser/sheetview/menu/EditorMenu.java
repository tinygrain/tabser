package app.tabser.sheetview.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import app.tabser.dom.Length;
import app.tabser.dom.Pitch;
import app.tabser.dom.Song;
import app.tabser.rendering.SheetCursor;
import app.tabser.rendering.Theme;
import app.tabser.sheetview.AndroidColor;
import app.tabser.sheetview.SheetView;

public final class EditorMenu {
    private final SheetView sheetView;
    private final Theme theme;

    public enum Menu {
        MAIN, FRET, BAR, MODE
    }

    private Song model;
    private Rect[] stringRects;
    private int selectedString = -1;
    private Length[] lengths = {Length.FULL, Length.HALF, Length.QUARTER, Length.EIGHTH, Length.SIXTEENTH, Length.THIRTY_SECOND};
    private Rect[] lengthRects = new Rect[lengths.length];
    private int selectedLength;
    private String[] moreControls = {"<-", "|<-", "Start", "Bar", "Part", "Mode"};
    private Rect[] moreRects = new Rect[moreControls.length];
    private String[] moreControls2 = {"->", "->|", "End", "Break", "Over", "View"};
    private Rect[] moreRects2 = new Rect[moreControls.length];
    private Rect[][] fretRects = new Rect[6][5];
    private String[][] fretStrings = new String[6][5];

    {
        int number = 0;
        for (int y = 0; y < fretRects.length; y++) {
            for (int x = 0; x < fretRects[y].length; x++) {
                if (x == fretRects[y].length - 1 && y == fretRects.length - 1) {
                    fretStrings[y][x] = "-";
                } else if (x == fretRects[y].length - 2 && y == fretRects.length - 1) {
                    fretStrings[y][x] = "X";
                } else {
                    fretStrings[y][x] = String.valueOf(number);
                    number++;
                }
            }
        }
    }

    private String[] modeControls = {"Compact", "Auto-Next", "Auto-Bar", "Auto-Off Insert", "Back"};
    private Rect[] modeRects = new Rect[modeControls.length];

    private String[] barMenu = {"Normal", "Double", "Double-Bold", "Repeat Start", "Repeat End", "New Beat"};

    private Rect[] barRects = new Rect[barMenu.length];

    private Menu menu = Menu.MAIN;
    private Rect menuRect;

    //private boolean insert;
    private boolean autoToggleInsert;

    private final SharedPreferences preferences;
    private final Context context;

    private final EditorMenuController editorMenuController ;

    public EditorMenu(Rect menuRect, SheetView sheetView, Context c, Theme theme, SharedPreferences preferences) {
        this.theme = theme;
        this.context = c;
        this.menuRect = menuRect;
        this.sheetView = sheetView;
        this.preferences = preferences;
        this.editorMenuController = new EditorMenuController();
        moreControls2[4] = sheetView.settings.isInsert() ? "Insert" : "Over";
        autoToggleInsert = preferences.getBoolean("auto-toggle-insert", false);
        selectedLength = preferences.getInt("speed", 2);
    }

    public void loadModel(Song model) {
        this.model = model;
        this.stringRects = new Rect[model.getTuning().getStringCount()];
    }

    public void drawControls(Canvas canvas, Paint paint) {
        paint.setColor(AndroidColor.getInt(theme.getBackgroundColorKeyboard()));
        canvas.drawRect(menuRect, paint);
        int menuHeight = (menuRect.bottom - menuRect.top);
        float xStart = sheetView.getMetrics().xMargin;
        float x = xStart;
        if (menu == Menu.MAIN || menu == Menu.FRET) {
            x = drawStrings(xStart, menuHeight, paint, canvas);
        }
        if (menu == Menu.MAIN) {
            x = drawLengths(x, canvas, paint, menuHeight, xStart);
            float width = (menuRect.right - xStart - x) / 2;
            x = drawMoreControlsColumn1(x, canvas, paint, xStart, menuHeight, width);
            x = drawMoreControlsColumn2(x, canvas, paint, xStart, menuHeight, width);
            Rect rLeftover = new Rect((int) x, menuRect.top, menuRect.right, menuRect.bottom);
            paint.setColor(Color.CYAN);
            //canvas.drawRect(rLeftover, paint);
        } else if (menu == Menu.FRET) {
            x = drawFrets(x, canvas, paint, xStart, menuHeight, menuRect.right);
        } else if (menu == Menu.MODE) {
            drawModes(canvas, paint, xStart, menuHeight, menuRect.right);
        } else if (menu == Menu.BAR) {
            drawBarMenu(canvas, paint, xStart, menuHeight, menuRect.right);
        }
    }

    private float drawStrings(float xStart, int menuHeight, Paint paint, Canvas canvas) {
        Pitch[] pitches = model.getTuning().getPitches();
        paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
        float textSize = (menuHeight - xStart) / model.getTuning().getStringCount();
        paint.setTextSize(textSize);
        float x = xStart * 2;
        float y = menuRect.top + textSize;
        int xMax = 0;
        for (int i = pitches.length - 1; i > -1; i--) {
            String str = pitches[i].getNoteName();
            Rect textBounds = new Rect();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            xMax = Math.max(xMax, textBounds.right);
            Rect strRect = new Rect((int) x, (int) y + textBounds.top, (int) x + textBounds.right, (int) y);
            if (selectedString == i) {
                paint.setColor(AndroidColor.getInt(theme.getForegroundColorActiveKeyboard()));
            } else {
                paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
            }
            canvas.drawText(str, x, y, paint);
            stringRects[i] = strRect;

            y += textSize;
        }
        paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
        return x + xMax + xStart * 2;
    }

    private float drawLengths(float x, Canvas canvas, Paint paint, float menuHeight, float xStart) {
        float textSize = (menuHeight - xStart) / lengths.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        int xMax = 0;
        for (int i = 0; i < lengths.length; i++) {
            Rect textBounds = new Rect();
            String str = lengths[i].getSignature();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            xMax = Math.max(xMax, textBounds.right);
        }
        for (int i = 0; i < lengths.length; i++) {
            Rect textBounds = new Rect();
            String str = lengths[i].getSignature();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) y + textBounds.top, (int) x + xMax, (int) y);
            lengthRects[i] = strRect;
            float xCentered = x + xMax / 2 - textBounds.width() / 2;
            if (selectedLength == i) {
                paint.setColor(AndroidColor.getInt(theme.getForegroundColorActiveKeyboard()));
            } else {
                paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
            }
            canvas.drawText(str, xCentered, y, paint);
            y += textSize;
        }
        paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
        return x + xMax + xStart;
    }

    private float drawMoreControlsColumn1(float x, Canvas canvas, Paint paint, float xStart, int menuHeight, float width) {
        float textSize = (menuHeight - xStart) / moreControls.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        for (int i = 0; i < moreControls.length; i++) {
            Rect textBounds = new Rect();
            String str = moreControls[i];
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) (y - textSize), (int) (x + width), (int) y);
            moreRects[i] = strRect;
            float xCentered = x + width / 2 - textBounds.width() / 2f;
            canvas.drawText(str, xCentered, y, paint);
            paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
            y += textSize;
        }
        return x + width + xStart;
    }

    private float drawMoreControlsColumn2(float x, Canvas canvas, Paint paint, float xStart, int menuHeight, float width) {
        float textSize = (menuHeight - xStart) / moreControls2.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        for (int i = 0; i < moreControls2.length; i++) {
            Rect textBounds = new Rect();
            String str = moreControls2[i];
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) (y - textSize), (int) (x + width), (int) y);
            moreRects2[i] = strRect;
            float xCentered = x + width / 2 - textBounds.width() / 2f;
            canvas.drawText(str, xCentered, y, paint);
            paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
            y += textSize;
        }
        return x + width + xStart;
    }

    private float drawFrets(float x, Canvas canvas, Paint paint, float xStart, int menuHeight, int menuWidth) {
        float textSize = (menuHeight - xStart) / fretStrings.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        int xUnit = (int) ((menuWidth - x) / fretStrings[0].length);
        int yUnit = (int) textSize;
        for (int yIndex = 0; yIndex < fretStrings.length; yIndex++) {
            for (int xIndex = 0; xIndex < fretStrings[yIndex].length; xIndex++) {
                String str = fretStrings[yIndex][xIndex];
                Rect textBounds = new Rect();
                paint.getTextBounds(str, 0, str.length(), textBounds);
                Rect strRect = new Rect((int) x + xIndex * xUnit, (int) y + yIndex * yUnit + textBounds.top,
                        (int) x + +xIndex * xUnit + xUnit, (int) y + yIndex * yUnit);
                fretRects[yIndex][xIndex] = strRect;
                float xCentered = x + xIndex * xUnit + xUnit / 2 - textBounds.width() / 2;
                canvas.drawText(str, xCentered, y + yUnit * yIndex, paint);
            }
        }
        return 0f;
    }

    private void drawModes(Canvas canvas, Paint paint, float xStart, int menuHeight, int menuWidth) {
        float textSize = (menuHeight - xStart) / 6;
        float y = menuRect.top + textSize;
        float x = 0;
        paint.setTextSize(textSize);
        int foregroundColorInactive = AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard());
        int foregroundColorActive = AndroidColor.getInt(theme.getForegroundColorActiveKeyboard());
        paint.setColor(foregroundColorInactive);
        int xUnit = (menuWidth);
        int yUnit = (int) textSize;
        for (int yIndex = 0, n = 0; n < modeControls.length; yIndex++) {
            String str = modeControls[n];
            Rect textBounds = new Rect();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            switch (yIndex) {
                case 0:
                    // COMPACT
                    paint.setColor(sheetView.settings.isCompact() ? foregroundColorActive : foregroundColorInactive);
                    break;
                case 1:
                    // Auto Next
                    paint.setColor(sheetView.settings.isAutoNext() ? foregroundColorActive : foregroundColorInactive);
                    break;
                case 2:
                    // Auto Bar
                    paint.setColor(sheetView.settings.isAutoBar() ? foregroundColorActive : foregroundColorInactive);
                    break;
                case 3:
                    // Auto toggle ins
                    paint.setColor(autoToggleInsert ? foregroundColorActive : foregroundColorInactive);
                    break;
                case 4:
                    // Back
                    paint.setColor(foregroundColorInactive);
                    break;
            }
            Rect strRect = new Rect((int) x, (int) y + yIndex * yUnit + textBounds.top,
                    (int) x + xUnit, (int) y + yIndex * yUnit);
            modeRects[n] = strRect;
            float xCentered = x + xUnit / 2 - textBounds.width() / 2;
            canvas.drawText(str, xCentered, y + yUnit * yIndex, paint);
            n++;
        }
    }

    private void drawBarMenu(Canvas canvas, Paint paint, float xStart, int menuHeight, int menuWidth) {
        float textSize = (menuHeight - xStart) / barMenu.length;
        float y = menuRect.top + textSize;
        float x = 0;
        paint.setTextSize(textSize);
        paint.setColor(AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard()));
        int xUnit = (menuWidth);
        int yUnit = (int) textSize;
        for (int yIndex = 0; yIndex < barMenu.length; yIndex++) {
            String str = barMenu[yIndex];
            Rect textBounds = new Rect();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) y + yIndex * yUnit + textBounds.top,
                    (int) x + xUnit, (int) y + yIndex * yUnit);
            barRects[yIndex] = strRect;
            float xCentered = x + xUnit / 2 - textBounds.width() / 2;
            canvas.drawText(str, xCentered, y + yUnit * yIndex, paint);
        }
    }

    public boolean isInSubMenu() {
        return menu == Menu.MODE || menu == Menu.FRET || menu == Menu.BAR;
    }

    public void showMainMenu(View view) {
        menu = Menu.MAIN;
        selectedString = -1;
        view.invalidate();
    }

    public String touch(View view, MotionEvent motionEvent, boolean longClick) {
        String message = "No Action";
        search:
        {
            if (menu == Menu.MAIN || menu == Menu.FRET) {
                for (int i = 0; i < stringRects.length; i++) {
                    if (stringRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        message = model.getTuning().getPitches()[i].getNoteName();
                        if (selectedString == i) {
                            this.selectedString = -1;
                            menu = Menu.MAIN;
                        } else {
                            selectedString = i;
                            menu = Menu.FRET;
                        }
                        view.invalidate();
                        break search;
                    }
                }
            }
            if (menu == Menu.MAIN) {
                for (int i = 0; i < lengthRects.length; i++) {
                    if (lengthRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        message = lengths[i].getSignature();
                        selectedLength = i;
                        SharedPreferences.Editor speedEditor = preferences.edit();
                        speedEditor.putInt("speed", i);
                        speedEditor.apply();
                        view.invalidate();
                        break search;
                    }
                }
                for (int i = 0; i < moreRects.length; i++) {
                    if (moreRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        message = moreControls[i];
                        switch (i) {
                            case 0:
                                // PREVIOUS
                                sheetView.nav.previousBeat();
                                view.invalidate();
                                break;
                            case 1:
                                // Prev. Bar
                                sheetView.nav.previousBar();
                                view.invalidate();
                                break;
                            case 2:
                                // START
                                sheetView.nav.start();
                                view.invalidate();
                                break;
                            case 3:
                                // BAR
                                if (longClick) {
                                    menu = Menu.BAR;
                                } else {
                                    sheetView.nav.newBar();
                                }
                                view.invalidate();
                                break;
                            case 4:
                                // SEQUENCE

                                break;
                            case 5:
                                // MODE
                                menu = Menu.MODE;
                                view.invalidate();
                                break;
                        }
                        break search;
                    }
                }
                for (int i = 0; i < moreRects2.length; i++) {
                    if (moreRects2[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        message = moreControls2[i];
                        switch (i) {
                            case 0:
                                // NEXT
                                sheetView.nav.nextBeat();
                                view.invalidate();
                                break;
                            case 1:
                                // NEXT Bar
                                sheetView.nav.nextBar();
                                view.invalidate();
                                break;
                            case 2:
                                // END
                                sheetView.nav.end();
                                view.invalidate();
                                break;
                            case 3:
                                // BREAK
                                break;
                            case 4:
                                // Over/Ins
                                moreControls2[i] = sheetView.settings.toggleInsert() ? "Insert" : "Over";
                                SharedPreferences.Editor editInsert = preferences.edit();
                                editInsert.putBoolean("insert", sheetView.settings.isInsert());
                                editInsert.apply();
                                view.invalidate();
                                break;
                            case 5:
                                // VIEW
                                sheetView.settings.setMode(SheetView.Mode.VIEW);
                                view.invalidate();
                                break;
                        }
                        break search;
                    }
                }
            } else if (menu == Menu.FRET) {
                for (int yIndex = 0; yIndex < fretStrings.length; yIndex++) {
                    for (int xIndex = 0; xIndex < fretStrings[yIndex].length; xIndex++) {
                        if (fretRects[yIndex][xIndex].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            message = fretStrings[yIndex][xIndex];
                            if ("-".equals(message)) {
                                SheetCursor mc = sheetView.getSheetCursor();
                                model.clearNote(selectedString, mc.barIndex, mc.beatIndex, mc.sequenceKey);
                            } else {
                                int fret = -1;
                                try {
                                    fret = Integer.parseInt(message);
                                } catch (NumberFormatException e) {
                                }
                                SheetCursor mc = sheetView.getSheetCursor();
                                boolean newBar = model.addNote(selectedString, fret,
                                        lengths[selectedLength], mc.barIndex, mc.beatIndex,
                                        sheetView.settings.isAutoBar(), sheetView.settings.isInsert(), mc.sequenceKey);
                                if (newBar) {
                                    sheetView.nav.nextBar();
                                } else if (sheetView.settings.isAutoNext()) {
                                    sheetView.nav.nextBeat();
                                }
                            }
                            menu = Menu.MAIN;
                            selectedString = -1;
                            view.invalidate();
                            break search;
                        }
                    }
                }
            } else if (menu == Menu.MODE) {
                for (int i = 0; i < modeRects.length; i++) {
                    if (modeRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        message = modeControls[i];
                        switch (i) {
                            case 0:
                                // COMPACT
                                SharedPreferences.Editor e = preferences.edit();
                                e.putBoolean("compact", sheetView.settings.toggleCompact());
                                e.apply();
                                view.invalidate();
                                break;
                            case 1:
                                // Auto Next
                                SharedPreferences.Editor e1 = preferences.edit();
                                e1.putBoolean("auto-next", sheetView.settings.toggleAutoNext());
                                e1.apply();
                                view.invalidate();
                                break;
                            case 2:
                                // Auto Bar
                                SharedPreferences.Editor e2 = preferences.edit();
                                e2.putBoolean("auto-bar", sheetView.settings.toggleAutoBar());
                                e2.apply();
                                view.invalidate();
                                break;
                            case 3:
                                // Auto toggle ins
                                this.autoToggleInsert = !this.autoToggleInsert;
                                SharedPreferences.Editor e3 = preferences.edit();
                                e3.putBoolean("auto-toggle-insert", this.autoToggleInsert);
                                e3.apply();
                                view.invalidate();
                                break;
                            case 4:
                                // Back
                                menu = Menu.MAIN;
                                view.invalidate();
                                break;
                        }
                        break search;
                    }
                }
            } else if (menu == Menu.BAR) {
                for (int i = 0; i < barMenu.length; i++) {
                    switch (i) {
                        case 0: // Normal
                            sheetView.nav.newBar();
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                    }
                }
            }
        }
        return message;
    }
}
