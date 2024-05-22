package app.tabser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Note {
    public static final String C = "C";
    public static final String C_SHARP = "C#";
    public static final String D = "D";
    public static final String D_SHARP = "D#";
    public static final String E = "E";
    public static final String F = "F";
    public static final String F_SHARP = "F#";
    public static final String G = "G";
    public static final String G_SHARP = "G#";
    public static final String A = "A";
    public static final String A_SHARP = "A#";
    public static final String B = "B";
    public static final String X = "X";
    public static final String BREAK = "-";
    public static final String[] SCALE =
            {C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B};

    private boolean isBreak;
    private Length length;
    private Pitch pitch;
    private int stringIndex;
    private int fretNumber;
    private Expression expression;

    public Note() {
    }

    public Note(int stringIndex, int fretNumber, Tuning tuning, Length speed, boolean isBreak, Expression expression) {
        this.pitch = tuning.resolve(stringIndex, fretNumber);
        this.stringIndex = stringIndex;
        this.fretNumber = fretNumber;
        this.length = speed;
        this.isBreak = isBreak;
        this.expression = expression;
    }

    public Pitch getPitch() {
        return pitch;
    }

    public void setPitch(Pitch pitch) {
        this.pitch = pitch;
    }

    public int getStringIndex() {
        return stringIndex;
    }

    public void setStringIndex(int stringIndex) {
        this.stringIndex = stringIndex;
    }

    public int getFretNumber() {
        return fretNumber;
    }

    public void setFretNumber(int fretNumber) {
        this.fretNumber = fretNumber;
    }

    public Length getLength() {
        return length;
    }

    public void setLength(Length length) {
        this.length = length;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public void setBreak(boolean aBreak) {
        isBreak = aBreak;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @JsonIgnore
    public int position(Song.Clef clef) {
        if (clef == Song.Clef.TREBLE) {

        } else if (clef == Song.Clef.BASS) {

        } else if (clef == Song.Clef.TAB) {

        }
        return 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return stringIndex == note.stringIndex && fretNumber == note.fretNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pitch, stringIndex, fretNumber);
    }
}