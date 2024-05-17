package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.io.IOException;

import app.tabser.R;
import app.tabser.model.Song;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.model.pdf.PDFSheet;
import app.tabser.view.model.pdf.PDFSongRenderer;
import app.tabser.view.render.SongRendererFactory;

public class TabViewControls {
    private final SheetView sheet;
    private final Context context;
    private final Design design;
    private final TabView tabView;
    private final Rect[] buttonRects = new Rect[5];
    private Song model;

    public TabViewControls(SheetView sheet, Context context, Design design, TabView tabView) {
        this.tabView = tabView;
        this.sheet = sheet;
        this.context = context;
        this.design = design;
    }

    public void loadModel(Song model) {
        this.model = model;
    }

    public float drawMenu(Canvas canvas, Paint paint) {
        int height = canvas.getHeight() / 14;
        float yStart = canvas.getHeight() - height;
        int foregroundColor = design.getForegroundColorInactiveKeyboard();
        int backgroundColor = design.getBackgroundColorKeyboard();
        Rect topMenu = new Rect(0, (int) yStart, canvas.getWidth(), canvas.getHeight());
        paint.setColor(backgroundColor);
        canvas.drawRect(topMenu, paint);
        int buttonDim = (int) (height * 0.9);
        int yPos = (int) (height * 0.05);
        int[] buttons = {R.drawable.baseline_play_arrow_24, R.drawable.baseline_stop_24,
                R.drawable.baseline_loop_24, R.drawable.metronome, R.drawable.baseline_edit_24};
        int i = 0;
        int xPos = 0;
        yPos += (int) yStart;
        for (int button : buttons) {
            if (i == buttons.length - 1) {
                xPos = canvas.getWidth() - height;
            }
            Drawable buttonDrawable = ViewUtils.getDrawable(context, button,
                    xPos, yPos, buttonDim, buttonDim);
            buttonDrawable.setTint(foregroundColor);
            buttonDrawable.draw(canvas);
            buttonRects[i] = buttonDrawable.getBounds();;

            xPos += height;
            i++;
        }
        return yStart + height;
    }

    String touch(MotionEvent motionEvent, boolean longClick) {
        String message = "No Action";
        for (int i = 0; i < buttonRects.length; i++) {
            if (buttonRects[i].contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                switch (i) {
                    case 0: // play
                        message = "Play";
                        ToneGenerator tg = new ToneGenerator(context);
                        tg.play(model);
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
                        sheet.settings.setMode(SheetView.Mode.EDIT);
                        tabView.invalidate();
                        break;
                }
            }
        }
        return message;
    }
}
