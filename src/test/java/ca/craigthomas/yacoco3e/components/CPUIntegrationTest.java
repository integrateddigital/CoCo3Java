/*
 * Copyright (C) 2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import static org.junit.Assert.*;

import ca.craigthomas.yacoco3e.datatypes.Registers;
import ca.craigthomas.yacoco3e.datatypes.UnsignedByte;
import ca.craigthomas.yacoco3e.datatypes.UnsignedWord;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.never;
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

    private UnsignedByte expectedDirectByte;

    @Before
    public void setUp() {
        memory = new Memory();
        memorySpy = spy(memory);

        registers = new Registers();
        registersSpy = spy(registers);

        cpu = new CPU(registersSpy, memorySpy);
        cpuSpy = spy(cpu);

        registersSpy.setDP(new UnsignedByte(0xA0));
        expectedDirectByte = new UnsignedByte(0xBA);
        memorySpy.writeByte(new UnsignedWord(0xA000), new UnsignedByte(0xBA));
    }

    @Test
    public void testNegateDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).negate(expectedDirectByte);
    }

    @Test
    public void testNegateIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x60));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testNegateExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x70));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testComplementDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x03));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).compliment(expectedDirectByte);
    }

    @Test
    public void testComplementIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x63));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
    }

    @Test
    public void testComplementExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x73));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x04));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).logicalShiftRight(expectedDirectByte);
    }

    @Test
    public void testLogicalShiftRightIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x64));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x74));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x06));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).rotateRight(expectedDirectByte);
    }

    @Test
    public void testRotateRightIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x66));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x76));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftRightDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x07));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).arithmeticShiftRight(expectedDirectByte);
    }

    @Test
    public void testArithmeticShiftIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x67));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftRightExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x77));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x08));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).arithmeticShiftLeft(expectedDirectByte);
    }

    @Test
    public void testArithmeticShiftLeftIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x68));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x78));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x09));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).rotateLeft(expectedDirectByte);
    }

    @Test
    public void testRotateLeftIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x69));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x79));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testDecrementDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0A));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).decrement(expectedDirectByte);
    }

    @Test
    public void testDecrementIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6A));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testDecrementExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7A));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testIncrementDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).increment(expectedDirectByte);
    }

    @Test
    public void testIncrementIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }

    @Test
    public void testIncrementExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }

    @Test
    public void testTestDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0D));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).test(expectedDirectByte);
    }

    @Test
    public void testTestIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6D));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).test(new UnsignedByte(0));
    }

    @Test
    public void testTestExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7D));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).test(new UnsignedByte(0));
    }

    @Test
    public void testJumpDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0E));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).jump(new UnsignedWord(0xA000));
    }

    @Test
    public void testJumpIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6E));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).jump(new UnsignedWord(0x2));
    }

    @Test
    public void testJumpExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7E));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).jump(new UnsignedWord(0x7E00));
    }

    @Test
    public void testClearDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0F));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registersSpy);
        verify(cpuSpy).clear(expectedDirectByte);
    }

    @Test
    public void testClearIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6F));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registersSpy);
        verify(cpuSpy).clear(new UnsignedByte(0x0));
    }

    @Test
    public void testClearExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7F));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registersSpy);
        verify(cpuSpy).clear(new UnsignedByte(0));
    }

    @Test
    public void testLongBranchNeverDoesNothing() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x21));
        cpuSpy.executeInstruction();
        assertEquals(0x3, registers.getPC().getInt());
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0x0000));
    }

    @Test
    public void testLongBranchOnHighCalledCorrect() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x22));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnHighNotCalledWhenCarrySet() {
        registersSpy.setCCCarry();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x22));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnHighNotCalledWhenZeroSet() {
        registersSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x22));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLowerCalledCorrect() {
        registersSpy.setCCZero();
        registersSpy.setCCCarry();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x23));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnHighNotCalledWhenCarryAndZeroNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x23));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnCarryClearCalledCorrect() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x24));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnCarryClearNotCalledWhenCarrySet() {
        registersSpy.setCCCarry();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x24));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnCarrySetCalledCorrect() {
        registersSpy.setCCCarry();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x25));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnCarrySetNotCalledWhenCarryNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x25));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnNotEqualCalledCorrect() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x26));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnNotEqualNotCalledWhenZeroSet() {
        registersSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x26));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnEqualCalledCorrect() {
        registersSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x27));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnEqualNotCalledWhenZeroNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x27));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnOverflowClearCalledCorrect() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x28));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnOverflowClearNotCalledWhenOverflowSet() {
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x28));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnOverflowSetCalledCorrect() {
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x29));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnOverflowSetNotCalledWhenOverflowNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x29));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnPlusCalledCorrect() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2A));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnPlusNotCalledWhenNegativeSet() {
        registersSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2A));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnMinusCalledCorrect() {
        registersSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2B));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnMinusNotCalledWhenNegativeNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2B));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLTCalledCorrectWhenOverflowSet() {
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2D));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLTCalledCorrectWhenNegativeSet() {
        registersSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2D));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }


    @Test
    public void testLongBranchOnLTNotCalledWhenNegativeAndOverflowNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2D));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLTNotCalledWhenNegativeAndOverflowSet() {
        registersSpy.setCCNegative();
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2D));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGECalledCorrectWhenOverflowSet() {
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2C));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGECalledCorrectWhenNegativeSet() {
        registersSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2C));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }


    @Test
    public void testLongBranchOnGENotCalledWhenNegativeAndOverflowNotSet() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2C));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGENotCalledWhenNegativeAndOverflowSet() {
        registersSpy.setCCNegative();
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2C));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGTCalledCorrectly() {
        registersSpy.setCCZero();
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2E));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGTNotCalledIfNotZeroSet() {
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2E));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGTNotCalledIfAllSet() {
        registersSpy.setCCZero();
        registersSpy.setCCOverflow();
        registersSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2E));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLECalledCorrectly() {
        registersSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLECalledIfNotZeroAndOverflow() {
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLECalledIfNotZeroAndNegative() {
        registersSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLENotCalledIfOverflowAndNegative() {
        registersSpy.setCCNegative();
        registersSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }
}
