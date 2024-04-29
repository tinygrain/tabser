package app.tabser.model;

import java.util.ArrayList;

public class Sequence {

    public static final String DEFAULT_HIDDEN_SEQUENCE_NAME = "</DEFAULT-HIDDEN/>";
    private String title;
    private ArrayList<Bar> bars = new ArrayList<>();

    public Sequence() {
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

    public ArrayList<Bar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<Bar> bars) {
        this.bars = bars;
    }
}
