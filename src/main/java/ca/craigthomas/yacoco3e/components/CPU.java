/*
 * Copyright (C) 2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import ca.craigthomas.yacoco3e.datatypes.*;

import java.util.function.Function;

public class CPU
{
    RegisterSet regs;
    Memory memory;
    String opShortDesc;
    String opLongDescription;
    public final static UnsignedWord SWI3 = new UnsignedWord(0xFFF2);

    public CPU(RegisterSet registerSet, Memory memory) {
        this.regs = registerSet;
        this.memory = memory;
    }

    public void setShortDesc(String string, MemoryResult value) {
        if (value != null) {
            opShortDesc = String.format(string, value.getResult().getInt());
        } else {
            opShortDesc = string;
        }
    }

    /**
     * Executes the instruction as indicated by the operand. Will return the
     * total number of ticks taken to execute the instruction.
     *
     * @return the number of ticks taken up by the instruction
     */
    int executeInstruction() {
        int operationTicks = 0;
        MemoryResult memoryResult;
        UnsignedByte operand = memory.getPCByte(regs);
        regs.incrementPC();

        switch (operand.getShort()) {

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

            /* INC - Increment - Direct */
            case 0x0C:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::increment, memoryResult);
                setShortDesc("INCM, DIR [%04X]", memoryResult);
                break;

            /* TST - Test - Direct */
            case 0x0D:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::test, memoryResult);
                setShortDesc("TSTM, DIR [%04X]", memoryResult);
                break;

            /* JMP - Jump - Direct */
            case 0x0E:
                memoryResult = memory.getDirect(regs);
                operationTicks = 3;
                jump(memoryResult.getResult());
                setShortDesc("JMP, DIR [%04X]", memoryResult);
                break;

            /* CLR - Clear - Direct */
            case 0x0F:
                memoryResult = memory.getDirect(regs);
                operationTicks = 6;
                executeByteFunctionM(this::clear, memoryResult);
                setShortDesc("CLRM, DIR [%04X]", memoryResult);
                break;

            /* Extended Opcodes */
            case 0x10:
            {
                UnsignedByte extendedOp = memory.getPCByte(regs);
                regs.incrementPC();

                switch(extendedOp.getShort()) {

                    /* LBRN - Long Branch Never */
                    case 0x21:
                        memoryResult = memory.getImmediate(regs);
                        operationTicks = 5;
                        setShortDesc("LBRN, REL [%04X]", memoryResult);
                        break;

                    /* LBHI - Long Branch on Higher */
                    case 0x22:
                        memoryResult = memory.getImmediate(regs);
                        if (!regs.ccCarrySet() && !regs.ccZeroSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBHI, REL [%04X]", memoryResult);
                        break;

                    /* LBLS - Long Branch on Lower or Same */
                    case 0x23:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccCarrySet() || regs.ccZeroSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBLS, REL [%04X]", memoryResult);
                        break;

                    /* LBCC - Long Branch on Carry Clear */
                    case 0x24:
                        memoryResult = memory.getImmediate(regs);
                        if (!regs.ccCarrySet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBCC, REL [%04X]", memoryResult);
                        break;

                    /* LBCS - Long Branch on Carry Set */
                    case 0x25:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccCarrySet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBCS, REL [%04X]", memoryResult);
                        break;

                    /* LBNE - Long Branch on Not Equal */
                    case 0x26:
                        memoryResult = memory.getImmediate(regs);
                        if (!regs.ccZeroSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBNE, REL [%04X]", memoryResult);
                        break;

                    /* LBNE - Long Branch on Equal */
                    case 0x27:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccZeroSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBEQ, REL [%04X]", memoryResult);
                        break;

                    /* LBVC - Long Branch on Overflow Clear */
                    case 0x28:
                        memoryResult = memory.getImmediate(regs);
                        if (!regs.ccOverflowSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBVC, REL [%04X]", memoryResult);
                        break;

                    /* LBVS - Long Branch on Overflow Set */
                    case 0x29:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccOverflowSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBVS, REL [%04X]", memoryResult);
                        break;

                    /* LBPL - Long Branch on Plus */
                    case 0x2A:
                        memoryResult = memory.getImmediate(regs);
                        if (!regs.ccNegativeSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBPL, REL [%04X]", memoryResult);
                        break;

                    /* LBMI - Long Branch on Minus */
                    case 0x2B:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccNegativeSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBMI, REL [%04X]", memoryResult);
                        break;

                    /* LBGE - Long Branch on Greater Than or Equal to Zero */
                    case 0x2C:
                        memoryResult = memory.getImmediate(regs);
                        if (!regs.ccNegativeSet() ^ !regs.ccOverflowSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBGE, REL [%04X]", memoryResult);
                        break;

                    /* LBLT - Long Branch on Less Than or Equal to Zero */
                    case 0x2D:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccNegativeSet() ^ regs.ccOverflowSet()) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBLT, REL [%04X]", memoryResult);
                        break;

                    /* LBGT - Long Branch on Greater Than Zero */
                    case 0x2E:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccZeroSet() && (regs.ccNegativeSet() ^ regs.ccOverflowSet())) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBGT, REL [%04X]", memoryResult);
                        break;

                    /* LBLE - Long Branch on Less Than Zero */
                    case 0x2F:
                        memoryResult = memory.getImmediate(regs);
                        if (regs.ccZeroSet() || (regs.ccNegativeSet() ^ regs.ccOverflowSet())) {
                            branchLong(memoryResult.getResult());
                            operationTicks = 6;
                        } else {
                            operationTicks = 5;
                        }
                        setShortDesc("LBLE, REL [%04X]", memoryResult);
                        break;

                    /* SWI3 - Software Interrupt 3 */
                    case 0x3F:
                        softwareInterrupt(SWI3);
                        operationTicks = 19;
                        setShortDesc("SWI3", null);
                        break;

                    /* CMPD - Compare D - Immediate */
                    case 0x83:
                        memoryResult = memory.getImmediate(regs);
                        compareWord(regs.getD(), memoryResult.getResult());
                        operationTicks = 5;
                        setShortDesc("CMPD, IMM", null);
                        break;

                    /* CMPY - Compare Y - Immediate */
                    case 0x8C:
                        memoryResult = memory.getImmediate(regs);
                        compareWord(regs.getY(), memoryResult.getResult());
                        operationTicks = 5;
                        setShortDesc("CMPY, IMM", null);
                        break;

                    /* CMPD - Compare D - Direct */
                    case 0x93:
                        memoryResult = memory.getDirect(regs);
                        compareWord(regs.getD(), memory.readWord(memoryResult.getResult()));
                        operationTicks = 7;
                        setShortDesc("CMPD, DIR", null);
                        break;

                    /* CMPY - Compare Y - Direct */
                    case 0x9C:
                        memoryResult = memory.getDirect(regs);
                        compareWord(regs.getY(), memory.readWord(memoryResult.getResult()));
                        operationTicks = 7;
                        setShortDesc("CMPY, DIR", null);
                        break;

                    /* CMPD - Compare D - Direct */
                    case 0xA3:
                        memoryResult = memory.getIndexed(regs);
                        compareWord(regs.getD(), memory.readWord(memoryResult.getResult()));
                        operationTicks = 5 + memoryResult.getBytesConsumed();
                        setShortDesc("CMPD, IND", null);
                        break;

                    /* CMPY - Compare Y - Direct */
                    case 0xAC:
                        memoryResult = memory.getIndexed(regs);
                        compareWord(regs.getY(), memory.readWord(memoryResult.getResult()));
                        operationTicks = 5 + memoryResult.getBytesConsumed();
                        setShortDesc("CMPY, IND", null);
                        break;

                    /* CMPD - Compare D - Extended */
                    case 0xB3:
                        memoryResult = memory.getExtended(regs);
                        compareWord(regs.getD(), memory.readWord(memoryResult.getResult()));
                        operationTicks = 8;
                        setShortDesc("CMPD, EXT", null);
                        break;

                    /* CMPY - Compare Y - Extended */
                    case 0xBC:
                        memoryResult = memory.getExtended(regs);
                        compareWord(regs.getY(), memory.readWord(memoryResult.getResult()));
                        operationTicks = 8;
                        setShortDesc("CMPY, EXT", null);
                        break;
                }
                break;
            }

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

            /* INC - Increment - Indexed */
            case 0x6C:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::increment, memoryResult);
                setShortDesc("INCM, IND [%04X]", memoryResult);
                break;

            /* TST - Test - Indexed */
            case 0x6D:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::test, memoryResult);
                setShortDesc("TSTM, IND [%04X]", memoryResult);
                break;

            /* JMP - Jump - Indexed */
            case 0x6E:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 1 + memoryResult.getBytesConsumed();
                jump(memoryResult.getResult());
                setShortDesc("JMP, IND [%04X]", memoryResult);
                break;

            /* CLR - Clear - Indexed */
            case 0x6F:
                memoryResult = memory.getIndexed(regs);
                operationTicks = 4 + memoryResult.getBytesConsumed();
                executeByteFunctionM(this::clear, memoryResult);
                setShortDesc("CLRM, IND [%04X]", memoryResult);
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

            /* INC - Increment - Extended */
            case 0x7C:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::increment, memoryResult);
                setShortDesc("INCM, EXT [%04X]", memoryResult);
                break;

            /* TST - Test - Extended */
            case 0x7D:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::test, memoryResult);
                setShortDesc("TSTM, EXT [%04X]", memoryResult);
                break;

            /* JMP - Jump - Extended */
            case 0x7E:
                memoryResult = memory.getExtended(regs);
                operationTicks = 4;
                jump(memoryResult.getResult());
                setShortDesc("JMP, EXT [%04X]", memoryResult);
                break;

            /* CLR - Clear - Extended */
            case 0x7F:
                memoryResult = memory.getExtended(regs);
                operationTicks = 7;
                executeByteFunctionM(this::clear, memoryResult);
                setShortDesc("CLRM, EXT [%04X]", memoryResult);
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

    public void executeWordFunctionM(Function<UnsignedWord, UnsignedWord> function,
                                     MemoryResult memoryResult) {
        UnsignedWord address = memoryResult.getResult();
        UnsignedWord tempWord = memory.readWord(address);
        tempWord = function.apply(tempWord);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V));
        regs.cc.or(RegisterSet.CC_C);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V | RegisterSet.CC_C));
        regs.cc.or(result.isMasked(0x80) ? RegisterSet.CC_V : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z | RegisterSet.CC_N : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_C));
        regs.cc.or(value.isMasked(0x1) ? RegisterSet.CC_C : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_C));
        regs.cc.or(value.isMasked(0x1) ? RegisterSet.CC_C : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_C));
        regs.cc.or(value.isMasked(0x1) ? RegisterSet.CC_C : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V | RegisterSet.CC_C));
        regs.cc.or(value.isMasked(0x80) ? RegisterSet.CC_C : 0);
        regs.cc.or(value.isMasked(0xC0) ? RegisterSet.CC_V : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_C | RegisterSet.CC_V));
        regs.cc.or(value.isMasked(0x80) ? RegisterSet.CC_C : 0);
        regs.cc.or(value.isMasked(0xC0) ? RegisterSet.CC_V : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
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
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V));
        regs.cc.or(value.isZero() ? RegisterSet.CC_V : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
        return result;
    }

    /**
     * Increments the byte value by one.
     *
     * @param value the byte value to increment
     * @return the incremented byte value
     */
    public UnsignedByte increment(UnsignedByte value) {
        UnsignedByte result = regs.binaryAdd(value, new UnsignedByte(0x1), false, false, false);
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V));
        regs.cc.or(value.isMasked(0x7F) ? RegisterSet.CC_V : 0);
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
        return result;
    }

    /**
     * Tests the byte for zero condition or negative condition.
     *
     * @param value the byte value to test
     * @return the original byte value
     */
    public UnsignedByte test(UnsignedByte value) {
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V));
        regs.cc.or(value.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(value.isNegative() ? RegisterSet.CC_N : 0);
        return value;
    }

    /**
     * Jumps to the specified address.
     *
     * @param address the address to jump to
     */
    public void jump(UnsignedWord address) {
        regs.setPC(address);
    }

    /**
     * Clears the specified byte.
     *
     * @param value the value to clear
     * @return the cleared byte
     */
    public UnsignedByte clear(UnsignedByte value) {
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_C | RegisterSet.CC_V));
        regs.cc.or(RegisterSet.CC_Z);
        return new UnsignedByte(0);
    }

    /**
     * Increments (or decrements) the program counter by the specified amount.
     * Will interpret the UnsignedWord offset as a negative value if the setHigh
     * bit is set.
     *
     * @param offset the amount to offset the program counter
     */
    public void branchLong(UnsignedWord offset) {
        regs.getPC().add(offset.isNegative() ? offset.getSignedInt() : offset.getInt());
    }

    /**
     * Saves all registers to the stack, and jumps to the memory location
     * read at the specified address.
     *
     * @param offset the offset to read for a jump address
     */
    public void softwareInterrupt(UnsignedWord offset) {
        regs.setCCEverything();
        memory.pushStack(regs, Register.S, regs.getPC().getLow());
        memory.pushStack(regs, Register.S, regs.getPC().getHigh());
        memory.pushStack(regs, Register.S, regs.getU().getLow());
        memory.pushStack(regs, Register.S, regs.getU().getHigh());
        memory.pushStack(regs, Register.S, regs.getY().getLow());
        memory.pushStack(regs, Register.S, regs.getY().getHigh());
        memory.pushStack(regs, Register.S, regs.getX().getLow());
        memory.pushStack(regs, Register.S, regs.getX().getHigh());
        memory.pushStack(regs, Register.S, regs.getDP());
        memory.pushStack(regs, Register.S, regs.getB());
        memory.pushStack(regs, Register.S, regs.getA());
        memory.pushStack(regs, Register.S, regs.getCC());
        regs.setPC(memory.readWord(offset));
    }

    /**
     * Compares the two words and sets the appropriate register sets.
     *
     * @param word1 the first word to compare
     * @param word2 the second word to compare
     */
    public UnsignedWord compareWord(UnsignedWord word1, UnsignedWord word2) {
        UnsignedWord result = regs.binaryAdd(word1, word2.twosCompliment(), false, true, true);
        regs.cc.and(~(RegisterSet.CC_N | RegisterSet.CC_Z | RegisterSet.CC_V | RegisterSet.CC_C));
        regs.cc.or(result.isZero() ? RegisterSet.CC_Z : 0);
        regs.cc.or(result.isNegative() ? RegisterSet.CC_N : 0);
        return result;
    }
}
