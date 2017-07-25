/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import static org.junit.Assert.*;

import ca.craigthomas.yacoco3e.datatypes.MemoryResult;
import ca.craigthomas.yacoco3e.datatypes.Registers;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;
import ca.craigthomas.yacoco3e.datatypes.UnsignedWord;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest
{
    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testDefaultConstructorSetsSizeTo512K() {
        assertEquals(memory.memory.length, Memory.MEM_512K);
    }

    @Test
    public void testReadByteReadsCorrectByte() {
        memory.memory[0xBEEF] = 0xAB;
        UnsignedByte result = memory.readByte(new UnsignedWord(0xBEEF));
        assertEquals(new UnsignedByte(0xAB), result);
    }

    @Test
    public void testWriteByteWritesCorrectByte() {
        memory.writeByte(new UnsignedWord(0xBEEF), new UnsignedByte(0xAB));
        assertEquals(memory.memory[0xBEEF], 0xAB);
    }

    @Test
    public void testReadWordReadsCorrectWord() {
        memory.memory[0xBEEE] = 0xAB;
        memory.memory[0xBEEF] = 0xCD;
        UnsignedWord result = memory.readWord(new UnsignedWord(0xBEEE));
        assertEquals(new UnsignedWord(0xABCD), result);
    }

    @Test
    public void testGetImmediateReadsAddressFromPC() {
        memory.memory[0xBEEE] = 0xAB;
        memory.memory[0xBEEF] = 0xCD;
        Registers regs = new Registers();
        regs.setPC(new UnsignedWord(0xBEEE));
        MemoryResult result = memory.getImmediate(regs);
        assertEquals(2, result.getBytesConsumed());
        assertEquals(new UnsignedWord(0xABCD), result.getResult());
    }

    @Test
    public void testGetDirectReadsAddressFromDPAndPC() {
        memory.memory[0xBEEE] = 0xCD;
        Registers regs = new Registers();
        regs.setPC(new UnsignedWord(0xBEEE));
        regs.setDP(new UnsignedByte(0xAB));
        MemoryResult result = memory.getDirect(regs);
        assertEquals(1, result.getBytesConsumed());
        assertEquals(new UnsignedWord(0xABCD), result.getResult());
    }
}
