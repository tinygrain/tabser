package app.tabser.sheetview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import app.tabser.rendering.GraphicsLoader;

public class AndroidGraphicsLoader implements GraphicsLoader {
    private final Context context;

    public AndroidGraphicsLoader(Context context) {
        this.context = context;
    }

    public static Drawable getDrawable(Context c, int drawable, int xPos, int yPos, int width, int height) {
        Drawable d = ResourcesCompat.getDrawable(c.getResources(), drawable, null);
        Rect r = new Rect();
        int right = xPos + width;
        int bottom = yPos + height;
        r.set(xPos, yPos, right, bottom);
        d.setBounds(r);
        return d;
    }

    @Override
    public final InputStream getGraphicAsStream(int resId, int xPos, int yPos, int width, int height) {
        Drawable drawable = getDrawable(context, resId, xPos, yPos, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT); //Not available in lowest targeted API?
//        int[] transparentPixels = new int[width * height];
//        bitmap.setPixels();
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //use the compression format of your need
        return new ByteArrayInputStream(stream.toByteArray());
    }

//    public static Bitmap makeTransparent(Bitmap bit, Color transparentColor) {
//        int width =  bit.getWidth();
//        int height = bit.getHeight();
//        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        int [] allpixels = new int [ myBitmap.getHeight()*myBitmap.getWidth()];
//        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(),myBitmap.getHeight());
//        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);
//        for(int i =0; i<myBitmap.getHeight()*myBitmap.getWidth();i++){
//            if( allpixels[i] == transparentColor.toArgb())
//                allpixels[i] = Color.alpha(Color.TRANSPARENT);
//        }
//        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
//        return myBitmap;
//    }
}
