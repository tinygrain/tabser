package app.tabser.view.render;

import app.tabser.view.viewmodel.RenderModel;

public abstract class AbstractSongRenderer implements SongRenderer {

    private final RenderModel renderModel;

    public AbstractSongRenderer(RenderModel renderModel) {
        this.renderModel = renderModel;
    }

//    protected RenderModel getRenderModel() {
//        return renderModel;
//    }

    @Override
    public void renderDocument(RenderOptions options) {
        RenderModel.RenderIterator iterator = renderModel.iterator();
        preparePage(iterator);
        while (iterator.hasNext()) {
            RenderBlock block = iterator.next();
            block.render(iterator);
        }
        postProcessDocument(iterator);
    }


    @Override
    public float getBlockOffsetY(int lineIndex) {
        if (renderModel.sheetModel.documentBlocks.size() > lineIndex) {
            return renderModel.sheetModel.documentBlocks.get(lineIndex).getBounds().top;
        }
        return 0;
    }

    @Override
    public int getYMin() {
        return renderModel.getYMin();
    }

    public RenderModel getModel() {
        return renderModel;
    }
}
