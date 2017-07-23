/*
 * Copyright (C) 2013-2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;

import ca.craigthomas.yacoco3e.datatypes.Registers;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;
import ca.craigthomas.yacoco3e.datatypes.UnsignedWord;
import org.junit.Before;
import org.junit.Test;

public class CPUTest
{
    private CPU cpu;
    private CPU cpuSpy;

    private Memory memory;
    private Memory memorySpy;

    private Registers registers;
    private Registers registersSpy;

    @Before
    public void setUp() {
        memory = new Memory();
        memorySpy = spy(memory);

        registers = new Registers();
        registersSpy = spy(registers);

        cpu = new CPU(registersSpy, memorySpy);
        cpuSpy = spy(cpu);
    }

    @Test
    public void testNegateAllOnes() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0xFF));
        cpu.negateM(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(1), result);
    }

    @Test
    public void testNegateOne() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x01));
        cpu.negateM(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(0xFF), result);
    }

    @Test
    public void testNegateSetsOverflowFlag() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x01));
        cpu.negateM(address);
        assertTrue(registers.ccOverflowSet());
    }

    @Test
    public void testNegateSetsNegativeFlag() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x01));
        cpu.negateM(address);
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testNegateDirectCalled() {
        cpuSpy.executeInstruction(0x00);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).negateM(new UnsignedWord(0));
    }

    @Test
    public void testNegateExtendedCalled() {
        cpuSpy.executeInstruction(0x70);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).negateM(new UnsignedWord(0));
    }
}
