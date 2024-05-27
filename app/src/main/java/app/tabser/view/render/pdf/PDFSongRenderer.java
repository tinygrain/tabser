package app.tabser.view.render.pdf;

import java.io.IOException;

import app.tabser.view.viewmodel.RenderModel;
import app.tabser.view.render.Sheet;
import app.tabser.view.render.SongRenderer;
import app.tabser.view.render.AbstractSongRenderer;

public class PDFSongRenderer extends AbstractSongRenderer implements SongRenderer {

    private final PDFSheet sheet;

    public PDFSongRenderer(RenderModel model, PDFSheet sheet) {
        super(model);
        this.sheet = sheet;
    }

    @Override
    public void postProcessDocument(RenderModel.RenderIterator iterator) {
        // TODO add header/footer
    }

    @Override
    public void newPage(RenderModel.RenderIterator iterator) {
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
