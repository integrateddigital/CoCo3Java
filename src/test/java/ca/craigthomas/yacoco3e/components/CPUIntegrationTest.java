/*
 * Copyright (C) 2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import ca.craigthomas.yacoco3e.datatypes.Registers;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CPUIntegrationTest
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
    public void testNegateDirectCalled() {
        cpuSpy.executeInstruction(0x00);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testNegateIndexedCalled() {
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
    public void testComplementIndexedCalled() {
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
    public void testLogicalShiftRightIndexedCalled() {
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
    public void testRotateRightIndexedCalled() {
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

    @Test
    public void testArithmeticShiftRightDirectCalled() {
        cpuSpy.executeInstruction(0x07);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftIndexedCalled() {
        cpuSpy.executeInstruction(0x67);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftRightExtendedCalled() {
        cpuSpy.executeInstruction(0x77);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftDirectCalled() {
        cpuSpy.executeInstruction(0x08);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftIndexedCalled() {
        cpuSpy.executeInstruction(0x68);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftExtendedCalled() {
        cpuSpy.executeInstruction(0x78);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftDirectCalled() {
        cpuSpy.executeInstruction(0x09);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftIndexedCalled() {
        cpuSpy.executeInstruction(0x69);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftExtendedCalled() {
        cpuSpy.executeInstruction(0x79);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testDecrementDirectCalled() {
        cpuSpy.executeInstruction(0x0A);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testDecrementIndexedCalled() {
        cpuSpy.executeInstruction(0x6A);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testDecrementExtendedCalled() {
        cpuSpy.executeInstruction(0x7A);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testIncrementDirectCalled() {
        cpuSpy.executeInstruction(0x0C);
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }

    @Test
    public void testIncrementIndexedCalled() {
        cpuSpy.executeInstruction(0x6C);
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }

    @Test
    public void testIncrementExtendedCalled() {
        cpuSpy.executeInstruction(0x7C);
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }
}
