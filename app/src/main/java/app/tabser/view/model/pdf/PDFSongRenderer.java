package app.tabser.view.model.pdf;

import java.io.IOException;

import app.tabser.view.render.AbstractSongRenderer;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.definition.SongRenderer;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.render.RenderModel;

public class PDFSongRenderer extends AbstractSongRenderer implements SongRenderer {

    private final PDFSheet sheet;

    public PDFSongRenderer(RenderModel model, PDFSheet sheet) {
        super(model);
        this.sheet = sheet;
    }

    @Override
    public void preparePage(RenderIterator iterator) {
        // TODO add header/footer, adjust viewPort
    }

    @Override
    public void newPage(RenderIterator iterator) {
        try {
            sheet.newPage(iterator);
            preparePage(iterator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sheet getSheet() {
        return sheet;
    }
}
