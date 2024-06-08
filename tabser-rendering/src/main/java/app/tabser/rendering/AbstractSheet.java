package app.tabser.rendering;

import app.tabser.rendering.geometry.SheetMetrics;

public abstract class AbstractSheet implements Sheet {
//    private final Rect viewPort;
    private final SheetMetrics sheetMetrics;
//    private final Context context;


    public AbstractSheet(SheetMetrics sheetMetrics) {
//        this.context = context;
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

//    public Context getContext() {
//        return context;
//    }
}
