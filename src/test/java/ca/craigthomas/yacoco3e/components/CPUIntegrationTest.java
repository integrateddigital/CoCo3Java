/*
 * Copyright (C) 2017 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.yacoco3e.components;

import static org.junit.Assert.*;

import ca.craigthomas.yacoco3e.datatypes.RegisterSet;
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

    private RegisterSet registerSet;
    private RegisterSet registerSetSpy;

    private UnsignedByte expectedDirectByte;

    @Before
    public void setUp() {
        memory = new Memory();
        memorySpy = spy(memory);

        registerSet = new RegisterSet();
        registerSetSpy = spy(registerSet);

        cpu = new CPU(registerSetSpy, memorySpy);
        cpuSpy = spy(cpu);

        registerSetSpy.setDP(new UnsignedByte(0xA0));
        expectedDirectByte = new UnsignedByte(0xBA);
        memorySpy.writeByte(new UnsignedWord(0xA000), new UnsignedByte(0xBA));
    }

    @Test
    public void testNegateDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).negate(expectedDirectByte);
    }

    @Test
    public void testNegateIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x60));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testNegateExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x70));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).negate(new UnsignedByte(0));
    }

    @Test
    public void testComplementDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x03));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).compliment(expectedDirectByte);
    }

    @Test
    public void testComplementIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x63));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
    }

    @Test
    public void testComplementExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x73));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).compliment(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x04));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).logicalShiftRight(expectedDirectByte);
    }

    @Test
    public void testLogicalShiftRightIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x64));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testLogicalShiftRightExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x74));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).logicalShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x06));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).rotateRight(expectedDirectByte);
    }

    @Test
    public void testRotateRightIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x66));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }

    @Test
    public void testRotateRightExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x76));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).rotateRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftRightDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x07));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).arithmeticShiftRight(expectedDirectByte);
    }

    @Test
    public void testArithmeticShiftIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x67));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftRightExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x77));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).arithmeticShiftRight(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x08));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).arithmeticShiftLeft(expectedDirectByte);
    }

    @Test
    public void testArithmeticShiftLeftIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x68));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testArithmeticShiftLeftExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x78));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).arithmeticShiftLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x09));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).rotateLeft(expectedDirectByte);
    }

    @Test
    public void testRotateLeftIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x69));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testRotateLeftExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x79));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).rotateLeft(new UnsignedByte(0));
    }

    @Test
    public void testDecrementDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0A));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).decrement(expectedDirectByte);
    }

    @Test
    public void testDecrementIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6A));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testDecrementExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7A));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).decrement(new UnsignedByte(0));
    }

    @Test
    public void testIncrementDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).increment(expectedDirectByte);
    }

    @Test
    public void testIncrementIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }

    @Test
    public void testIncrementExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).increment(new UnsignedByte(0));
    }

    @Test
    public void testTestDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0D));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).test(expectedDirectByte);
    }

    @Test
    public void testTestIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6D));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).test(new UnsignedByte(0));
    }

    @Test
    public void testTestExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7D));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).test(new UnsignedByte(0));
    }

    @Test
    public void testJumpDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0E));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).jump(new UnsignedWord(0xA000));
    }

    @Test
    public void testJumpIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6E));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).jump(new UnsignedWord(0x2));
    }

    @Test
    public void testJumpExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7E));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).jump(new UnsignedWord(0x7E00));
    }

    @Test
    public void testClearDirectCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x0F));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).clear(expectedDirectByte);
    }

    @Test
    public void testClearIndexedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x6F));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).clear(new UnsignedByte(0x0));
    }

    @Test
    public void testClearExtendedCalled() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x7F));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).clear(new UnsignedByte(0));
    }

    @Test
    public void testLongBranchNeverDoesNothing() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x21));
        cpuSpy.executeInstruction();
        assertEquals(0x3, registerSet.getPC().getInt());
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
        registerSetSpy.setCCCarry();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x22));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnHighNotCalledWhenZeroSet() {
        registerSetSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x22));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLowerCalledCorrect() {
        registerSetSpy.setCCZero();
        registerSetSpy.setCCCarry();
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
        registerSetSpy.setCCCarry();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x24));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnCarrySetCalledCorrect() {
        registerSetSpy.setCCCarry();
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
        registerSetSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x26));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnEqualCalledCorrect() {
        registerSetSpy.setCCZero();
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
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x28));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnOverflowSetCalledCorrect() {
        registerSetSpy.setCCOverflow();
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
        registerSetSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2A));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnMinusCalledCorrect() {
        registerSetSpy.setCCNegative();
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
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2D));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLTCalledCorrectWhenNegativeSet() {
        registerSetSpy.setCCNegative();
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
        registerSetSpy.setCCNegative();
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2D));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGECalledCorrectWhenOverflowSet() {
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2C));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGECalledCorrectWhenNegativeSet() {
        registerSetSpy.setCCNegative();
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
        registerSetSpy.setCCNegative();
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2C));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGTCalledCorrectly() {
        registerSetSpy.setCCZero();
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2E));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGTNotCalledIfNotZeroSet() {
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2E));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnGTNotCalledIfAllSet() {
        registerSetSpy.setCCZero();
        registerSetSpy.setCCOverflow();
        registerSetSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2E));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLECalledCorrectly() {
        registerSetSpy.setCCZero();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLECalledIfNotZeroAndOverflow() {
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLECalledIfNotZeroAndNegative() {
        registerSetSpy.setCCNegative();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testLongBranchOnLENotCalledIfOverflowAndNegative() {
        registerSetSpy.setCCNegative();
        registerSetSpy.setCCOverflow();
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x2F));
        memorySpy.writeByte(new UnsignedWord(0x2), new UnsignedByte(0xBE));
        memorySpy.writeByte(new UnsignedWord(0x3), new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy, never()).branchLong(new UnsignedWord(0xBEEF));
    }

    @Test
    public void testSWI3PushesAllValues() {
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x3F));
        memorySpy.writeByte(CPU.SWI3, new UnsignedByte(0x56));
        memorySpy.writeByte(CPU.SWI3.next(), new UnsignedByte(0x78));
        registerSetSpy.setCCNegative();
        registerSetSpy.setCCOverflow();
        registerSetSpy.setS(new UnsignedWord(0xA000));
        registerSetSpy.setU(new UnsignedWord(0xDEAD));
        registerSetSpy.setY(new UnsignedWord(0xBEEF));
        registerSetSpy.setX(new UnsignedWord(0xCAFE));
        registerSetSpy.setDP(new UnsignedByte(0xAB));
        registerSetSpy.setB(new UnsignedByte(0xCD));
        registerSetSpy.setA(new UnsignedByte(0xEF));
        cpuSpy.executeInstruction();
        verify(cpuSpy).softwareInterrupt(CPU.SWI3);
        assertEquals(new UnsignedByte(0x02), memorySpy.readByte(new UnsignedWord(0x9FFF)));
        assertEquals(new UnsignedByte(0x00), memorySpy.readByte(new UnsignedWord(0x9FFE)));
        assertEquals(new UnsignedByte(0xAD), memorySpy.readByte(new UnsignedWord(0x9FFD)));
        assertEquals(new UnsignedByte(0xDE), memorySpy.readByte(new UnsignedWord(0x9FFC)));
        assertEquals(new UnsignedByte(0xEF), memorySpy.readByte(new UnsignedWord(0x9FFB)));
        assertEquals(new UnsignedByte(0xBE), memorySpy.readByte(new UnsignedWord(0x9FFA)));
        assertEquals(new UnsignedByte(0xFE), memorySpy.readByte(new UnsignedWord(0x9FF9)));
        assertEquals(new UnsignedByte(0xCA), memorySpy.readByte(new UnsignedWord(0x9FF8)));
        assertEquals(new UnsignedByte(0xAB), memorySpy.readByte(new UnsignedWord(0x9FF7)));
        assertEquals(new UnsignedByte(0xCD), memorySpy.readByte(new UnsignedWord(0x9FF6)));
        assertEquals(new UnsignedByte(0xEF), memorySpy.readByte(new UnsignedWord(0x9FF5)));
        assertEquals(new UnsignedByte(0x8A), memorySpy.readByte(new UnsignedWord(0x9FF4)));
    }

    @Test
    public void testCompareDImmediateCalled() {
        registerSetSpy.setD(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x83));
        cpuSpy.executeInstruction();
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(0x00));
    }

    @Test
    public void testCompareDDirectCalled() {
        registerSetSpy.setD(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x93));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(expectedDirectByte, new UnsignedByte(0)));
    }

    @Test
    public void testCompareDIndexedCalled() {
        registerSetSpy.setD(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0xA3));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(0x00));
    }

    @Test
    public void testCompareDExtendedCalled() {
        registerSetSpy.setD(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0xB3));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(0x00));
    }

    @Test
    public void testCompareYImmediateCalled() {
        registerSetSpy.setY(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x8C));
        cpuSpy.executeInstruction();
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(0x00));
    }

    @Test
    public void testCompareYDirectCalled() {
        registerSetSpy.setY(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0x9C));
        cpuSpy.executeInstruction();
        verify(memorySpy).getDirect(registerSetSpy);
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(expectedDirectByte, new UnsignedByte(0)));
    }

    @Test
    public void testCompareYIndexedCalled() {
        registerSetSpy.setY(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0xAC));
        cpuSpy.executeInstruction();
        verify(memorySpy).getIndexed(registerSetSpy);
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(0x00));
    }

    @Test
    public void testCompareYExtendedCalled() {
        registerSetSpy.setY(new UnsignedWord(0x10));
        memorySpy.writeByte(new UnsignedWord(0x0), new UnsignedByte(0x10));
        memorySpy.writeByte(new UnsignedWord(0x1), new UnsignedByte(0xBC));
        cpuSpy.executeInstruction();
        verify(memorySpy).getExtended(registerSetSpy);
        verify(cpuSpy).compareWord(new UnsignedWord(0x10), new UnsignedWord(0x00));
    }
}
