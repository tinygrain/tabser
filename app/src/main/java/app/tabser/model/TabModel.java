package app.tabser.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class TabModel {

    public static enum Clef {
        TREBLE, BASS
    }

    private Tuning tuning;
    private String title;
    private String artist;
    private String album;
    private int released;
    private int tempo;
    private Clef clef = Clef.BASS;
    private String instrument;
    private Beat beat;
    private ArrayList<Bar> bars = new ArrayList<>();

    public TabModel() {
    }

    public Tuning getTuning() {
        return tuning;
    }

    public void setTuning(Tuning tuning) {
        this.tuning = tuning;
    }

    public ArrayList<Bar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<Bar> bars) {
        this.bars = bars;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public Clef getClef() {
        return clef;
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Beat getBeat() {
        return beat;
    }

    public void setBeat(Beat beat) {
        this.beat = beat;
    }

    public boolean addNote(int string, int fret, Speed speed, int barIndex, int beatIndex, boolean autoBar) {
        Bar bar;
        if (bars.size() == 0 || beatIndex == -1 || barIndex == -1 || bars.size() < barIndex) {
            bar = new Bar();
            bars.add(bar);
        } else {
            bar = bars.get(barIndex);
        }
        bar.addNote(string, fret, tuning, speed, beatIndex);
        if (autoBar && bar.isComplete(beat)) {
            bar.setSeparator(Bar.SeparatorBar.NORMAL);
            bars.add(new Bar());
            return true;
        }
        return false;
    }

    public void clearNote(int selectedString, int barIndex, int beatIndex) {
        if (bars.size() > barIndex && bars.get(barIndex).getNotes().size() > beatIndex) {
            bars.get(barIndex).getNotes().get(beatIndex)[selectedString] = null;
            boolean allNull = true;
            for (int i = 0; i < bars.get(barIndex).getNotes().get(beatIndex).length; i++) {
                if (Objects.nonNull(bars.get(barIndex).getNotes().get(beatIndex)[i])) {
                    allNull = false;
                    break;
                }
            }
            if (allNull) {
                bars.get(barIndex).getNotes().remove(beatIndex);
            }
        }
    }

    public void addBreak(Speed speed) {

    }

    private void createBarIfNecessary() {

    }

    @Override
    public int hashCode() {
        return Objects.hash(tuning, artist, album, released, title, tempo, clef, instrument, tuning, bars);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return Objects.equals(this.hashCode(), obj.hashCode());
    }
}
