package app.tabser.view.render;

import app.tabser.view.SheetCursor;
import app.tabser.view.viewmodel.geometry.ViewPort;
import app.tabser.view.viewmodel.geometry.SheetMetrics;

public final class RenderOptions {

    /**
     * How to compile the sheet
     */
    public enum Compilation {
        /**
         * Compile as played
         */
        FULL,
        /**
         * Compile as written
         */
        MEDIUM,
        /**
         * Compile as Sequences
         */
        RAW,
        /**
         * Compile single Sequence
         */
        SEQUENCE,

    }

    /**
     * Layout direction
     */
    public enum Build {
        /**
         * Build line by line
         */
        VERTICAL,
        /**
         * Build single line
         */
        HORIZONTAL
    }

    public enum CursorType {
        /**
         * No visible cursor
         */
        NONE,
        /**
         * Cursor for item selection
         */
        ITEM,
        /**
         * Animated player cursor
         */
        PLAY
    }

    public enum SongPages {
        /**
         * Render viewport once
         */
        SINGLE,
        /**
         * Render Document
         */
        MULTI
    }

    public enum Form {
        /**
         * Tab only
         */
        COMPACT,
        /**
         * Notes and tab
         */
        FULL,
        /**
         *
         */
    }

    public static class SheetFormat {
        public static SheetFormat build(Element... format) {
            return new SheetFormat(format);
        }

        public enum Element {
            NOTES, SONG_TEXT, TAB
        }

        public final Element[] format;

        private SheetFormat(Element... format) {
            this.format = format;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
    public final ViewPort viewPort;
    public final SheetMetrics sheetMetrics;
    public final SheetCursor currentCursor;
    public final Form form;
    public final SheetFormat format;
    public final Build build;
    public final boolean cache;
    public final Compilation compilation;
    public final SongPages songPages;

    private RenderOptions(ViewPort viewPort, SheetMetrics sheetMetrics,
                          SheetCursor currentCursor, Form form, SheetFormat format,
                          Build build, boolean cache, Compilation compilation, SongPages songPages) {
        this.viewPort = viewPort;
        this.sheetMetrics = sheetMetrics;
        this.currentCursor = currentCursor;
        this.form = form;
        this.format = format;
        this.build = build;
        this.cache = cache;
        this.compilation = compilation;
        this.songPages = songPages;
    }

    public RenderOptions withCursor(SheetCursor currentCursor) {
        return new RenderOptions(viewPort, sheetMetrics, currentCursor, form, format, build, cache,
                compilation, songPages);
    }

    public static final class Builder {
        private ViewPort viewPort = new ViewPort();
        private SheetMetrics sheetMetrics;
        private SheetCursor currentCursor = new SheetCursor();
        private Form form = Form.FULL;
        private SheetFormat format = SheetFormat.build(SheetFormat.Element.NOTES,
                SheetFormat.Element.SONG_TEXT, SheetFormat.Element.TAB);
        private Build build = Build.VERTICAL;
        private boolean cache = false;
        private Compilation compilation = Compilation.SEQUENCE;
        private SongPages songPages = SongPages.SINGLE;

        private Builder() {
        }

        public Builder setViewPort(ViewPort viewPort) {
            this.viewPort = viewPort;
            return this;
        }

        public Builder setSheetMetrics(SheetMetrics sheetMetrics) {
            this.sheetMetrics = sheetMetrics;
            return this;
        }

        public Builder setCursor(SheetCursor cursor) {
            this.currentCursor = cursor;
            return this;
        }

        public Builder setForm(Form form) {
            this.form = form;
            return this;
        }

        public Builder setFormat(SheetFormat.Element... format) {
            this.format = SheetFormat.build(format);
            return this;
        }

        public Builder setBuild(Build build) {
            this.build = build;
            return this;
        }

        public Builder setCache(boolean cache) {
            this.cache = cache;
            return this;
        }

        public Builder setCompilation(Compilation compilation) {
            this.compilation = compilation;
            return this;
        }

        public Builder setSongPages(SongPages songPages) {
            this.songPages = songPages;
            return this;
        }



        public RenderOptions build() {
            return new RenderOptions(viewPort, sheetMetrics, currentCursor, form, format, build,
                    cache, compilation, songPages);
        }
    }
}
