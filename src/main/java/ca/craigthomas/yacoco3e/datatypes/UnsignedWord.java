/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.datatypes;

/**
 * The UnsignedWord class holds a single 16 bit value. The value is held in
 * a low byte and a high byte, which are represented as
 */
public class UnsignedWord
{
    /* The high 8 bits for the word */
    private UnsignedByte highByte;

    /* The low 8 bits for the word */
    private UnsignedByte lowByte;

    public UnsignedWord(int value) {
        highByte = new UnsignedByte((value & 0xFF00) >> 8);
        lowByte = new UnsignedByte(value & 0x00FF);
    }

    public UnsignedWord(UnsignedByte high, UnsignedByte low) {
        highByte = high;
        lowByte = low;
    }

    public UnsignedWord() {
        highByte = new UnsignedByte(0);
        lowByte = new UnsignedByte(0);
    }

    /**
     * The high function sets the high byte for the word.
     *
     * @param high the high byte for the word
     */
    public void high(UnsignedByte high) {
        highByte = high;
    }

    /**
     * The low function sets the low byte for the word.
     *
     * @param low the low byte for the word
     */
    public void low(UnsignedByte low) {
        lowByte = low;
    }

    /**
     * Returns the next byte along the sequence to read.
     *
     * @return the next byte to read
     */
    public UnsignedWord next() {
        int value = intValue();
        value += 1;
        return new UnsignedWord(value);
    }

    /**
     * Returns the integer representation of the word.
     *
     * @return the integer representation of the word
     */
    public int intValue() {
        int result = highByte.getShort();
        result = result << 8;
        result |= lowByte.getShort();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnsignedWord that = (UnsignedWord) o;

        return this.intValue() == that.intValue();
    }

    @Override
    public int hashCode() {
        int result = (int) highByte.getShort();
        result = 31 * result + (int) lowByte.getShort();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%02X%02X", highByte.getShort(), lowByte.getShort());
    }
}
