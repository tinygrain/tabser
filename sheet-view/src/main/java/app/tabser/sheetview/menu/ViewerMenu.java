package app.tabser.sheetview.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.Arrays;

import app.tabser.dom.Song;
import app.tabser.rendering.Theme;
import app.tabser.sheetview.AndroidColor;
import app.tabser.sheetview.AndroidGraphicsLoader;
import app.tabser.sheetview.R;
import app.tabser.sheetview.SheetView;

public class ViewerMenu {
    private final SheetView view;
    private final Context context;
    private final Theme theme;
    private final Rect[] buttonRects = new Rect[5];
    private final int[] buttons;
    private Song model;

    public ViewerMenu(SheetView view, Context context, Theme theme, int[] buttons) {
        this.view = view;
        this.context = context;
        this.theme = theme;
        this.buttons = Arrays.copyOf(buttons, buttons.length);
    }

    public void loadModel(Song model) {
        this.model = model;
    }

    public float drawMenu(Canvas canvas, Paint paint) {
        int height = canvas.getHeight() / 14;
        float yStart = canvas.getHeight() - height;
        int foregroundColor = AndroidColor.getInt(theme.getForegroundColorInactiveKeyboard());
        int backgroundColor = AndroidColor.getInt(theme.getBackgroundColorKeyboard());
        Rect topMenu = new Rect(0, (int) yStart, canvas.getWidth(), canvas.getHeight());
        paint.setColor(backgroundColor);
        canvas.drawRect(topMenu, paint);
        int buttonDim = (int) (height * 0.9);
        int yPos = (int) (height * 0.05);
        int i = 0;
        int xPos = 0;
        yPos += (int) yStart;
        for (int button : buttons) {
            if (i == buttons.length - 1) {
                xPos = canvas.getWidth() - height;
            }
            Drawable buttonDrawable = AndroidGraphicsLoader.getDrawable(context, button,
                    xPos, yPos, buttonDim, buttonDim);
            buttonDrawable.setTint(foregroundColor);
            buttonDrawable.draw(canvas);
            buttonRects[i] = buttonDrawable.getBounds();;

            xPos += height;
            i++;
        }
        return yStart + height;
    }

    public String touch(MotionEvent motionEvent, boolean longClick) {
        String message = "No Action";
        for (int i = 0; i < buttonRects.length; i++) {
            if (buttonRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                switch (i) {
                    case 0: // play
                        message = "Play";
//                        ToneGenerator tg = new ToneGenerator(context);
//                        tg.play(model);
                        break;
                    case 1: // Stop
                        message = "Stop";
                        break;
                    case 2: // loop
                        message = "Loop";
                        break;
                    case 3: // Speed
                        message = "Speed";
                        break;
                    case 4: // Edit
                        message = "Edit";
                        view.settings.setMode(SheetView.Mode.EDIT);
                        view.invalidate();
                        break;
                }
            }
        }
        return message;
    }
}
