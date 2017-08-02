/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import ca.craigthomas.yacoco3e.datatypes.*;

import java.util.function.Function;

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
    int executeInstruction(int operand) {
        int operationTicks = 0;
        MemoryResult memoryResult;

        switch (operand) {

            /* NEG - Negate M - Direct */
            case 0x00:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::negateM, memoryResult);
                setShortDesc("NEGM, DIR [%04X]", memoryResult);
                break;

            /* COM - Complement M - Direct */
            case 0x03:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::complimentM, memoryResult);
                setShortDesc("COMM, DIR [%04X]", memoryResult);
                break;

            /* LSR - Logical Shift Right - Direct */
            case 0x04:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::logicalShiftRight, memoryResult);
                setShortDesc("LSRM, DIR [%04X]", memoryResult);
                break;

            /* NEG - Negate M - Indirect */
            case 0x60:
                memoryResult = memory.getIndirect(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::negateM, memoryResult);
                setShortDesc("NEGM, IND [%04X]", memoryResult);
                break;

            /* COM - Complement M - Indirect */
            case 0x63:
                memoryResult = memory.getIndirect(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::complimentM, memoryResult);
                setShortDesc("COMM, IND [%04X]", memoryResult);
                break;

            /* LSR - Logical Shift Right - Indirect */
            case 0x64:
                memoryResult = memory.getIndirect(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::logicalShiftRight, memoryResult);
                setShortDesc("LSRM, IND [%04X]", memoryResult);
                break;

            /* NEG - Negate M - Extended */
            case 0x70:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::negateM, memoryResult);
                setShortDesc("NEGM, EXT [%04X]", memoryResult);
                break;

            /* COM - Complement M - Extended */
            case 0x73:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::complimentM, memoryResult);
                setShortDesc("COMM, EXT [%04X]", memoryResult);
                break;

            /* LSR - Logical Shift Right - Extended */
            case 0x74:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::logicalShiftRight, memoryResult);
                setShortDesc("LSRM, EXT [%04X]", memoryResult);
                break;

        }

        return operationTicks;
    }

    /**
     * Executes a byte function on the memory location M, and writes the
     * resultant byte back to the memory location.
     *
     * @param function the function to execute
     * @param memoryResult the MemoryResult where the address is located
     */
    public void executeByteFunctionM(Function<UnsignedByte, UnsignedByte> function,
                                     MemoryResult memoryResult) {
        UnsignedWord address = memoryResult.getResult();
        UnsignedByte tempByte = memory.readByte(address);
        tempByte = function.apply(tempByte);
        memory.writeByte(address, tempByte);
    }

    /**
     * Inverts all bits in the byte. Returns the complimented value as the
     * result.
     *
     * @param value the UnsignedByte to complement
     * @return the complimented value
     */
    public UnsignedByte complimentM(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(~(value.getShort()));
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_V));
        regs.cc.or(Registers.CC_C);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        return result;
    }

    /**
     * Applies the two's compliment value to the contents in the specified
     * memory address.
     *
     * @param value the byte to negate
     * @return the negated byte
     */
    public UnsignedByte negateM(UnsignedByte value) {
        UnsignedByte result = value.twosCompliment();
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_V | Registers.CC_C));
        regs.cc.or(result.isMasked(0x80) ? Registers.CC_V : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z | Registers.CC_N : 0);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        return result;
    }

    /**
     * Shifts all the bits in the byte to the left by one bit. Returns the
     * result of the operation, while impacting the condition code register.
     *
     * @param value the UnsignedByte to operate on
     */
    public UnsignedByte logicalShiftRight(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(value.getShort() >> 1);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_C));
        regs.cc.or(value.isMasked(0x1) ? Registers.CC_C : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        return result;
    }

    public void rotateRight(UnsignedWord address) {
        UnsignedByte tempByte = memory.readByte(address);

    }
}
