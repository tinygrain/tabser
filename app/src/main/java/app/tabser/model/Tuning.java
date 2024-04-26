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

    public static final Tuning[] TUNINGS = {STANDARD_4, STANDARD_5, STANDARD_6};
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


/*
    private static Pitch resolveStandard(int string, int fret) {
        switch (string) {
            case Note.STRING_E:
                switch (fret) {
                    case 0:
                        return Pitch.of("E", 2);
                    case 1:
                        return Pitch.of("F", 2);
                    case 2:
                        return Pitch.of("F#", 2);
                    case 3:
                        return Pitch.of("G", 2);
                    case 4:
                        return Pitch.of("G#", 2);
                    case 5:
                        return Pitch.of("A", 2);
                    case 6:
                        return Pitch.of("A#", 2);
                    case 7:
                        return Pitch.of("B", 2);
                    case 8:
                        return Pitch.of("C", 3);
                    case 9:
                        return Pitch.of("C#", 3);
                    case 10:
                        return Pitch.of("D", 3);
                    case 11:
                        return Pitch.of("D#", 3);
                    case 12:
                        return Pitch.of("E", 3);
                    case 13:
                        return Pitch.of("F", 3);
                    case 14:
                        return Pitch.of("F#", 3);
                    case 15:
                        return Pitch.of("G", 3);
                    case 16:
                        return Pitch.of("G#", 3);
                    case 17:
                        return Pitch.of("A", 3);
                    case 18:
                        return Pitch.of("A#", 3);
                    case 19:
                        return Pitch.of("B", 3);
                    case 20:
                        return Pitch.of("C", 4);
                    case 21:
                        return Pitch.of("C#", 4);
                    case 22:
                        return Pitch.of("D", 4);
                    case 23:
                        return Pitch.of("D#", 4);
                    case 24:
                        return Pitch.of("E", 4);
                }
                break;
            case Note.STRING_A:
                switch (fret) {
                    case 0:
                        return Pitch.of("A", 2);
                    case 1:
                        return Pitch.of("A#", 2);
                    case 2:
                        return Pitch.of("B", 2);
                    case 3:
                        return Pitch.of("C", 3);
                    case 4:
                        return Pitch.of("C#", 3);
                    case 5:
                        return Pitch.of("D", 3);
                    case 6:
                        return Pitch.of("D#", 3);
                    case 7:
                        return Pitch.of("E", 3);
                    case 8:
                        return Pitch.of("F", 3);
                    case 9:
                        return Pitch.of("F#", 3);
                    case 10:
                        return Pitch.of("G", 3);
                    case 11:
                        return Pitch.of("G#", 3);
                    case 12:
                        return Pitch.of("A", 3);
                    case 13:
                        return Pitch.of("A#", 3);
                    case 14:
                        return Pitch.of("B", 3);
                    case 15:
                        return Pitch.of("C", 4);
                    case 16:
                        return Pitch.of("C#", 4);
                    case 17:
                        return Pitch.of("D", 4);
                    case 18:
                        return Pitch.of("D#", 4);
                    case 19:
                        return Pitch.of("E", 4);
                    case 20:
                        return Pitch.of("F", 4);
                    case 21:
                        return Pitch.of("F#", 4);
                    case 22:
                        return Pitch.of("G", 4);
                    case 23:
                        return Pitch.of("G#", 4);
                    case 24:
                        return Pitch.of("A", 4);
                }
                break;
            case Note.STRING_D:
                switch (fret) {
                    case 0:
                        return Pitch.of("D", 3);
                    case 1:
                        return Pitch.of("D#", 2);
                    case 2:
                        return Pitch.of("E", 2);
                    case 3:
                        return Pitch.of("F", 3);
                    case 4:
                        return Pitch.of("F#", 3);
                    case 5:
                        return Pitch.of("G", 3);
                    case 6:
                        return Pitch.of("G#", 3);
                    case 7:
                        return Pitch.of("A", 3);
                    case 8:
                        return Pitch.of("A#", 3);
                    case 9:
                        return Pitch.of("B", 3);
                    case 10:
                        return Pitch.of("C", 4);
                    case 11:
                        return Pitch.of("C#", 4);
                    case 12:
                        return Pitch.of("D", 4);
                    case 13:
                        return Pitch.of("D#", 4);
                    case 14:
                        return Pitch.of("E", 4);
                    case 15:
                        return Pitch.of("F", 4);
                    case 16:
                        return Pitch.of("F#", 4);
                    case 17:
                        return Pitch.of("G", 4);
                    case 18:
                        return Pitch.of("G#", 4);
                    case 19:
                        return Pitch.of("A", 4);
                    case 20:
                        return Pitch.of("A#", 4);
                    case 21:
                        return Pitch.of("B", 4);
                    case 22:
                        return Pitch.of("C", 5);
                    case 23:
                        return Pitch.of("C#", 5);
                    case 24:
                        return Pitch.of("D", 5);
                }
                break;
            case Note.STRING_G:
                switch (fret) {
                    case 0:
                        return Pitch.of("G", 3);
                    case 1:
                        return Pitch.of("G#", 3);
                    case 2:
                        return Pitch.of("A", 3);
                    case 3:
                        return Pitch.of("A#", 3);
                    case 4:
                        return Pitch.of("B", 3);
                    case 5:
                        return Pitch.of("C", 4);
                    case 6:
                        return Pitch.of("C#", 4);
                    case 7:
                        return Pitch.of("D", 4);
                    case 8:
                        return Pitch.of("D#", 4);
                    case 9:
                        return Pitch.of("E", 4);
                    case 10:
                        return Pitch.of("F", 4);
                    case 11:
                        return Pitch.of("F#", 4);
                    case 12:
                        return Pitch.of("G", 4);
                    case 13:
                        return Pitch.of("G#", 4);
                    case 14:
                        return Pitch.of("A", 4);
                    case 15:
                        return Pitch.of("A#", 4);
                    case 16:
                        return Pitch.of("B", 4);
                    case 17:
                        return Pitch.of("C", 5);
                    case 18:
                        return Pitch.of("C#", 5);
                    case 19:
                        return Pitch.of("D", 5);
                    case 20:
                        return Pitch.of("D#", 5);
                    case 21:
                        return Pitch.of("E", 5);
                    case 22:
                        return Pitch.of("F", 5);
                    case 23:
                        return Pitch.of("F#", 5);
                    case 24:
                        return Pitch.of("G", 5);
                }
                break;
            case Note.STRING_B:
                switch (fret) {
                    case 0:
                        return Pitch.of("B", 3);
                    case 1:
                        return Pitch.of("C", 4);
                    case 2:
                        return Pitch.of("C#", 4);
                    case 3:
                        return Pitch.of("D", 4);
                    case 4:
                        return Pitch.of("D#", 4);
                    case 5:
                        return Pitch.of("E", 4);
                    case 6:
                        return Pitch.of("F", 4);
                    case 7:
                        return Pitch.of("F#", 4);
                    case 8:
                        return Pitch.of("G", 4);
                    case 9:
                        return Pitch.of("G#", 4);
                    case 10:
                        return Pitch.of("A", 4);
                    case 11:
                        return Pitch.of("A#", 4);
                    case 12:
                        return Pitch.of("B", 4);
                    case 13:
                        return Pitch.of("C", 5);
                    case 14:
                        return Pitch.of("C#", 5);
                    case 15:
                        return Pitch.of("D", 5);
                    case 16:
                        return Pitch.of("D#", 5);
                    case 17:
                        return Pitch.of("E", 5);
                    case 18:
                        return Pitch.of("F", 5);
                    case 19:
                        return Pitch.of("F#", 5);
                    case 20:
                        return Pitch.of("G", 5);
                    case 21:
                        return Pitch.of("G#", 5);
                    case 22:
                        return Pitch.of("A", 5);
                    case 23:
                        return Pitch.of("A#", 5);
                    case 24:
                        return Pitch.of("B", 5);
                }
                break;
            case Note.STRING_E1:
                switch (fret) {
                    case 0:
                        return Pitch.of("E", 4);
                    case 1:
                        return Pitch.of("F", 4);
                    case 2:
                        return Pitch.of("F#", 4);
                    case 3:
                        return Pitch.of("G", 4);
                    case 4:
                        return Pitch.of("G#", 4);
                    case 5:
                        return Pitch.of("A", 4);
                    case 6:
                        return Pitch.of("A#", 4);
                    case 7:
                        return Pitch.of("B", 4);
                    case 8:
                        return Pitch.of("C", 5);
                    case 9:
                        return Pitch.of("C#", 5);
                    case 10:
                        return Pitch.of("D", 5);
                    case 11:
                        return Pitch.of("D#", 5);
                    case 12:
                        return Pitch.of("E", 5);
                    case 13:
                        return Pitch.of("F", 5);
                    case 14:
                        return Pitch.of("F#", 5);
                    case 15:
                        return Pitch.of("G", 5);
                    case 16:
                        return Pitch.of("G#", 5);
                    case 17:
                        return Pitch.of("A", 5);
                    case 18:
                        return Pitch.of("A#", 5);
                    case 19:
                        return Pitch.of("B", 5);
                    case 20:
                        return Pitch.of("C", 6);
                    case 21:
                        return Pitch.of("C#", 6);
                    case 22:
                        return Pitch.of("D", 6);
                    case 23:
                        return Pitch.of("D#", 6);
                    case 24:
                        return Pitch.of("E", 6);
                }
                break;
        }
        throw new RuntimeException("Could not resolve note");
    }
 */
}
