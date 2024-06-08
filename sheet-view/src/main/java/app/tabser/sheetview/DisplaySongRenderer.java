package app.tabser.sheetview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import app.tabser.rendering.AbstractSongRenderer;
import app.tabser.rendering.RenderModel;
import app.tabser.rendering.Sheet;
import app.tabser.rendering.SongRenderer;

public class DisplaySongRenderer extends AbstractSongRenderer implements SongRenderer {

    private final DisplaySheet sheet;
    private final Context context;

    public DisplaySongRenderer(RenderModel renderModel, DisplaySheet sheet, Context context) {
        super(renderModel);
        this.sheet = sheet;
        this.context = context;
    }

    public void setUp(Canvas canvas, Paint paint) {
        sheet.setUp(canvas, paint, context);
    }

    @Override
    public Sheet getSheet() {
        return sheet;
    }
}
