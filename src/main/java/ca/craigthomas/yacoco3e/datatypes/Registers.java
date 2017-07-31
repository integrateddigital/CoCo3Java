package ca.craigthomas.yacoco3e.datatypes;

/**
 * The Registers class contains the full set of CPU registers as used by the
 * CoCo 3. It also contains helper functions to modify the registers, as well
 * as common register flags.
 */
public class Registers
{
    /* Register X Flag */
    public static final int REG_X = 0;

    /* Register Y Flag */
    public static final int REG_Y = 1;

    /* Register U Flag */
    public static final int REG_U = 2;

    /* Register S Flag */
    public static final int REG_S = 3;

    /* Register Unknown Flag */
    public static final int REG_UNKNOWN = 99;

    /* Condition Code - Carry */
    public static final short CC_C = 0x01;

    /* Condition Code - Overflow */
    public static final short CC_V = 0x02;

    /* Condition Code - Zero */
    public static final short CC_Z = 0x04;

    /* Condition Code - Negative */
    public static final short CC_N = 0x08;

    /* Condition Code - Interrupt Request */
    public static final short CC_I = 0x10;

    /* Condition Code - Half Carry */
    public static final short CC_H = 0x20;

    /* Condition Code - Fast Interrupt Request */
    public static final short CC_F = 0x40;

    /* Condition Code - Everything */
    public static final short CC_E = 0x80;

    UnsignedByte a;
    UnsignedByte b;
    UnsignedByte dp;
    public UnsignedByte cc;
    UnsignedWord pc;
    UnsignedWord x;
    UnsignedWord y;
    UnsignedWord s;
    UnsignedWord u;

    public Registers() {
        cc = new UnsignedByte(0);
        dp = new UnsignedByte(0);
        pc = new UnsignedWord(0);
    }

    public UnsignedWord getPC() {
        return pc;
    }

    public void setPC(UnsignedWord pc) {
        this.pc = pc;
    }

    public void setDP(UnsignedByte dp) {
        this.dp = dp;
    }

    public UnsignedByte getDP() {
        return dp;
    }

    public UnsignedWord getX() {
        return x;
    }

    public UnsignedWord getY() {
        return y;
    }

    public UnsignedWord getU() {
        return u;
    }

    public UnsignedWord getS() {
        return s;
    }

    public UnsignedByte getA() {
        return a;
    }

    public UnsignedByte getB() {
        return b;
    }

    public UnsignedWord getD() {
        return new UnsignedWord(a, b);
    }

    /**
     * Performs a binary add of the two values, setting flags on the condition
     * code register where required.
     *
     * @param val1 the first value to add
     * @param val2 the second value to add
     * @param flagHalfCarry whether to flag half carries
     * @param flagCarry whether to flag full carries
     * @param flagOverflow whether to flag overflow
     *
     * @return the addition of the two values
     */
    public UnsignedWord binaryAdd(UnsignedWord val1, UnsignedWord val2,
                                  boolean flagHalfCarry, boolean flagCarry,
                                  boolean flagOverflow) {
        int value1 = val1.getInt();
        int value2 = val2.getInt();

        /* Check to see if a half carry occurred and we should flag it */
        if (flagHalfCarry) {
            UnsignedWord test = new UnsignedWord(value1 & 0xF);
            test.add(value2 & 0xF);
            if (test.isMasked(0x10)) {
                setCCHalfCarry();
            }
        }

        /* Check to see if a full carry occurred and we should flag it */
        if (flagCarry) {
            UnsignedWord test = new UnsignedWord(value1 & 0xFF);
            test.add(value2 & 0xFF);
            if (test.isMasked(0x100) && flagCarry) {
                setCCCarry();
            }
        }

        /* Check to see if overflow occurred and we should flag it */
        if (flagOverflow) {
            if ((value1 + value2) > 0xFFFF) {
                setCCOverflow();
            }
        }

        return new UnsignedWord(value1 + value2);
    }

    /**
     * Performs a binary add of the two values, setting flags on the condition
     * code register where required.
     *
     * @param val1 the first value to add
     * @param val2 the second value to add
     * @param flagHalfCarry whether to flag half carries
     * @param flagCarry whether to flag full carries
     * @param flagOverflow whether to flag overflow
     *
     * @return the addition of the two values
     */
    public UnsignedByte binaryAdd(UnsignedByte val1, UnsignedByte val2,
                                  boolean flagHalfCarry, boolean flagCarry,
                                  boolean flagOverflow) {
        int value1 = val1.getShort();
        int value2 = val2.getShort();

        /* Check for half carries */
        if (flagHalfCarry) {
            UnsignedByte test = new UnsignedByte(value1 & 0xF);
            test.add(value2 & 0xF);
            if (test.isMasked(0x10)) {
                setCCHalfCarry();
            }
        }

        /* Check for full carries */
        /* TODO: Check for correctness here */
        if (flagCarry) {
            UnsignedByte test = new UnsignedByte(value1 & 0xFF);
            test.add(value2 & 0xFF);
            if (test.isMasked(0x100)) {
                setCCCarry();
            }
        }

        /* Check for overflow */
        if (flagOverflow) {
            if ((value1 + value2) > 255) {
                setCCOverflow();
            }
        }

        return new UnsignedByte(value1 + value2);
    }

    public boolean ccCarrySet() {
        return cc.isMasked(CC_C);
    }

    public void setCCCarry() {
        cc.or(CC_C);
    }

    public boolean ccOverflowSet() {
        return cc.isMasked(CC_V);
    }

    public void setCCOverflow() {
        cc.or(CC_V);
    }

    public boolean ccZeroSet() {
        return cc.isMasked(CC_Z);
    }

    public void setCCZero() {
        cc.or(CC_Z);
    }

    public boolean ccNegativeSet() {
        return cc.isMasked(CC_N);
    }

    public void setCCNegative() {
        cc.or(CC_N);
    }

    public boolean ccInterruptSet() {
        return cc.isMasked(CC_I);
    }

    public void setCCInterrupt() {
        cc.or(CC_I);
    }

    public boolean ccHalfCarrySet() {
        return cc.isMasked(CC_H);
    }

    public void setCCHalfCarry() {
        cc.or(CC_H);
    }

    public boolean ccFastInterruptSet() {
        return cc.isMasked(CC_F);
    }

    public void setCCFastInterrupt() {
        cc.or(CC_F);
    }
}