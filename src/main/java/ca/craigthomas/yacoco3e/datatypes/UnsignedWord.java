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
        set(value);
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
        int value = getInt();
        value += 1;
        return new UnsignedWord(value);
    }

    /**
     * Adds the specified value to the current word.
     *
     * @param value the additional value to add
     */
    public void add(int value) {
        int newValue = getInt();
        newValue += value;
        set(newValue);
    }

    /**
     * Returns true if the specified mask results in a non-zero value when
     * ANDed to the current value of the word.
     *
     * @param mask the bitmask to apply
     * @return True if applying the mask would result in a non-zero value
     */
    public boolean isMasked(int mask) {
        return (getInt() & mask) == mask;
    }

    /**
     * Sets the current value for the UnsignedWord.
     *
     * @param value the new value to set
     */
    public void set(int value) {
        highByte = new UnsignedByte((value & 0xFF00) >> 8);
        lowByte = new UnsignedByte(value & 0x00FF);
    }

    /**
     * Returns the integer representation of the word.
     *
     * @return the integer representation of the word
     */
    public int getInt() {
        int result = highByte.getShort();
        result = result << 8;
        result |= lowByte.getShort();
        return result;
    }

    /**
     * Returns the signed integer representation of the word. The integer is
     * negative if the high byte has the 8th bit set.
     *
     * @return the signed integer representation of the word
     */
    public int getSignedInt() {
        if (highByte.isNegative()) {
            int result = highByte.getShort() & 0x7F;
            result = result << 8;
            result |= lowByte.getShort();
            return -result;
        }
        return getInt();
    }

    /**
     * Creates a copy of the current unsigned word.
     *
     * @return a new copy of the UnsignedWord
     */
    public UnsignedWord copy() {
        return new UnsignedWord(getInt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnsignedWord that = (UnsignedWord) o;

        return this.getInt() == that.getInt();
    }

    @Override
    public int hashCode() {
        return getInt();
    }

    @Override
    public String toString() {
        return String.format("%02X%02X", highByte.getShort(), lowByte.getShort());
    }
}
