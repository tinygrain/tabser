package app.tabser.view.render;

import app.tabser.view.model.RenderModel;
import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.definition.SongRenderer;

public abstract class AbstractSongRenderer implements SongRenderer {

    private final RenderModel renderModel;

    public AbstractSongRenderer(RenderModel renderModel) {
        this.renderModel = renderModel;
    }

    protected RenderModel getRenderModel() {
        return renderModel;
    }

    @Override
    public void renderDocument(RenderOptions options) {
       RenderIterator iterator = iterator(options);
       preProcess(iterator);
        while (iterator.hasNext()) {
            renderBlock(iterator);
            if (iterator.pageEndReached && !iterator.endReached) {
                newPage(iterator);
                iterator.pageOffset++;
                iterator.yPosition = 0;
            } else {
            }
        }
        postProcess(iterator);
    }

    private void renderBlock(RenderIterator iterator) {
        RenderBlock block = iterator.next();
        block.render(iterator);
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

    protected RenderIterator iterator(RenderOptions options) {
        return new RenderIterator(renderModel, options);
    }

    public RenderModel getModel() {
        return renderModel;
    }
}
