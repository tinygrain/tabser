package app.tabser.dom;

import java.util.Objects;

public class Beat {
    public static final int INHERIT_BAR = -1;
    public static final int INHERIT_COUNT = -1;
    public static final Beat INHERIT = new Beat(INHERIT_BAR, INHERIT_COUNT);
    public static final Beat FOUR_FOURTH = new Beat(4, 4);
    public static final Beat THREE_FOURTH = new Beat(3, 4);
    private  int bar;
    private  int count;
    private int tempo = 100;

    public Beat() {
    }

    public Beat(int bar, int count) {
        this.bar = bar;
        this.count = count;
    }

    public int getBar() {
        return bar;
    }

    public void setBar(int bar) {
        this.bar = bar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beat beat = (Beat) o;
        return bar == beat.bar && count == beat.count && tempo == beat.tempo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bar, count, tempo);
    }
}
