package app.tabser.dom;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tuning {
    public static final Tuning STANDARD_BASS_4 = new Tuning("Standard Bass (4)",
            Pitch.of(Note.E, 1),
            Pitch.of(Note.A, 1),
            Pitch.of(Note.D, 2),
            Pitch.of(Note.G, 2));
    public static Tuning STANDARD_BASS_5 = new Tuning("Standard Bass (5)",
            Pitch.of(Note.B, 0),
            Pitch.of(Note.E, 1),
            Pitch.of(Note.A, 1),
            Pitch.of(Note.D, 2),
            Pitch.of(Note.G, 2));
    public static Tuning STANDARD_GUITAR_6 = new Tuning("Standard Guitar (6)",
            Pitch.of(Note.E, 2),
            Pitch.of(Note.A, 2),
            Pitch.of(Note.D, 3),
            Pitch.of(Note.G, 3),
            Pitch.of(Note.B, 3),
            Pitch.of(Note.E, 4));
    public static Tuning STANDARD_GUITAR_7 = new Tuning("Standard Guitar (7)",
            Pitch.of(Note.B, 1),
            Pitch.of(Note.E, 2),
            Pitch.of(Note.A, 2),
            Pitch.of(Note.D, 3),
            Pitch.of(Note.G, 3),
            Pitch.of(Note.B, 3),
            Pitch.of(Note.E, 4));

    public static final Tuning[] STANDARD_TUNINGS = {STANDARD_BASS_4, STANDARD_BASS_5,
            STANDARD_GUITAR_6, STANDARD_GUITAR_7};
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
