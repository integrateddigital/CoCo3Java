/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import ca.craigthomas.yacoco3e.datatypes.MemoryResult;
import ca.craigthomas.yacoco3e.datatypes.Registers;
import ca.craigthomas.yacoco3e.datatypes.UnsignedWord;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;

/**
 * The Memory class controls access to and from memory locations in the memory
 * array. Additionally, the memory class controls access to and from memory
 * mapped IO address routines.
 */
public class Memory
{
    /* 512K memory definition */
    public static final int MEM_512K = 0x80000;

    /* The main memory array */
    protected short [] memory;

    public Memory() {
        this(MEM_512K);
    }

    /**
     * Initializes the memory module with the number of bytes specified.
     *
     * @param size the number of bytes to initialize in main memory
     */
    public Memory(int size) {
        memory = new short[size];
    }

    /**
     * Given the current registers, will return an UnsignedWord from
     * the memory location of the direct pointer as the high byte,
     * and the low byte pointed to by the PC.
     *
     * @param regs the Registers containing the current CPU state
     * @return a MemoryResult with the data from the DP:PC location
     */
    public MemoryResult getDirect(Registers regs) {
        return new MemoryResult(
                1,
                new UnsignedWord(regs.getDP(), readByte(regs.getPC()))
        );
    }

    /**
     * Given the current registers, and an address into memory, return an
     * UnsignedWord that is based on the value of the byte that occurs at
     * one byte past the PC. This address contains a "post-byte" value that
     * encodes one of several different meanings. See the inline comments for
     * how the post-byte value is checked.
     *
     * @param regs the current state of the registers
     *
     * @return the value pointed to by the post-byte (or indexed location)
     * @throws IllegalIndexedPostbyteException
     */
    public MemoryResult getIndirect(Registers regs) {
        UnsignedWord address = regs.getPC().next();
        UnsignedByte postByte = readByte(address).copy();
        UnsignedWord register = new UnsignedWord(0);
        UnsignedWord result;
        UnsignedByte tempByte;
        UnsignedWord tempWord;
        int registerFlag = Registers.REG_UNKNOWN;
        int bytesConsumed = 1;
        int offset = 0;
        int addToReg = 0;

        /* Check to see if there is a 5-bit signed offset to apply */
        if (!postByte.isMasked(0x80)) {
            postByte.and(0x1F);
            if (postByte.isMasked(0x10)) {
                postByte.and(0xF);
                offset -= postByte.getShort();
            } else {
                offset += postByte.getShort();
            }
            result = address.copy();
            result.add(offset);
            return new MemoryResult(bytesConsumed, result);
        }

        /* Check to see if a register should be modified */
        switch (postByte.getShort() & 0x60) {
            case 0x00:
                register = regs.getX();
                registerFlag = Registers.REG_X;
                break;

            case 0x20:
                register = regs.getY();
                registerFlag = Registers.REG_Y;
                break;

            case 0x40:
                register = regs.getU();
                registerFlag = Registers.REG_U;
                break;

            case 0x60:
                register = regs.getS();
                registerFlag = Registers.REG_S;
                break;

            default:
                break;
        }

        /* Check the post-byte for the offset codes */
        switch (postByte.getShort() & 0xF) {
            case 0x0:
                /* ,R+ */
                result = register.copy();
                addToReg = 1;
                break;

            case 0x1:
                /* ,R++ */
                result = register.copy();
                addToReg = 2;
                break;

            case 0x2:
                /* ,R- */
                result = register.copy();
                addToReg = -1;
                break;

            case 0x3:
                /* ,R-- */
                result = register.copy();
                addToReg = -2;
                break;

            case 0x4:
                /* Nothing */
                result = register.copy();
                break;

            case 0x5:
                /* B,R */
                result = register.copy();
                result.add(regs.getB().getSignedShort());
                break;

            case 0x6:
                /* A,R */
                result = register.copy();
                result.add(regs.getA().getSignedShort());
                break;

            case 0x8:
                /* nn,R - 8-bit offset */
                tempByte = readByte(address);
                result = register.copy();
                result.add(tempByte.getSignedShort());
                bytesConsumed++;
                break;

            case 0x9:
                /* nnnn,R - 16-bit offset */
                tempWord = readWord(address);
                tempByte = readByte(tempWord);
                result = register.copy();
                result.add(tempByte.getSignedShort());
                bytesConsumed += 2;
                break;

            case 0xB:
                /* D,R */
                result = regs.binaryAdd(register, regs.getD(), false, false, false);
                break;

            case 0xC:
                /* nn,PC - 8-bit offset */
                tempByte = readByte(address);
                result = regs.getPC().copy();
                result.add(tempByte.getSignedShort());
                bytesConsumed = 1;
                break;

            case 0xD:
                /* nnnn,PC - 16-bit offset */
                tempWord = readWord(address);
                result = regs.getPC().copy();
                result.add(tempWord.getSignedInt());
                bytesConsumed = 2;
                break;

            default:
                /* TODO: better error handling */
                System.out.println("Illegal post-byte " + postByte.toString());
                result = new UnsignedWord(0);
        }

        /* If the 5-bit code starts with 1, do indirect addressing */
        if (postByte.isMasked(0x10)) {
            return new MemoryResult(bytesConsumed, readWord(result));
        }

        /* Before we calculate the address, post increment the registers */
        switch (registerFlag) {
            case Registers.REG_X:
                regs.getX().add(addToReg);
                break;

            case Registers.REG_Y:
                regs.getY().add(addToReg);
                break;

            case Registers.REG_U:
                regs.getU().add(addToReg);
                break;

            case Registers.REG_S:
                regs.getS().add(addToReg);
                break;
        }

        return new MemoryResult(bytesConsumed, result);
    }

    /**
     * Given the current registers, will return the value that is
     * pointed to by the program counter.
     *
     * @param regs the Registers containing the current CPU state
     * @return a MemoryResult with the data from the PC location
     */
    public MemoryResult getImmediate(Registers regs) {
        return new MemoryResult(
                2,
                readWord(regs.getPC())
        );
    }

    /**
     * Given the current registers, will return the value that is
     * pointed to by the value that is pointed to by the program
     * counter value.
     *
     * @param regs the Registers containing the current CPU state
     * @return a MemoryResult with the data from the PC location
     */
    public MemoryResult getExtended(Registers regs) {
        return new MemoryResult(
                2,
                readWord(readWord(regs.getPC()))
        );
    }

    /**
     * Reads an UnsignedByte from the specified address.
     *
     * @param address the UnsignedWord location to read from
     * @return an UnsignedByte from the specified location
     */
    public UnsignedByte readByte(UnsignedWord address) {
        return new UnsignedByte(memory[address.getInt()]);
    }

    /**
     * Reads an UnsignedWord from the specified address.
     *
     * @param address the UnsignedWord location to read from
     * @return an UnsignedWord from the specified location
     */
    public UnsignedWord readWord(UnsignedWord address) {
        UnsignedWord result = new UnsignedWord();
        result.high(readByte(address));
        result.low(readByte(address.next()));
        return result;
    }

    /**
     * Writes an UnsignedByte to the specified memory address.
     *
     * @param address the UnsignedWord location to write to
     * @param value the UnsignedByte to write
     */
    public void writeByte(UnsignedWord address, UnsignedByte value) {
        memory[address.getInt()] = value.getShort();
    }
}
