package ca.craigthomas.yacoco3e.datatypes;

public class MemoryResult
{
    int bytesConsumed;
    UnsignedWord result;

    public MemoryResult(int bytesConsumed, UnsignedWord result) {
        this.bytesConsumed = bytesConsumed;
        this.result = result;
    }

    int getBytesConsumed() {
        return bytesConsumed;
    }

    public UnsignedWord getResult() {
        return result;
    }
}
