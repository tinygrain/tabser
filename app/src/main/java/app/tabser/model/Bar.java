package app.tabser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Bar {

    public enum SeparatorBar {
        NORMAL, BOLD, REPEAT_START, REPEAT_END,
    }

    private Beat beat = Beat.INHERIT;
    private List<Map<Integer, Note>> notes = new ArrayList<>();
    private SeparatorBar separator = null;

    public Bar() {
    }

    public Bar(Beat beat, List<Map<Integer, Note>> notes, SeparatorBar separator) {
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

    public List<Map<Integer, Note>> getNotes() {
        return notes;
    }

    public void setNotes(List<Map<Integer, Note>> notes) {
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

    public void addBreak(Length length, int beat) {
        // notes.add(new Break(speed));
        //notes.put(0, new Note(-1, -1, null, length, true, null));
        addNote(-1, -1, null, length, beat);
    }

    public Note addNote(int string, int fret, Tuning tuning, Length length, int beat) {
        Map<Integer, Note> notes;
        if (beat == -1 || beat >= this.notes.size()) {
            notes = new HashMap<>();
            this.notes.add(notes);
        } else {
            notes = this.notes.get(beat);
        }
        notes.put(string, new Note(string, fret, tuning, length, string == -1, Expression.PLUCK));
        return notes.get(string);
    }

    public boolean isComplete(Beat b) {
        if (!Beat.INHERIT.equals(beat)) {
            b = beat;
        }
        int bar = b.getBar();
        int count = 0;
        for (int i = 0; i < notes.size(); i++) {
            Map<Integer, Note> noteArray = notes.get(i);
            Note n = null;
            for (Integer key : noteArray.keySet()) {
                Note x = noteArray.get(key);
                if (Objects.nonNull(x)) {
                    n = x;
                    break;
                }
            }
            if (Objects.nonNull(n)) {
                count += n.getLength().getLength();
            }
            if (count >= bar * Length.QUARTER.getLength()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return Objects.equals(beat, bar.beat) && Objects.equals(notes, bar.notes)
                && separator == bar.separator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beat, notes, separator);
    }
}
