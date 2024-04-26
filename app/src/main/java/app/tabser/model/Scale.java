package app.tabser.model;

public enum Scale {
    C(Note.C),
    C_SHARP(Note.C_SHARP),
    D(Note.D),
    D_SHARP(Note.D_SHARP),
    E(Note.E),
    F(Note.F),
    F_SHARP(Note.F_SHARP),
    G(Note.G),
    G_SHARP(Note.G_SHARP),
    A(Note.A),
    A_SHARP(Note.A_SHARP),
    B(Note.B);
    private final String note;

    Scale(String name) {
        this.note = name;
    }

    public String getNote() {
        return note;
    }
}
