package one.tlph.machinist.energy;

public enum TransferType {
    INOUT,
    IN,
    OUT,
    NONE;



    public TransferType next() {
        int idx = this.ordinal() + 1;
        if(idx < values().length) {
            return values()[idx];
        }
        return INOUT;
    }

    @Override
    public String toString() {
        switch (this) {
            case IN:
                return "IN";
            case OUT:
                return "OUT";
            case INOUT:
                return "IN/OUT";
            case NONE:
                return "NONE";
        }
        return super.toString();
    }
}
