package app.tabser.dom;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Bar {

    public enum SeparatorBar {
        NORMAL, BOLD, REPEAT_START, REPEAT_END, CONCLUSION
    }

    private Beat beat = Beat.INHERIT;
    /**
     * contains the notes (and breaks) of this bar.
     * the {@code Map<guitarStringIndex, Note} can be
     * used to define notes and chords.
     * <p>
     * Standard tuning: E=0, A=1, ...
     */
    private List<Map<Integer, Note>> notes = new ArrayList<>();
    private SeparatorBar separator = SeparatorBar.NORMAL;

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

    /**
     * Adds a note to the end or to an existing beatIndex
     *
     * @param stringIndex the instrument string index
     * @param fretNumber the fret number, 0 for open string, -1 for muted string
     * @param tuning the tuning
     * @param length the note length
     * @param beatIndex the beat index to use or insert
     * @return the inserted note
     */
    public Note addNote(int stringIndex, int fretNumber, Tuning tuning,
                        Length length, int beatIndex) {
        Map<Integer, Note> notes;
        /*
         * insert new beat in bar if not exists
         */
        if (beatIndex >= this.notes.size()) {
            notes = new HashMap<>();
            this.notes.add(notes);
        } else {
            notes = this.notes.get(beatIndex);
        }
        notes.put(stringIndex, new Note(stringIndex, fretNumber, tuning,
                length, stringIndex == -1,
                Expression.PLUCK));
        return notes.get(stringIndex);
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
