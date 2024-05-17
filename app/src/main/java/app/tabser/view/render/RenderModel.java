package app.tabser.view.render;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import app.tabser.model.Song;
import app.tabser.view.model.blocks.AdvertisementBlock;
import app.tabser.view.model.blocks.LineBlock;
import app.tabser.view.model.blocks.PageBlock;
import app.tabser.view.model.blocks.RenderBlock;
import app.tabser.view.model.blocks.SongHeaderBlock;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;

public class RenderModel {
    public final Design design;
    public final Song song;
    public final Sheet sheet;

    public PageBlock pageBlock;
    public AdvertisementBlock advertisementBlock;
    public final SongHeaderBlock songHeaderBlock;
    private final List<LineBlock> documentLineBlocks = new ArrayList<>();

    public RenderModel(Song song, Sheet sheet, Design design) {
        this.song = song;
        this.sheet = sheet;
        this.design = design;
        this.songHeaderBlock = new SongHeaderBlock();
        //documentBlocks.add(songHeaderBlock);
    }

    public void invalidateHeader() {
        songHeaderBlock.invalidate();
    }

    public void invalidateLine(int i) {
        getLine(i).invalidate();
    }

    public void calculate(RenderIterator iterator) {
//        float fullWidth = iterator.getModel().getFullWidth();
        songHeaderBlock.calculate(iterator);
        for (; !iterator.endReached; iterator.lineOffset++) {
            LineBlock block;
            if (iterator.lineOffset < documentLineBlocks.size()) {
                block = documentLineBlocks.get(iterator.lineOffset);
            } else {
                block = new LineBlock(iterator);
                documentLineBlocks.add(block);
            }
            if (!block.isValid()) {
                block.calculate(iterator);
            }
        }
    }

    public int getYMin() {
        if (documentLineBlocks.size() > 0) {
            return documentLineBlocks.get(documentLineBlocks.size() - 1).getBounds().top;
        }
        return 0;
    }

    public LineBlock getLine(int i) {
        return documentLineBlocks.get(i);
    }

    public SheetMetrics getSheetMetrics() {
        return sheet.getMetrics();
    }

    public float getFullWidth() {
        Rect viewPort = sheet.getViewPort();
        return viewPort.right-viewPort.left-2* getSheetMetrics().xMargin;
    }

    public int getLineCount() {
        return documentLineBlocks.size();
    }
}
