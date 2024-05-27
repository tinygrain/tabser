package app.tabser.view.render;

import android.content.Context;

import app.tabser.view.viewmodel.geometry.SheetMetrics;

public abstract class AbstractSheet implements Sheet {
//    private final Rect viewPort;
    private final SheetMetrics sheetMetrics;
    private final Context context;


    public AbstractSheet(Context context, SheetMetrics sheetMetrics) {
        this.context = context;
//        this.viewPort = viewPort;
        this.sheetMetrics = sheetMetrics;
    }

    @Override
    public boolean isMultiPage() {
        return false;
    }

//    @Override
//    }
//    public Rect getViewPort() {
//        return viewPort;

    @Override
    public SheetMetrics getMetrics() {
        return sheetMetrics;
    }

    public Context getContext() {
        return context;
    }
}
