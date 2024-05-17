package app.tabser.view.render;

import android.content.Context;
import android.graphics.Rect;

import app.tabser.view.model.blocks.AdvertisementBlock;
import app.tabser.view.model.blocks.RenderBlock;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;

public abstract class AbstractSheet implements Sheet {
    private final Rect viewPort;
    private final SheetMetrics sheetMetrics;
    private Context context;


    public AbstractSheet(Context context, Rect viewPort, SheetMetrics sheetMetrics) {
        this.context = context;
        this.viewPort = viewPort;
        this.sheetMetrics = sheetMetrics;
    }

    @Override
    public boolean isMultiPage() {
        return false;
    }

    @Override
    public Rect getViewPort() {
        return viewPort;
    }

    @Override
    public SheetMetrics getMetrics() {
        return sheetMetrics;
    }

    public Context getContext() {
        return context;
    }
}
