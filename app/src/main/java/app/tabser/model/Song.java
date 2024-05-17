package app.tabser.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.tabser.view.ToneGenerator;

public class Song {

    public enum Clef {
        TAB,

        BASS,

        TREBLE
    }

    /*
     * Song Configuration
     */
    private Clef clef = Clef.BASS;
    private Tuning tuning;
    private Beat beat;
    private String instrument;
    /*
     * Song Metadata
     */
    private int released;
    private String artist;
    private String title;
    private String album;
    /*
     * Song data
     */
    private Map<String, Sequence> sequenceMap = new HashMap<>();
    private List<String> sequenceOrder = new ArrayList<>();
    private List<Songtext> songText = new ArrayList<>();

    public Song() {
    }

    public Clef getClef() {
        return clef;
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }

    public Tuning getTuning() {
        return tuning;
    }

    public void setTuning(Tuning tuning) {
        this.tuning = tuning;
    }

    public Beat getBeat() {
        return beat;
    }

    public void setBeat(Beat beat) {
        this.beat = beat;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Map<String, Sequence> getSequenceMap() {
        return sequenceMap;
    }

    public void setSequenceMap(Map<String, Sequence> sequenceMap) {
        this.sequenceMap = sequenceMap;
    }

    public List<String> getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(List<String> sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public List<Songtext> getSongText() {
        return songText;
    }

    public void setSongText(List<Songtext> songText) {
        this.songText = songText;
    }

    public boolean addNote(int string, int fret, Length speed, int barIndex, int beatIndex,
                           boolean autoBar, boolean insert, String sequenceKey, Context context) {
        Sequence sequence = checkGetSequence(sequenceKey);
        ArrayList<Bar> bars = sequence.getBars();
        Bar bar;
        if (bars.size() == 0 || beatIndex == -1 || barIndex == -1 || bars.size() < barIndex) {
            bar = new Bar();
            bars.add(bar);
        } else {
            bar = bars.get(barIndex);
        }
        Note n = bar.addNote(string, fret, tuning, speed, beatIndex);
        ToneGenerator tg = new ToneGenerator(context);
        tg.play(n.getPitch().getFrequency(), 2);
        if (autoBar && bar.isComplete(beat)) {
            bar.setSeparator(Bar.SeparatorBar.NORMAL);
            bars.add(new Bar());
            return true;
        }
        return false;
    }
    public ArrayList<Bar> getBars(String sequenceKey) {
        return checkGetSequence(sequenceKey).getBars();
    }

    private Sequence checkGetSequence(String sequenceKey) {
        Sequence sequence;
        if (sequenceMap.containsKey(sequenceKey)) {
            sequence = sequenceMap.get(sequenceKey);
        } else {
            sequence = new Sequence();
            sequenceMap.put(sequenceKey, sequence);
        }
        return sequence;
    }

    public void clearNote(int selectedString, int barIndex, int beatIndex, String sequenceKey) {
        Sequence sequence = checkGetSequence(sequenceKey);
        ArrayList<Bar> bars = sequence.getBars();
        if (bars.size() > barIndex && bars.get(barIndex).getNotes().size() > beatIndex) {
            List<Map<Integer, Note>> notes = bars.get(barIndex).getNotes();
            Map<Integer, Note> note = notes.get(beatIndex);
            note.remove(selectedString);
            if (note.isEmpty()) {
                notes.remove(beatIndex);
            }
        }
    }

    public void addBreak(Length speed) {

    }

    public void addBar(String sequenceKey) {
        Sequence sequence = checkGetSequence(sequenceKey);
        ArrayList<Bar> bars = sequence.getBars();
        bars.add(new Bar(beat, new ArrayList<>(), null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song tabModel = (Song) o;
        return released == tabModel.released && clef == tabModel.clef && Objects.equals(tuning, tabModel.tuning) && Objects.equals(beat, tabModel.beat) && Objects.equals(instrument, tabModel.instrument) && Objects.equals(artist, tabModel.artist) && Objects.equals(title, tabModel.title) && Objects.equals(album, tabModel.album) && Objects.equals(sequenceMap, tabModel.sequenceMap) && Objects.equals(sequenceOrder, tabModel.sequenceOrder) && Objects.equals(songText, tabModel.songText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clef, tuning, beat, instrument, released, artist, title, album, sequenceMap, sequenceOrder, songText);
    }
}
