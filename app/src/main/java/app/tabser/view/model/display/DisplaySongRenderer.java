package app.tabser.view.model.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import app.tabser.view.render.AbstractSongRenderer;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.definition.SongRenderer;
import app.tabser.view.render.RenderModel;

public class DisplaySongRenderer extends AbstractSongRenderer implements SongRenderer {

    private final DisplaySheet sheet;

    public DisplaySongRenderer(RenderModel renderModel, DisplaySheet sheet) {
        super(renderModel);
        this.sheet = sheet;
    }

    public void setUp(Canvas canvas, Paint paint) {
        sheet.setUp(canvas, paint);
    }

    @Override
    public Sheet getSheet() {
        return sheet;
    }
}
