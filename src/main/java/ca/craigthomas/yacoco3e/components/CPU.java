/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import ca.craigthomas.yacoco3e.datatypes.MemoryResult;
import ca.craigthomas.yacoco3e.datatypes.RegisterSet;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;
import ca.craigthomas.yacoco3e.datatypes.UnsignedWord;

public class CPU
{
    RegisterSet registers;
    Memory memory;

    public CPU(RegisterSet registers, Memory memory) {
        this.registers = registers;
        this.memory = memory;
    }

    /**
     * Executes the instruction as indicated by the operand. Will return the
     * total number of ticks taken to execute the instruction.
     *
     * @param operand the operand containing the instruction to execute
     * @return the number of ticks taken up by the instruction
     */
    int executeInstruction(int operand) {
        int operationTicks = 0;
        UnsignedWord targetWord;
        MemoryResult memoryResult;

        switch (operand) {

            /* NEG - Negate M - Direct */
            case 0x00:
                memoryResult = memory.getDirect(registers);
                operationTicks = 6;
                negateM(memoryResult.getResult());
                break;

            /* NEG - Negate M - Indirect */
            case 0x60:

                break;

            /* NEG - Negate M - Extended */
            case 0x70:
                memoryResult = memory.getExtended(registers);
                operationTicks = 7;
                negateM(memoryResult.getResult());
                break;

        }

        return operationTicks;
    }


    public void negateM(UnsignedWord address) {
        UnsignedByte tempByte = memory.readByte(address).twosCompliment();
        
    }
}
