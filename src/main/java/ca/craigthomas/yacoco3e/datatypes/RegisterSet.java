package ca.craigthomas.yacoco3e.datatypes;


public class RegisterSet
{
    UnsignedByte a;
    UnsignedByte b;
    UnsignedWord d;
    UnsignedByte dp;
    UnsignedByte cc;
    UnsignedWord pc;
    UnsignedWord x;
    UnsignedWord y;
    UnsignedWord s;
    UnsignedWord u;

    boolean ccNegative;
    boolean ccOverflow;
    boolean ccZero;

    public UnsignedWord getPC() {
        return pc;
    }

    public UnsignedByte getDP() {
        return dp;
    }

    public boolean isCarrySet() {
        return (cc.getMasked((short) 0x01)) == 0x01;
    }

    public boolean isOverflowSet() {
        return (cc.getMasked((short) 0x02)) == 0x02;
    }

    public boolean isZeroSet() {
        return (cc.getMasked((short) 0x04)) == 0x04;
    }

    public boolean isNegativeSet() {
        return (cc.getMasked((short) 0x08)) == 0x08;
    }
}
