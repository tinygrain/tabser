package app.tabser.view.render.pdf;

import java.io.IOException;

import app.tabser.view.render.AbstractSongRenderer;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.definition.SongRenderer;
import app.tabser.view.render.RenderIterator;
import app.tabser.view.model.RenderModel;

public class PDFSongRenderer extends AbstractSongRenderer implements SongRenderer {

    private final PDFSheet sheet;

    public PDFSongRenderer(RenderModel model, PDFSheet sheet) {
        super(model);
        this.sheet = sheet;
    }

    @Override
    public void postProcess(RenderIterator iterator) {
        // TODO add header/footer
    }

    @Override
    public void newPage(RenderIterator iterator) {
        try {
            sheet.newPage(iterator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sheet getSheet() {
        return sheet;
    }
}
