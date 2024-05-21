package app.tabser.view.render;

import app.tabser.model.Sequence;
import app.tabser.view.model.RenderModel;
import app.tabser.view.model.blocks.LineBlock;
import app.tabser.view.model.definition.RenderBlock;

public final class RenderIterator {
    //    @Deprecated
//    public ArrayList<SheetView.ModelCursor> cursorPositions;
    public int barOffset;
    public int beatOffset;
    public int lineOffset;
    public int pageOffset;
    public float xPosition;
    public float yPosition;
    public boolean pageEndReached;
    public boolean endReached;
    public int calculatedBarCount;
    public int calculatedBeatCount;
    public String sequenceKey = Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME;

    private final RenderModel renderModel;

    public final RenderOptions options;
    public int blockOffset;

    RenderIterator(RenderModel renderModel, RenderOptions options) {
        this.renderModel = renderModel;
        this.options = options;
        this.xPosition = options.sheetMetrics.pageSideMargin;
        this.yPosition = options.sheetMetrics.pageTopMargin;
    }

    public RenderModel getModel() {
        return renderModel;
    }

    public boolean hasNext() {
        return !endReached;
    }

    public RenderBlock next() {
        return renderModel.next(this);
    }

    public void increment(RenderBlock block) {
        RenderOptions.Build build = options.build;
        if (build == RenderOptions.Build.VERTICAL) {
            yPosition = block.getBounds().bottom;
        } else {
            xPosition = block.getBounds().right;
        }
        blockOffset++;
        if (block instanceof LineBlock) {
            LineBlock lineBlock = (LineBlock) block;
            if (lineBlock.beatCount > 0) {
                beatOffset += lineBlock.beatCount;
            } else if (lineBlock.barCount > 0) {
                barOffset += lineBlock.barCount;
            }
            if (barOffset == getModel().song.getBars(sequenceKey).size()) {
                endReached = true;
            }
        }

    }
}