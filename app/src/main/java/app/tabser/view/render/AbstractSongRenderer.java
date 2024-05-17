package app.tabser.view.render;

import app.tabser.view.model.blocks.LineBlock;
import app.tabser.view.model.definition.SongRenderer;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.render.RenderModel;

public abstract class AbstractSongRenderer implements SongRenderer {

    private final RenderModel renderModel;

    public AbstractSongRenderer(RenderModel renderModel) {
        this.renderModel = renderModel;
    }

    protected RenderModel getRenderModel() {
        return renderModel;
    }

    @Override
    public LineBlock getLine(int i) {
        return renderModel.getLine(i);
    }

    @Override
    public void calculateModel() {
        renderModel.calculate(iterator());
    }

    @Override
    public void renderDocument(RenderIterator iterator) {
        calculateModel();
        //RenderIterator iterator = new RenderIterator();
        preparePage(iterator);
        iterator.getModel().songHeaderBlock.render(iterator);
        while (iterator.hasNext()) {
            renderLine(iterator);
            if (iterator.pageEndReached && !iterator.endReached) {
                newPage(iterator);
            } else {
                iterator.lineOffset++;
            }
        }
    }

    @Override
    public void renderLine(RenderIterator it) {
        if (it.lineOffset < renderModel.getLineCount()) {
            LineBlock line = renderModel.getLine(it.lineOffset);
            line.render(it);
        } else {
            it.endReached = true;
        }
    }

    @Override
    public int getYMin() {
        return renderModel.getYMin();
    }

    @Override
    public RenderIterator iterator() {
        return new RenderIterator(renderModel);
    }

    @Override
    public float getHeaderHeight() {
        return renderModel.songHeaderBlock.getBounds().height();
    }

    public RenderModel getModel() {
        return  renderModel;
    }
}
