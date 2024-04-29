package app.tabser.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import app.tabser.R;

public class TabViewControls {

    private final TabSheet sheet;

    private final Context context;

    public TabViewControls(TabSheet sheet, Context context) {
        this.sheet = sheet;
        this.context = context;
    }

    public float drawTopMenu(Canvas canvas, Paint paint, float yStart, int foregroundColor, int backgroundColor) {
        int height = canvas.getHeight() / 14;
        Rect topMenu = new Rect(0, 0, canvas.getWidth(), height);
        paint.setColor(foregroundColor);
        canvas.drawRect(topMenu, paint);
        int buttonDim = (int) (height * 0.9);
        int yPos = (int) (height * 0.05);
        int[] buttons = {R.drawable.baseline_play_arrow_24, R.drawable.baseline_stop_24,
                R.drawable.baseline_loop_24, R.drawable.metronome, R.drawable.baseline_edit_24};
        int i = 0;
        int xPos = yPos;
        for (int button : buttons) {
            Drawable buttonDrawable = ViewUtils.getDrawable(context, button,
                    xPos, yPos, buttonDim, buttonDim);
            buttonDrawable.setTint(backgroundColor);
            buttonDrawable.draw(canvas);
            xPos += height;
        }
        return yStart + height;
    }
}
