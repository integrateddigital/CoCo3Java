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
    public void testComplementAllOnes() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0xFF));
        cpu.complementM(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(0), result);
    }

    @Test
    public void testComplementOne() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x01));
        cpu.complementM(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(0xFE), result);
    }

    @Test
    public void testComplementSetsCarryFlag() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x01));
        cpu.complementM(address);
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testComplementSetsNegativeFlagCorrect() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x01));
        cpu.complementM(address);
        assertTrue(registers.ccNegativeSet());

        address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0xFE));
        cpu.complementM(address);
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testComplementSetsZeroFlagCorrect() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0xFF));
        cpu.complementM(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(0, result.getShort());
        assertTrue(registers.ccZeroSet());
    }

    @Test
    public void testLogicalShiftRightMovesOneBitCorrect() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x2));
        cpu.logicalShiftRight(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(1), result);
    }

    @Test
    public void testLogicalShiftRightMovesOneBitToZero() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x1));
        cpu.logicalShiftRight(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(0), result);
    }

    @Test
    public void testLogicalShiftRightSetsCarryBit() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x80));
        cpu.logicalShiftRight(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(0x40), result);
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testLogicalShiftRightSetsZeroBit() {
        UnsignedWord address = new UnsignedWord(0);
        memory.writeByte(address, new UnsignedByte(0x1));
        cpu.logicalShiftRight(address);
        UnsignedByte result = memory.readByte(address);
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registers.ccZeroSet());
    }

    @Test
    public void testNegateDirectCalled() {
        cpuSpy.executeInstruction(0x00);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).negateM(new UnsignedWord(0));
    }

    @Test
    public void testNegateIndirectCalled() {
        cpuSpy.executeInstruction(0x60);
        verify(memorySpy).getIndirect(registersSpy);
        verify(cpuSpy).negateM(new UnsignedWord(1));
    }

    @Test
    public void testNegateExtendedCalled() {
        cpuSpy.executeInstruction(0x70);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).negateM(new UnsignedWord(0));
    }

    @Test
    public void testComplementDirectCalled() {
        cpuSpy.executeInstruction(0x03);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).complementM(new UnsignedWord(0));
    }

    @Test
    public void testComplementIndirectCalled() {
        cpuSpy.executeInstruction(0x63);
        verify(memorySpy).getIndirect(registersSpy);
        verify(cpuSpy).complementM(new UnsignedWord(1));
    }

    @Test
    public void testComplementExtendedCalled() {
        cpuSpy.executeInstruction(0x73);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).complementM(new UnsignedWord(0));
    }

    @Test
    public void testLogicalShiftRightDirectCalled() {
        cpuSpy.executeInstruction(0x04);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedWord(0));
    }

    @Test
    public void testLogicalShiftRightIndirectCalled() {
        cpuSpy.executeInstruction(0x64);
        verify(memorySpy).getIndirect(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedWord(1));
    }

    @Test
    public void testLogicalShiftRightExtendedCalled() {
        cpuSpy.executeInstruction(0x74);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedWord(0));
    }
}
