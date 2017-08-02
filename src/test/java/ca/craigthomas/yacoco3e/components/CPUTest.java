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
        UnsignedByte result = cpu.negateM(new UnsignedByte(0xFF));
        assertEquals(new UnsignedByte(1), result);
    }

    @Test
    public void testNegateOne() {
        UnsignedByte result = cpu.negateM(new UnsignedByte(0x01));
        assertEquals(new UnsignedByte(0xFF), result);
    }

    @Test
    public void testNegateSetsOverflowFlag() {
        cpu.negateM(new UnsignedByte(0x01));
        assertTrue(registers.ccOverflowSet());
    }

    @Test
    public void testNegateSetsNegativeFlag() {
        cpu.negateM(new UnsignedByte(0x01));
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testComplementAllOnes() {
        UnsignedByte result = cpu.complimentM(new UnsignedByte(0xFF));
        assertEquals(new UnsignedByte(0), result);
    }

    @Test
    public void testComplementOne() {
        UnsignedByte result = cpu.complimentM(new UnsignedByte(0x01));
        assertEquals(new UnsignedByte(0xFE), result);
    }

    @Test
    public void testComplementSetsCarryFlag() {
        cpu.complimentM(new UnsignedByte(0x01));
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testComplementSetsNegativeFlagCorrect() {
        cpu.complimentM(new UnsignedByte(0x01));
        assertTrue(registers.ccNegativeSet());

        cpu.complimentM(new UnsignedByte(0xFE));
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testComplementSetsZeroFlagCorrect() {
        UnsignedByte result = cpu.complimentM(new UnsignedByte(0xFF));
        assertEquals(0, result.getShort());
        assertTrue(registers.ccZeroSet());
    }

    @Test
    public void testLogicalShiftRightMovesOneBitCorrect() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(1), result);
    }

    @Test
    public void testLogicalShiftRightMovesOneBitToZero() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
    }

    @Test
    public void testLogicalShiftRightSetsCarryBit() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x01));
        assertEquals(new UnsignedByte(0x0), result);
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testLogicalShiftRightSetsZeroBit() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registers.ccZeroSet());
    }

    @Test
    public void testNegateDirectCalled() {
        cpuSpy.executeInstruction(0x00);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).negateM(new UnsignedByte(0));
    }

    @Test
    public void testNegateIndirectCalled() {
        cpuSpy.executeInstruction(0x60);
        verify(memorySpy).getIndirect(registersSpy);
        verify(cpuSpy).negateM(new UnsignedByte(0));
    }

    @Test
    public void testNegateExtendedCalled() {
        cpuSpy.executeInstruction(0x70);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).negateM(new UnsignedByte(0));
    }

    @Test
    public void testComplementDirectCalled() {
        cpuSpy.executeInstruction(0x03);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).complimentM(new UnsignedByte(0));
    }

    @Test
    public void testComplementIndirectCalled() {
        cpuSpy.executeInstruction(0x63);
        verify(memorySpy).getIndirect(registersSpy);
        verify(cpuSpy).complimentM(new UnsignedByte(0));
    }

    @Test
    public void testComplementExtendedCalled() {
        cpuSpy.executeInstruction(0x73);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).complimentM(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightDirectCalled() {
        cpuSpy.executeInstruction(0x04);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightIndirectCalled() {
        cpuSpy.executeInstruction(0x64);
        verify(memorySpy).getIndirect(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightExtendedCalled() {
        cpuSpy.executeInstruction(0x74);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }
}
