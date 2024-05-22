package app.tabser.model;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    /**
     * Songs that do not use the sequence/song part feature are stored
     * as a single "DEFFAULT" sequence.
     *
     * this is the key for the sequence map.
     */
    public static final String DEFAULT_SEQUENCE_NAME = "DEFAULT";
    /**
     * Intro, Verse, Chorus, Bridge, etc
     */
    private String title;
    /**
     * The data ;)
     */
    private List<Bar> bars = new ArrayList<>();


    public Sequence() {
        //title = DEFAULT_SEQUENCE_NAME;
    }

    public Sequence(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<Bar> bars) {
        this.bars = bars;
    }
}
