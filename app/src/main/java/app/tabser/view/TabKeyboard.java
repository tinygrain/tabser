package app.tabser.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import app.tabser.model.Pitch;
import app.tabser.model.Speed;
import app.tabser.model.TabModel;

class TabKeyboard {
    private static final int BACKGROUND_COLOR = Color.BLACK;
    private static final int FOREGROUND_COLOR_INACTIVE = Color.WHITE;
    private static final int FOREGROUND_COLOR_ACTIVE = Color.GREEN;
    private static final int MORE_NEXT = 0;
    private static final int MORE_PREVIOUS = 1;
    private static final int MORE_BREAK = 2;
    private static final int MORE_BAR = 3;
    private static final int MORE_COMPACT = 4;
    private static final int MORE_META = 5;
    private final TabSheet sheet;

    private enum Menu {
        MAIN, FRET, BAR, MODE
    }

    private TabModel model;
    private Rect[] stringRects;
    private int selectedString = -1;
    private Speed[] speeds = {Speed.FULL, Speed.HALF, Speed.QUARTER, Speed.EIGHTH, Speed.SIXTEENTH, Speed.THIRTY_SECOND};
    private Rect[] speedRects = new Rect[speeds.length];
    private int selectedSpeed = 3;
    private String[] moreControls = {"<-", "|<-", "Start", "Bar", "Mode", "Meta"};
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
    private Menu menu = Menu.MAIN;
    private Rect menuRect;
    private boolean autoToggleInsert;

    TabKeyboard(Rect menuRect, TabSheet sheet) {
        this.menuRect = menuRect;
        this.sheet = sheet;
    }

    void loadModel(TabModel model) {
        this.model = model;
        this.stringRects = new Rect[model.getTuning().getStringCount()];
    }

    void drawControls(Canvas canvas, Paint paint) {
        paint.setColor(BACKGROUND_COLOR);
        canvas.drawRect(menuRect, paint);
        int menuHeight = (menuRect.bottom - menuRect.top);
        float xStart = sheet.getXStart();
        float x = xStart;
        if (menu == Menu.MAIN || menu == Menu.FRET) {
            x = drawStrings(xStart, menuHeight, paint, canvas);
        }
        if (menu == Menu.MAIN) {
        /*
        float xSeparator = x - xStart / 2f;
        float strokeWidth = sheet.getStrokeWidth();
        paint.setStrokeWidth(strokeWidth * 2);
        canvas.drawLine(xSeparator, menuRect.top, xSeparator, menuRect.bottom, paint);
        */
            x = drawSpeeds(x, canvas, paint, menuHeight, xStart);
        /*
        xSeparator = x - xStart / 2f;
        paint.setStrokeWidth(strokeWidth * 2);
        canvas.drawLine(xSeparator, menuRect.top, xSeparator, menuRect.bottom, paint);
         */
            x = drawMoreControls(x, canvas, paint, xStart, menuHeight);
            x = drawMoreControls2(x, canvas, paint, xStart, menuHeight);
        } else if (menu == Menu.FRET) {
            x = drawFrets(x, canvas, paint, xStart, menuHeight, menuRect.right);
        } else if (menu == Menu.MODE) {
            drawModes(canvas, paint, xStart, menuHeight, menuRect.right);
        }
    }

    private float drawStrings(float xStart, int menuHeight, Paint paint, Canvas canvas) {
        Pitch[] pitches = model.getTuning().getPitches();
        //paint.setColor(FOREGROUND_COLOR_INACTIVE);
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
                paint.setColor(FOREGROUND_COLOR_ACTIVE);
            } else {
                paint.setColor(FOREGROUND_COLOR_INACTIVE);
            }
            canvas.drawText(str, x, y, paint);
            stringRects[i] = strRect;

            y += textSize;
        }
        paint.setColor(FOREGROUND_COLOR_INACTIVE);
        return x + xMax + xStart;
    }

    private float drawSpeeds(float x, Canvas canvas, Paint paint, float menuHeight, float xStart) {
        float textSize = (menuHeight - xStart) / speeds.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        int xMax = 0;
        for (int i = 0; i < speeds.length; i++) {
            Rect textBounds = new Rect();
            String str = speeds[i].getSignature();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            xMax = Math.max(xMax, textBounds.right);
        }
        for (int i = 0; i < speeds.length; i++) {
            Rect textBounds = new Rect();
            String str = speeds[i].getSignature();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) y + textBounds.top, (int) x + xMax, (int) y);
            speedRects[i] = strRect;
            float xCentered = x + xMax / 2 - textBounds.width() / 2;
            if (selectedSpeed == i) {
                paint.setColor(FOREGROUND_COLOR_ACTIVE);
            } else {
                paint.setColor(FOREGROUND_COLOR_INACTIVE);
            }
            canvas.drawText(str, xCentered, y, paint);
            y += textSize;
        }
        paint.setColor(FOREGROUND_COLOR_INACTIVE);
        return x + xMax + xStart;
    }

    private float drawMoreControls(float x, Canvas canvas, Paint paint, float xStart, int menuHeight) {
        float textSize = (menuHeight - xStart) / moreControls.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        int xMax = 0;
        for (int i = 0; i < moreControls.length; i++) {
            Rect textBounds = new Rect();
            String str = moreControls[i];
            paint.getTextBounds(str, 0, str.length(), textBounds);
            xMax = Math.max(xMax, textBounds.right);
        }
        for (int i = 0; i < moreControls.length; i++) {
            Rect textBounds = new Rect();
            String str = moreControls[i];
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) y + textBounds.top, (int) x + xMax, (int) y);
            moreRects[i] = strRect;
            float xCentered = x + xMax / 2 - textBounds.width() / 2;
            canvas.drawText(str, xCentered, y, paint);
            paint.setColor(FOREGROUND_COLOR_INACTIVE);
            y += textSize;
        }
        return x + xMax + xStart;
    }

    private float drawMoreControls2(float x, Canvas canvas, Paint paint, float xStart, int menuHeight) {
        float textSize = (menuHeight - xStart) / moreControls2.length;
        float y = menuRect.top + textSize;
        paint.setTextSize(textSize);
        int xMax = 0;
        for (int i = 0; i < moreControls2.length; i++) {
            Rect textBounds = new Rect();
            String str = moreControls2[i];
            paint.getTextBounds(str, 0, str.length(), textBounds);
            xMax = Math.max(xMax, textBounds.right);
        }
        for (int i = 0; i < moreControls2.length; i++) {
            Rect textBounds = new Rect();
            String str = moreControls2[i];
            paint.getTextBounds(str, 0, str.length(), textBounds);
            Rect strRect = new Rect((int) x, (int) y + textBounds.top, (int) x + xMax, (int) y);
            moreRects2[i] = strRect;
            float xCentered = x + xMax / 2 - textBounds.width() / 2;
            canvas.drawText(str, xCentered, y, paint);
            paint.setColor(FOREGROUND_COLOR_INACTIVE);
            y += textSize;
        }
        return x + xMax + xStart;
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
        paint.setColor(FOREGROUND_COLOR_INACTIVE);
        int xUnit = (menuWidth);
        int yUnit = (int) textSize;
        for (int yIndex = 0, n = 0; n < modeControls.length; yIndex++) {
            String str = modeControls[n];
            Rect textBounds = new Rect();
            paint.getTextBounds(str, 0, str.length(), textBounds);
            switch (yIndex) {
                case 0:
                    // COMPACT
                    paint.setColor(sheet.isCompact() ? FOREGROUND_COLOR_ACTIVE : FOREGROUND_COLOR_INACTIVE);
                    break;
                case 1:
                    // Auto Next
                    paint.setColor(sheet.isAutoNext() ? FOREGROUND_COLOR_ACTIVE : FOREGROUND_COLOR_INACTIVE);
                    break;
                case 2:
                    // Auto Bar
                    paint.setColor(sheet.isAutoBar() ? FOREGROUND_COLOR_ACTIVE : FOREGROUND_COLOR_INACTIVE);
                    break;
                case 3:
                    // Auto toggle ins
                    paint.setColor(autoToggleInsert ? FOREGROUND_COLOR_ACTIVE : FOREGROUND_COLOR_INACTIVE);
                    break;
                case 4:
                    // Back
                    paint.setColor(FOREGROUND_COLOR_INACTIVE);
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

    String touch(View view, MotionEvent motionEvent) {
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
                for (int i = 0; i < speedRects.length; i++) {
                    if (speedRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        message = speeds[i].getSignature();
                        selectedSpeed = i;
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
                                sheet.previousBeat();
                                view.invalidate();
                                break;
                            case 1:
                                // Prev. Bar
                                sheet.previousBar();
                                view.invalidate();
                                break;
                            case 2:
                                // START
                                sheet.start();
                                view.invalidate();
                                break;
                            case 3:
                                // BAR
                                break;
                            case 4:
                                // MODE
                                menu = Menu.MODE;
                                view.invalidate();
                                break;
                            case 5:
                                // Meta
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
                                sheet.nextBeat();
                                view.invalidate();
                                break;
                            case 1:
                                // NEXT Bar
                                sheet.nextBar();
                                view.invalidate();
                                break;
                            case 2:
                                // END
                                sheet.end();
                                view.invalidate();
                                break;
                            case 3:
                                // BREAK
                                break;
                            case 4:
                                // Over/Ins
                                moreControls2[i] = sheet.toggleInsert() ? "Insert" : "Over";
                                view.invalidate();
                                break;
                            case 5:
                                // VIEW
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
                                model.clearNote(selectedString, sheet.getBarIndex(), sheet.getBeatIndex());
                            } else {
                                int fret = -1;
                                try {
                                    fret = Integer.parseInt(message);
                                } catch (NumberFormatException e) {
                                }
                                boolean newBar = model.addNote(selectedString, fret, speeds[selectedSpeed], sheet.getBarIndex(), sheet.getBeatIndex(), sheet.isAutoBar());
                                if (newBar) {
                                    sheet.nextBar();
                                } else if (sheet.isAutoNext()) {
                                    sheet.nextBeat();
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
                                sheet.setCompact(!sheet.isCompact());
                                view.invalidate();
                                break;
                            case 1:
                                // Auto Next
                                sheet.setAutoNext(!sheet.isAutoNext());
                                view.invalidate();
                                break;
                            case 2:
                                // Auto Bar
                                sheet.setAutoBar(!sheet.isAutoBar());
                                view.invalidate();
                                break;
                            case 3:
                                // Auto toggle ins
                                this.autoToggleInsert = !this.autoToggleInsert;
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
            }
        }
        return message;
    }
}
