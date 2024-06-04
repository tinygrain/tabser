package app.tabser.view.viewmodel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import app.tabser.dom.Sequence;
import app.tabser.dom.Song;
import app.tabser.view.viewmodel.blocks.LineBlock;
import app.tabser.view.viewmodel.blocks.PageBlock;
import app.tabser.view.viewmodel.blocks.SongHeaderBlock;
import app.tabser.view.viewmodel.blocks.RenderBlock;
import app.tabser.view.render.Theme;
import app.tabser.view.render.Sheet;
import app.tabser.view.viewmodel.geometry.SheetMetrics;
import app.tabser.view.render.RenderOptions;

public class RenderModel implements Iterable<RenderBlock> {

    public final class RenderIterator implements Iterator<RenderBlock> {
        //    @Deprecated
//    public ArrayList<SheetView.ModelCursor> cursorPositions;
        /*
         * Song model cursors
         */
        public String sequenceKey = Sequence.DEFAULT_SEQUENCE_NAME;
        public int barOffset;
        public int beatOffset;
        @Deprecated
        public int lineOffset;
        /*
         * Song model return values
         */
//        public int calculatedBarCount;
//        public int calculatedBeatCount;
        /*
         * draw / page coordinate cursors
         */
        public float xPosition;
        public float yPosition;
        /*
         * Block index / size
         */
        public int blockOffset;
        /*
         * page index / size
         */
        public int pageOffset;
        /*
         * limit indicators
         */
        private boolean endReached;
        /**
         * Render options
         */
        public final RenderOptions options;

        private RenderIterator(RenderOptions options) {
            this.options = options;
            this.xPosition = options.sheetMetrics.pageSideMargin;
            this.yPosition = options.sheetMetrics.pageTopMargin;
        }

        public RenderModel getModel() {
            return RenderModel.this;
        }

        @Override
        public boolean hasNext() {
            return !endReached;
        }

        @Override
        public RenderBlock next() {
            RenderBlock block = null;
            if (sheetModel.documentBlocks.size() == 0) {
                SongHeaderBlock headerBlock = new SongHeaderBlock(options.viewPort);
                sheetModel.documentBlocks.add(headerBlock);
                block = headerBlock;
            } else if (sheetModel.documentBlocks.size() == 1) {
                LineBlock lineBlock = new LineBlock(this);
                sheetModel.documentBlocks.add(lineBlock);
                endReached = true;
                block = lineBlock;
            } else if (sheetModel.documentBlocks.size() > blockOffset) {
                block = sheetModel.documentBlocks.get(blockOffset);
            } else if (barOffset < song.getBars(sequenceKey).size()
                    && beatOffset < song.getBars(sequenceKey).get(barOffset).size()) {
                LineBlock lineBlock = new LineBlock(this);
                sheetModel.documentBlocks.add(lineBlock);
                block = lineBlock;
            } else {
                // TODO initialize other blocks
            }
            if (Objects.nonNull(block)) {
                increment(block);
                return block;
            }
            throw new NoSuchElementException();
        }

        private void increment(RenderBlock block) {
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

//        public RenderContext getRenderContext() {
//            return null;
//        }
    }

    @NonNull
    @Override
    public RenderIterator iterator() {
        return new RenderIterator(options);
    }

    public static class SheetModel {
        public PageBlock headerBlock;
        public PageBlock footerBlock;
        public final List<RenderBlock> documentBlocks = new ArrayList<>();
    }

    public final Theme theme;
    public final Song song;
    public final Sheet sheet;
    public final SheetModel sheetModel = new SheetModel();
    private final RenderOptions options;

    public RenderModel(Song song, Sheet sheet, Theme theme, RenderOptions options) {
        this.song = song;
        this.sheet = sheet;
        this.theme = theme;
        this.options = options;
    }

    //    public void calculate(RenderIterator iterator) {
////        float fullWidth = iterator.getModel().getFullWidth();
//        if (!sheetModel.songHeaderBlock.isValid()) {
//            sheetModel.songHeaderBlock.calculate(iterator);
//        }
//        for (; !iterator.endReached; iterator.lineOffset++) {
//            LineBlock block;
//            if (iterator.lineOffset < sheetModel.documentLineBlocks.size()) {
//                block = sheetModel.documentLineBlocks.get(iterator.lineOffset);
//            } else {
//                block = new LineBlock(iterator);
//                sheetModel.documentLineBlocks.add(block);
//            }
//            if (!block.isValid()) {
//                block.calculate(iterator);
//            }
//        }
//    }

    public int getYMin() {
        if (sheetModel.documentBlocks.size() > 0) {
            return -sheetModel.documentBlocks.get(sheetModel.documentBlocks.size() - 1).getBounds().top;
        }
        return 0;
    }

    public SheetMetrics getSheetMetrics() {
        return sheet.getMetrics();
    }

    public float getBlockWidth() {
        return getSheetMetrics().viewPort.area.right - getSheetMetrics().pageSideMargin * 2f;
    }

    public int getLineCount() {
        return sheetModel.documentBlocks.size();
    }


}
