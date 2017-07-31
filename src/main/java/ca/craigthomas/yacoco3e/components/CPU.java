/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import ca.craigthomas.yacoco3e.datatypes.MemoryResult;
import ca.craigthomas.yacoco3e.datatypes.Registers;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;
import ca.craigthomas.yacoco3e.datatypes.UnsignedWord;

public class CPU
{
    Registers regs;
    Memory memory;
    String opShortDesc;
    String opLongDescription;

    public CPU(Registers registers, Memory memory) {
        this.regs = registers;
        this.memory = memory;
    }

    public void setShortDesc(String string, MemoryResult value) {
        opShortDesc = String.format(string, value.getResult().getInt());
    }

    /**
     * Executes the instruction as indicated by the operand. Will return the
     * total number of ticks taken to execute the instruction.
     *
     * @param operand the operand containing the instruction to execute
     * @return the number of ticks taken up by the instruction
     */
    int executeInstruction(int operand) throws IllegalIndexedPostbyteException {
        int operationTicks = 0;
        MemoryResult memoryResult;

        switch (operand) {

            /* NEG - Negate M - Direct */
            case 0x00:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                negateM(memoryResult.getResult());
                setShortDesc("NEGM, DIR [%04X]", memoryResult);
                break;

            /* NEG - Negate M - Indirect */
            case 0x60:

                break;

            /* NEG - Negate M - Extended */
            case 0x70:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                negateM(memoryResult.getResult());
                setShortDesc("NEGM, EXT [%04X]", memoryResult);
                break;

        }

        return operationTicks;
    }

    /**
     * Applies the two's compliment value to the contents in the specified
     * memory address. Stores the result back in the address.
     *
     * @param address the address to modify
     */
    public void negateM(UnsignedWord address) {
        UnsignedByte tempByte = memory.readByte(address).twosCompliment();
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_V | Registers.CC_C));
        regs.cc.or(tempByte.isMasked(0x80) ? Registers.CC_V : 0);
        regs.cc.or(tempByte.isZero() ? Registers.CC_Z | Registers.CC_N : 0);
        regs.cc.or(tempByte.isNegative() ? Registers.CC_N : 0);
        memory.writeByte(address, tempByte);
    }
}
