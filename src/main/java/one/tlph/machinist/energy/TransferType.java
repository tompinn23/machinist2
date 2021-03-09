package one.tlph.machinist.energy;

public enum TransferType {
    INOUT,
    IN,
    OUT,
    NONE;

    public TransferType next() {
        return values()[this.ordinal() % values().length];
    }
}
