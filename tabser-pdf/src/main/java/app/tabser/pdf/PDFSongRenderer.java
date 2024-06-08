package app.tabser.pdf;

import java.io.IOException;

import app.tabser.rendering.AbstractSongRenderer;
import app.tabser.rendering.RenderModel;
import app.tabser.rendering.Sheet;
import app.tabser.rendering.SongRenderer;

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
