package app.tabser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Objects;

public class Bar {

    public boolean isComplete(Beat b) {
        if (beat != Beat.INHERIT) {
            b = beat;
        }
        int bar = b.getBar();
        int count = 0;
        for (int i = 0; i < notes.size(); i++) {
            Note[] noteArray = notes.get(i);
            Note n = null;
            for (Note x : noteArray) {
                if (Objects.nonNull(x)) {
                    n = x;
                    break;
                }
            }
            if (Objects.nonNull(n)) {
                count += n.getSpeed().getLength();
            }

            if (count >= bar * Speed.QUARTER.getLength()) {
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
        notes.add(new Note[]{new Note(-1, -1, null, speed, true, null)});
    }

    public void addNote(int string, int fret, Tuning tuning, Speed speed, int beat) {
        Note[] notes;
        if (beat == -1 || beat >= this.notes.size()) {
            notes = new Note[tuning.getStringCount()];
            this.notes.add(notes);
        } else {
            notes = this.notes.get(beat);
        }
        notes[string] = new Note(string, fret, tuning, speed, false, Expression.PLUCK);
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
