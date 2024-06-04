package app.tabser.dom;

/**
 * Relative to count in bar
 *
 * @see Beat
 * @see Bar
 */
public enum Length {
    FULL("1", 512),
    HALF("1/2", 128),
    QUARTER("1/4",64),
    EIGHTH("1/8", 32),
    SIXTEENTH("1/16", 16),
    THIRTY_SECOND("1/32",8);

    private final String signature;
    private final int length;

    Length(String signature, int length) {
        this.signature = signature;
        this.length = length;
    }

    public String getSignature() {
        return signature;
    }

    public int getLength() {
        return length;
    }
}
