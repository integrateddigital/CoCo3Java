package ca.craigthomas.yacoco3e.datatypes;

public class UnsignedWord
{
    short highByte;
    short lowByte;

    public UnsignedWord(int value) {
        highByte = (short) ((value & 0xFF00) >> 2);
        lowByte = (short) (value & 0x00FF);
    }

    public UnsignedWord(UnsignedByte high, UnsignedByte low) {
        highByte = high.getShort();
        lowByte = low.getShort();
    }

    public UnsignedWord(short high, short low) {
        highByte = high;
        lowByte = low;
    }

    public UnsignedWord() {
        highByte = 0;
        lowByte = 0;
    }

    public UnsignedByte high() {
        return new UnsignedByte(highByte);
    }

    public UnsignedByte low() {
        return new UnsignedByte(lowByte);
    }

    public void high(UnsignedByte high) {
        highByte = high.getShort();
    }

    public void low(UnsignedByte low) {
        lowByte = low.getShort();
    }

    public UnsignedWord next() {
        return new UnsignedWord(highByte, (short)(lowByte + 1));
    }

    public int intValue() {
        int result = highByte;
        result = result << 2;
        result |= lowByte;
        return result;
    }
}
