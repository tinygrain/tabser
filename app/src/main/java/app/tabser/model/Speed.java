package app.tabser.model;

public enum Speed {
    FULL("1", 100000),
    HALF("1/2", 50000),
    QUARTER("1/4",25000),
    EIGHTH("1/8", 12500),
    SIXTEENTH("1/16", 6250),
    THIRTY_SECOND("1/32",3125);

    private final String signature;
    private final int cm;

    Speed(String signature, int cm) {
        this.signature = signature;
        this.cm = cm;
    }

    public String getSignature() {
        return signature;
    }

    public int getLength() {
        return cm;
    }
}
