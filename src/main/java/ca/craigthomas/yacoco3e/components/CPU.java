/*
 * Copyright (C) 2017 Craig Thomas
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
                executeByteFunctionM(this::negate, memoryResult);
                setShortDesc("NEGM, DIR [%04X]", memoryResult);
                break;

            /* COM - Complement M - Direct */
            case 0x03:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::compliment, memoryResult);
                setShortDesc("COMM, DIR [%04X]", memoryResult);
                break;

            /* LSR - Logical Shift Right - Direct */
            case 0x04:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::logicalShiftRight, memoryResult);
                setShortDesc("LSRM, DIR [%04X]", memoryResult);
                break;

            /* ROR - Rotate Right - Direct */
            case 0x06:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::rotateRight, memoryResult);
                setShortDesc("RORM, DIR [%04X]", memoryResult);
                break;

            /* ASR - Arithmetic Shift Right - Direct */
            case 0x07:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::arithmeticShiftRight, memoryResult);
                setShortDesc("ASRM, DIR [%04X]", memoryResult);
                break;

            /* ASL - Arithmetic Shift Left - Direct */
            case 0x08:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::arithmeticShiftLeft, memoryResult);
                setShortDesc("ASLM, DIR [%04X]", memoryResult);
                break;

            /* ROL - Rotate Left - Direct */
            case 0x09:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::rotateLeft, memoryResult);
                setShortDesc("ROLM, DIR [%04X]", memoryResult);
                break;

            /* DEC - Decrement - Direct */
            case 0x0A:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::decrement, memoryResult);
                setShortDesc("DECM, DIR [%04X]", memoryResult);
                break;

            /* NEG - Negate M - Indexed */
            case 0x60:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::negate, memoryResult);
                setShortDesc("NEGM, IND [%04X]", memoryResult);
                break;

            /* COM - Complement M - Indexed */
            case 0x63:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::compliment, memoryResult);
                setShortDesc("COMM, IND [%04X]", memoryResult);
                break;

            /* LSR - Logical Shift Right - Indexed */
            case 0x64:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::logicalShiftRight, memoryResult);
                setShortDesc("LSRM, IND [%04X]", memoryResult);
                break;

            /* ROR - Rotate Right - Indexed */
            case 0x66:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::rotateRight, memoryResult);
                setShortDesc("RORM, IND [%04X]", memoryResult);
                break;

            /* ASR - Arithmetic Shift Right - Indexed */
            case 0x67:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::arithmeticShiftRight, memoryResult);
                setShortDesc("ASRM, IND [%04X]", memoryResult);
                break;

            /* ASL - Arithmetic Shift Left - Indexed */
            case 0x68:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::arithmeticShiftLeft, memoryResult);
                setShortDesc("ASLM, IND [%04X]", memoryResult);
                break;

            /* ROL - Rotate Left - Indexed */
            case 0x69:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::rotateLeft, memoryResult);
                setShortDesc("ROLM, IND [%04X]", memoryResult);
                break;

            /* DEC - Decrement - Indexed */
            case 0x6A:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::decrement, memoryResult);
                setShortDesc("DECM, IND [%04X]", memoryResult);
                break;

            /* NEG - Negate M - Extended */
            case 0x70:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::negate, memoryResult);
                setShortDesc("NEGM, EXT [%04X]", memoryResult);
                break;

            /* COM - Complement M - Extended */
            case 0x73:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::compliment, memoryResult);
                setShortDesc("COMM, EXT [%04X]", memoryResult);
                break;

            /* LSR - Logical Shift Right - Extended */
            case 0x74:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::logicalShiftRight, memoryResult);
                setShortDesc("LSRM, EXT [%04X]", memoryResult);
                break;

            /* ROR - Rotate Right - Extended */
            case 0x76:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::rotateRight, memoryResult);
                setShortDesc("RORM, EXT [%04X]", memoryResult);
                break;

            /* ASR - Arithmetic Shift Right - Extended */
            case 0x77:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::arithmeticShiftRight, memoryResult);
                setShortDesc("ASRM, EXT [%04X]", memoryResult);
                break;

            /* ASL - Arithmetic Shift Left - Extended */
            case 0x78:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::arithmeticShiftLeft, memoryResult);
                setShortDesc("ASLM, EXT [%04X]", memoryResult);
                break;

            /* ROL - Rotate Left - Extended */
            case 0x79:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::rotateLeft, memoryResult);
                setShortDesc("ROLM, EXT [%04X]", memoryResult);
                break;

            /* DEC - Decrement - Extended */
            case 0x7A:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::decrement, memoryResult);
                setShortDesc("DECM, EXT [%04X]", memoryResult);
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
    public UnsignedByte compliment(UnsignedByte value) {
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
    public UnsignedByte negate(UnsignedByte value) {
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
     * The lowest bit of the byte is shifted into the condition code carry
     * bit.
     *
     * @param value the UnsignedByte to operate on
     * @return the shifted byte value
     */
    public UnsignedByte logicalShiftRight(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(value.getShort() >> 1);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_C));
        regs.cc.or(value.isMasked(0x1) ? Registers.CC_C : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        return result;
    }

    /**
     * Rotates the bits of a byte one place to the right. Will rotate the
     * carry bit into the highest bit of the byte if set.
     *
     * @param value the value to rotate right
     * @return the rotated value
     */
    public UnsignedByte rotateRight(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(value.getShort() >> 1);
        result.add(regs.ccCarrySet() ? 0x80 : 0x0);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_C));
        regs.cc.or(value.isMasked(0x1) ? Registers.CC_C : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        return result;
    }

    /**
     * Shifts the bits of a byte one place to the right. Will maintain a copy
     * of bit 7 in the 7th bit. Bit 0 will be shifted into the carry bit.
     *
     * @param value the value to shift right
     * @return the shifted value
     */
    public UnsignedByte arithmeticShiftRight(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(value.getShort() >> 1);
        result.add(value.isMasked(0x80) ? 0x80 : 0);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_C));
        regs.cc.or(value.isMasked(0x1) ? Registers.CC_C : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        return result;
    }

    /**
     * Shifts the bits of a byte one place to the left. Bit 0 will be filled
     * with a zero, while bit 7 will be shifted into the carry bit.
     *
     * @param value the value to shift left
     * @return the shifted value
     */
    public UnsignedByte arithmeticShiftLeft(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(value.getShort() << 1);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_V | Registers.CC_C));
        regs.cc.or(value.isMasked(0x80) ? Registers.CC_C : 0);
        regs.cc.or(value.isMasked(0xC0) ? Registers.CC_V : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        return result;
    }

    /**
     * Rotates the bits of a byte one place to the left. Will rotate the
     * carry bit into the lowest bit of the byte if set.
     *
     * @param value the value to rotate left
     * @return the rotated value
     */
    public UnsignedByte rotateLeft(UnsignedByte value) {
        UnsignedByte result = new UnsignedByte(value.getShort() << 1);
        result.add(regs.ccCarrySet() ? 0x1 : 0x0);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_C | Registers.CC_V));
        regs.cc.or(value.isMasked(0x80) ? Registers.CC_C : 0);
        regs.cc.or(value.isMasked(0xC0) ? Registers.CC_V : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        return result;
    }

    /**
     * Decrements the byte value by one.
     *
     * @param value the byte value to decrement
     * @return the decremented byte value
     */
    public UnsignedByte decrement(UnsignedByte value) {
        UnsignedByte result = regs.binaryAdd(value, new UnsignedByte(0xFF), false, false, false);
        regs.cc.and(~(Registers.CC_N | Registers.CC_Z | Registers.CC_V));
        regs.cc.or(value.isZero() ? Registers.CC_V : 0);
        regs.cc.or(result.isZero() ? Registers.CC_Z : 0);
        regs.cc.or(result.isNegative() ? Registers.CC_N : 0);
        return result;
    }
}
