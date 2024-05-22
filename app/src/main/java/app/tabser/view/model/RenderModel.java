package app.tabser.view.model;

import java.util.ArrayList;
import java.util.List;

import app.tabser.model.Song;
import app.tabser.view.model.blocks.PageBlock;
import app.tabser.view.model.definition.RenderBlock;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;

public class RenderModel {
    public static class SheetModel {
        public PageBlock headerBlock;
        public PageBlock footerBlock;
        public final List<RenderBlock> documentBlocks = new ArrayList<>();
    }

    public final Design design;
    public final Song song;
    public final Sheet sheet;
    public final SheetModel sheetModel = new SheetModel();

    public RenderModel(Song song, Sheet sheet, Design design) {
        this.song = song;
        this.sheet = sheet;
        this.design = design;
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
