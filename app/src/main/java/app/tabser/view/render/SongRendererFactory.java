package app.tabser.view.render;

import app.tabser.model.Song;
import app.tabser.view.model.definition.Design;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.definition.SongRenderer;
import app.tabser.view.model.display.DisplaySheet;
import app.tabser.view.model.display.DisplaySongRenderer;
import app.tabser.view.model.pdf.PDFSheet;
import app.tabser.view.model.pdf.PDFSongRenderer;

public final class SongRendererFactory {
    public static SongRenderer create(Song song, Sheet sheet, Design design) {
        RenderModel model = new RenderModel(song, sheet, design);
        if (sheet instanceof DisplaySheet) {
            return new DisplaySongRenderer(model, (DisplaySheet) sheet);
        } else if (sheet instanceof PDFSheet) {
            return new PDFSongRenderer(model, (PDFSheet) sheet);
        }
        throw new RuntimeException("Sheet implementation not recognized");
    }
}
