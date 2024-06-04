package app.tabser.view.render;

import app.tabser.dom.Song;
import app.tabser.view.viewmodel.RenderModel;
import app.tabser.view.render.display.DisplaySheet;
import app.tabser.view.render.display.DisplaySongRenderer;
import app.tabser.view.render.pdf.PDFSheet;
import app.tabser.view.render.pdf.PDFSongRenderer;

public final class SongRendererFactory {
    public static SongRenderer create(Song song, Sheet sheet, Theme theme, RenderOptions options) {
        RenderModel model = new RenderModel(song, sheet, theme, options);
        if (sheet instanceof DisplaySheet) {
            return new DisplaySongRenderer(model, (DisplaySheet) sheet);
        } else if (sheet instanceof PDFSheet) {
            return new PDFSongRenderer(model, (PDFSheet) sheet);
        }
        throw new RuntimeException("Sheet implementation not recognized");
    }
}
