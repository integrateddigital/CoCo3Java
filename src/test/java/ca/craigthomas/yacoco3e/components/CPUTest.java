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
        UnsignedByte result = cpu.negate(new UnsignedByte(0xFF));
        assertEquals(new UnsignedByte(1), result);
    }

    @Test
    public void testNegateOne() {
        UnsignedByte result = cpu.negate(new UnsignedByte(0x01));
        assertEquals(new UnsignedByte(0xFF), result);
    }

    @Test
    public void testNegateSetsOverflowFlag() {
        cpu.negate(new UnsignedByte(0x01));
        assertTrue(registers.ccOverflowSet());
    }

    @Test
    public void testNegateSetsNegativeFlag() {
        cpu.negate(new UnsignedByte(0x01));
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testComplementAllOnes() {
        UnsignedByte result = cpu.compliment(new UnsignedByte(0xFF));
        assertEquals(new UnsignedByte(0), result);
    }

    @Test
    public void testComplementOne() {
        UnsignedByte result = cpu.compliment(new UnsignedByte(0x01));
        assertEquals(new UnsignedByte(0xFE), result);
    }

    @Test
    public void testComplementSetsCarryFlag() {
        cpu.compliment(new UnsignedByte(0x01));
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testComplementSetsNegativeFlagCorrect() {
        cpu.compliment(new UnsignedByte(0x01));
        assertTrue(registers.ccNegativeSet());

        cpu.compliment(new UnsignedByte(0xFE));
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testComplementSetsZeroFlagCorrect() {
        UnsignedByte result = cpu.compliment(new UnsignedByte(0xFF));
        assertEquals(0, result.getShort());
        assertTrue(registers.ccZeroSet());
    }

    @Test
    public void testLogicalShiftRightMovesOneBitCorrect() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(1), result);
        assertFalse(registers.ccCarrySet());
    }

    @Test
    public void testLogicalShiftRightMovesOneBitToZero() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testLogicalShiftRightSetsZeroBit() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registers.ccZeroSet());
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testRotateRightMovesOneBitCorrect() {
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(1), result);
        assertFalse(registers.ccCarrySet());
    }

    @Test
    public void testRotateRightMovesOneBitCorrectWithCarry() {
        registers.setCCCarry();
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(0x81), result);
        assertFalse(registers.ccCarrySet());
    }

    @Test
    public void testRotateRightMovesOneBitToZero() {
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testRotateRightSetsZeroBit() {
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registers.ccZeroSet());
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testRotateRightSetsNegativeBit() {
        registers.setCCCarry();
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0x80), result);
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccCarrySet());
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testNegateDirectCalled() {
        cpuSpy.executeInstruction(0x00);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testNegateIndirectCalled() {
        cpuSpy.executeInstruction(0x60);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testNegateExtendedCalled() {
        cpuSpy.executeInstruction(0x70);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testComplementDirectCalled() {
        cpuSpy.executeInstruction(0x03);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
    }

    @Test
    public void testComplementIndirectCalled() {
        cpuSpy.executeInstruction(0x63);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
    }

    @Test
    public void testComplementExtendedCalled() {
        cpuSpy.executeInstruction(0x73);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
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
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightExtendedCalled() {
        cpuSpy.executeInstruction(0x74);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightDirectCalled() {
        cpuSpy.executeInstruction(0x06);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightIndirectCalled() {
        cpuSpy.executeInstruction(0x66);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightExtendedCalled() {
        cpuSpy.executeInstruction(0x76);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }
}
