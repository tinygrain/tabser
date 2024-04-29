package app.tabser.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

class ViewUtils {
    public static Drawable getDrawable(Context c, int drawable, int xPos, int yPos, int width, int height) {
        Drawable d = ResourcesCompat.getDrawable(c.getResources(), drawable, null);
        Rect r = new Rect();
        int right = xPos + width;
        int bottom = yPos + height;
        r.set(xPos, yPos, right, bottom);
        d.setBounds(r);
        return d;
    }

    public static Drawable colorVector(Context c, int drawable, int color) {
        Drawable d = ResourcesCompat.getDrawable(c.getResources(), drawable, null);
        Drawable cd = d.mutate();
        cd.setTint(color);
        return cd;
    }
}
