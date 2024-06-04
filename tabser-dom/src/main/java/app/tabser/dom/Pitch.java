package app.tabser.dom;

import java.util.Arrays;
import java.util.List;

public class Pitch {
    private String noteName;
    private int octave;
    private double frequency;

    public Pitch() {}

    private Pitch(String noteName, int octave, double frequency) {
        this.noteName = noteName;
        this.octave = octave;
        this.frequency = frequency;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public final Pitch plus(int steps) {
        if (steps == 0) {
            return this;
        }
        if(steps < 0) {
            return minus(steps * -1);
        }
        String note = this.noteName;
        int octave = this.octave;
        List<String> scale = Arrays.asList(Note.SCALE);
        int startScale = scale.indexOf(note);
        int destScale = startScale + steps;
        while (destScale >= scale.size()){
            octave++;
            destScale-=scale.size();
        }
        return of(scale.get(destScale), octave);
    }

    public final Pitch minus(int steps) {
        if (steps == 0) {
            return this;
        }
        if(steps < 0){
            return plus(steps * -1);
        }
        String note = this.noteName;
        int octave = this.octave;
        List<String> scale = Arrays.asList(Note.SCALE);
        int startScale = scale.indexOf(note);
        int destScale = startScale - steps;
        while (destScale < 0){
            octave--;
            destScale+=scale.size();
        }
        return of(scale.get(destScale), octave);
    }

    public static final Pitch of(String noteName, int octave) {
        switch (octave) {
            case 0:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 16.35d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 17.32d);
                    case Note.D:
                        return new Pitch(noteName, octave, 18.35d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 19.45d);
                    case Note.E:
                        return new Pitch(noteName, octave, 20.6d);
                    case Note.F:
                        return new Pitch(noteName, octave, 21.83d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 23.12d);
                    case Note.G:
                        return new Pitch(noteName, octave, 24.5d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 25.96d);
                    case Note.A:
                        return new Pitch(noteName, octave, 27.5d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 29.14d);
                    case Note.B:
                        return new Pitch(noteName, octave, 30.87d);
                }
                break;
            case 1:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 32.7d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 34.65d);
                    case Note.D:
                        return new Pitch(noteName, octave, 36.71d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 38.89d);
                    case Note.E:
                        return new Pitch(noteName, octave, 41.2d);
                    case Note.F:
                        return new Pitch(noteName, octave, 43.65d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 46.25d);
                    case Note.G:
                        return new Pitch(noteName, octave, 49d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 51.91d);
                    case Note.A:
                        return new Pitch(noteName, octave, 55);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 58.27d);
                    case Note.B:
                        return new Pitch(noteName, octave, 61.74d);
                }
                break;
            case 2:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 65.41d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 69.3d);
                    case Note.D:
                        return new Pitch(noteName, octave, 73.42d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 77.78d);
                    case Note.E:
                        return new Pitch(noteName, octave, 82.41d);
                    case Note.F:
                        return new Pitch(noteName, octave, 87.31d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 92.5d);
                    case Note.G:
                        return new Pitch(noteName, octave, 98d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 103.83d);
                    case Note.A:
                        return new Pitch(noteName, octave, 110d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 116.54d);
                    case Note.B:
                        return new Pitch(noteName, octave, 123.47d);
                }
                break;
            case 3:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 130.81d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 138.59);
                    case Note.D:
                        return new Pitch(noteName, octave, 146.83d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 155.56d);
                    case Note.E:
                        return new Pitch(noteName, octave, 164.81d);
                    case Note.F:
                        return new Pitch(noteName, octave, 174.61d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 185d);
                    case Note.G:
                        return new Pitch(noteName, octave, 196d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 207.65d);
                    case Note.A:
                        return new Pitch(noteName, octave, 220d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 233.08d);
                    case Note.B:
                        return new Pitch(noteName, octave, 246.94d);
                }
                break;
            case 4:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 261.63d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 277.18d);
                    case Note.D:
                        return new Pitch(noteName, octave, 293.66d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 311.13d);
                    case Note.E:
                        return new Pitch(noteName, octave, 329.63d);
                    case Note.F:
                        return new Pitch(noteName, octave, 349.23d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 369.99d);
                    case Note.G:
                        return new Pitch(noteName, octave, 392d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 415.3d);
                    case Note.A:
                        return new Pitch(noteName, octave, 440d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 466.16d);
                    case Note.B:
                        return new Pitch(noteName, octave, 493.88d);
                }
                break;
            case 5:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 523.25d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 554.37d);
                    case Note.D:
                        return new Pitch(noteName, octave, 587.33d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 622.25d);
                    case Note.E:
                        return new Pitch(noteName, octave, 659.25d);
                    case Note.F:
                        return new Pitch(noteName, octave, 698.46d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 739.99d);
                    case Note.G:
                        return new Pitch(noteName, octave, 783.99d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 830.61d);
                    case Note.A:
                        return new Pitch(noteName, octave, 880d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 932.33d);
                    case Note.B:
                        return new Pitch(noteName, octave, 987.77d);
                }
                break;
            case 6:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 1046.5d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 1108.73d);
                    case Note.D:
                        return new Pitch(noteName, octave, 1174.66d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 1244.51d);
                    case Note.E:
                        return new Pitch(noteName, octave, 1318.51d);
                    case Note.F:
                        return new Pitch(noteName, octave, 1396.91d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 1479.98d);
                    case Note.G:
                        return new Pitch(noteName, octave, 1567.98d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 1661.22d);
                    case Note.A:
                        return new Pitch(noteName, octave, 1760d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 1864.66d);
                    case Note.B:
                        return new Pitch(noteName, octave, 1975.53d);
                }
                break;
            case 7:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 2093d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 2217.46d);
                    case Note.D:
                        return new Pitch(noteName, octave, 2349.32d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 2489d);
                    case Note.E:
                        return new Pitch(noteName, octave, 2637d);
                    case Note.F:
                        return new Pitch(noteName, octave, 2793.83d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 2959.96d);
                    case Note.G:
                        return new Pitch(noteName, octave, 3135.96d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 3322.44d);
                    case Note.A:
                        return new Pitch(noteName, octave, 3520d);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 3729.31d);
                    case Note.B:
                        return new Pitch(noteName, octave, 3951d);
                }
                break;
            case 8:
                switch (noteName) {
                    case Note.C:
                        return new Pitch(noteName, octave, 4186d);
                    case Note.C_SHARP:
                        return new Pitch(noteName, octave, 4434.92d);
                    case Note.D:
                        return new Pitch(noteName, octave, 4698.63d);
                    case Note.D_SHARP:
                        return new Pitch(noteName, octave, 4978d);
                    case Note.E:
                        return new Pitch(noteName, octave, 5274d);
                    case Note.F:
                        return new Pitch(noteName, octave, 5587.65d);
                    case Note.F_SHARP:
                        return new Pitch(noteName, octave, 5919.91d);
                    case Note.G:
                        return new Pitch(noteName, octave, 6271.93d);
                    case Note.G_SHARP:
                        return new Pitch(noteName, octave, 6644.88);
                    case Note.A:
                        return new Pitch(noteName, octave, 7040);
                    case Note.A_SHARP:
                        return new Pitch(noteName, octave, 7458.62d);
                    case Note.B:
                        return new Pitch(noteName, octave, 7902.13d);
                }
                break;
        }
        throw new RuntimeException("Pitch could not be determined");
    }
}