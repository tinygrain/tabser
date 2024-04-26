package app.tabser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Bar {

    public boolean isComplete(Beat b) {
        if (beat != Beat.INHERIT) {
            b = beat;
        }
        int bar = b.getBar();
        int count = 0;
        for (int i = 0; i < notes.size(); i++) {
            Note n = Arrays.asList(notes.get(i)).stream().findFirst().orElse(null);
            if (Objects.nonNull(n)) {
                switch (n.getSpeed()){
                    case FULL:
                        return true;
                    case HALF:
                        count += Speed.HALF.getCm();
                        break;
                    case QUARTER:
                        count += Speed.QUARTER.getCm();
                        break;
                    case EIGHTH:
                        count += Speed.EIGHTH.getCm();
                        break;
                    case SIXTEENTH:
                        count += Speed.SIXTEENTH.getCm();
                        break;
                    case THIRTY_SECOND:
                        count += Speed.THIRTY_SECOND.getCm();
                        break;
                }
            }
            if(count >= bar*Speed.QUARTER.getCm()) {
                return true;
            }
        }
        return false;
    }

    public enum SeparatorBar {
        NORMAL, BOLD, REPEAT_START, REPEAT_END,
    }

    private Beat beat = Beat.INHERIT;
    private ArrayList<Note[]> notes = new ArrayList<>();
    private SeparatorBar separator = null;

    public Bar() {
    }

    public Bar(Beat beat, ArrayList<Note[]> notes, SeparatorBar separator) {
        this.beat = beat;
        this.notes = notes;
        this.separator = separator;
    }

    public Beat getBeat() {
        return beat;
    }

    public void setBeat(Beat beat) {
        this.beat = beat;
    }

    public ArrayList<Note[]> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note[]> notes) {
        this.notes = notes;
    }

    public SeparatorBar getSeparator() {
        return separator;
    }

    public void setSeparator(SeparatorBar separator) {
        this.separator = separator;
    }

    @JsonIgnore
    public int size() {
        return notes.size();
    }

    public void addBreak(Speed speed) {
        // notes.add(new Break(speed));
        notes.add(new Note[]{new Note(-1, -1, null, speed, true)});
    }

    public void addNote(int string, int fret, Tuning tuning, Speed speed, int beat) {
        Note[] notes;
        if (beat == -1 || beat >= this.notes.size()) {
            notes = new Note[tuning.getStringCount()];
            this.notes.add(notes);
        } else {
            notes = this.notes.get(beat);
        }
        notes[string] = new Note(string, fret, tuning, speed, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return Objects.equals(beat, bar.beat) && Objects.equals(notes, bar.notes) && separator == bar.separator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beat, notes, separator);
    }
}
