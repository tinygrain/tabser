package app.tabser.model;

import java.util.Objects;

public class Note  {
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

    private  boolean isBreak;
    private Length length;
    private Pitch pitch;
    private int string;
    private int fret;
    private Expression expression;

    public Note() {
    }

    public Note(int string, int fret, Tuning tuning, Length speed, boolean isBreak, Expression expression) {
        this.pitch = tuning.resolve(string, fret);
        this.string = string;
        this.fret = fret;
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

    public int getString() {
        return string;
    }

    public void setString(int string) {
        this.string = string;
    }

    public int getFret() {
        return fret;
    }

    public void setFret(int fret) {
        this.fret = fret;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return string == note.string && fret == note.fret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pitch, string, fret);
    }
}