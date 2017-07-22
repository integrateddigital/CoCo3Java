package ca.craigthomas.yacoco3e.datatypes;

public class UnsignedByte
{
    short value;

    public UnsignedByte(short value) {
        this.value = value;
    }

    public short getShort() {
        return value;
    }

    /**
     * Returns a new UnsignedByte that is the twos compliment
     * value of the current byte.
     *
     * @return a new UnsignedByte with the twos compliment value
     */
    public UnsignedByte twosCompliment() {
        return new UnsignedByte((short) (~value + 1));
    }

    public short getMasked(short mask) {
        return (short) (value & mask);
    }
}
