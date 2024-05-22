package app.tabser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tuning {
    public static final Tuning STANDARD_4 = new Tuning("Standard (4)",
            Pitch.of(Note.E, 2),
            Pitch.of(Note.A, 2),
            Pitch.of(Note.D, 3),
            Pitch.of(Note.G, 3));
    public static Tuning STANDARD_5 = new Tuning("Standard (5)",
            Pitch.of(Note.E, 2),
            Pitch.of(Note.A, 2),
            Pitch.of(Note.D, 3),
            Pitch.of(Note.G, 3),
            Pitch.of(Note.B, 3));
    public static Tuning STANDARD_6 = new Tuning("Standard (6)",
            Pitch.of(Note.E, 2),
            Pitch.of(Note.A, 2),
            Pitch.of(Note.D, 3),
            Pitch.of(Note.G, 3),
            Pitch.of(Note.B, 3),
            Pitch.of(Note.E, 4));

    public static final Tuning[] STANDARD_TUNINGS = {STANDARD_4, STANDARD_5, STANDARD_6};
    private String name;
    private Pitch[] pitches;

    public Tuning() {
    }

    public Tuning(String name, Pitch... pitches) {
        this.name = name;
        this.pitches = pitches;
    }

    @JsonIgnore
    public int getStringCount() {
        return pitches.length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pitch[] getPitches() {
        return pitches;
    }

    public void setPitches(Pitch[] pitches) {
        this.pitches = pitches;
    }

    public  Pitch resolve(int string, int fret) {
        return pitches[string].plus(fret);
    }
}
