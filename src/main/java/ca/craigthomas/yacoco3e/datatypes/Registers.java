package ca.craigthomas.yacoco3e.datatypes;

/**
 * The Registers class contains the full set of CPU registers as used by the
 * CoCo 3. It also contains helper functions to modify the registers, as well
 * as common register flags.
 */
public class Registers
{
    /* Condition Code - Carry */
    public static final short CC_C = 0x01;

    /* Condition Code - Overflow */
    public static final short CC_V = 0x02;

    /* Condition Code - Zero */
    public static final short CC_Z = 0x04;

    /* Condition Code - Negative */
    public static final short CC_N = 0x08;

    UnsignedByte a;
    UnsignedByte b;
    UnsignedWord d;
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

    public UnsignedByte getDP() {
        return dp;
    }

    public boolean ccOverflowSet() {
        return cc.isMasked(CC_V);
    }

    public boolean ccZeroSet() {
        return cc.isMasked(CC_Z);
    }

    public boolean ccNegativeSet() {
        return cc.isMasked(CC_N);
    }
}
