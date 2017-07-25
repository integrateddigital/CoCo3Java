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
        return new UnsignedByte(memory[address.intValue()]);
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
        memory[address.intValue()] = value.getShort();
    }
}
