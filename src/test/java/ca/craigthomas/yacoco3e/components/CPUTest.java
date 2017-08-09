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

public class CPUTest
{
    private CPU cpu;
    private Memory memory;
    private RegisterSet registerSet;

    @Before
    public void setUp() {
        memory = new Memory();
        registerSet = new RegisterSet();
        cpu = new CPU(registerSet, memory);
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
        assertTrue(registerSet.ccOverflowSet());
    }

    @Test
    public void testNegateSetsNegativeFlag() {
        cpu.negate(new UnsignedByte(0x01));
        assertTrue(registerSet.ccNegativeSet());
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
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testComplementSetsNegativeFlagCorrect() {
        cpu.compliment(new UnsignedByte(0x01));
        assertTrue(registerSet.ccNegativeSet());

        cpu.compliment(new UnsignedByte(0xFE));
        assertFalse(registerSet.ccNegativeSet());
    }

    @Test
    public void testComplementSetsZeroFlagCorrect() {
        UnsignedByte result = cpu.compliment(new UnsignedByte(0xFF));
        assertEquals(0, result.getShort());
        assertTrue(registerSet.ccZeroSet());
    }

    @Test
    public void testLogicalShiftRightMovesOneBitCorrect() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(1), result);
        assertFalse(registerSet.ccCarrySet());
    }

    @Test
    public void testLogicalShiftRightMovesOneBitToZero() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testLogicalShiftRightSetsZeroBit() {
        UnsignedByte result = cpu.logicalShiftRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registerSet.ccZeroSet());
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testRotateRightMovesOneBitCorrect() {
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(1), result);
        assertFalse(registerSet.ccCarrySet());
    }

    @Test
    public void testRotateRightMovesOneBitCorrectWithCarry() {
        registerSet.setCCCarry();
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x2));
        assertEquals(new UnsignedByte(0x81), result);
        assertFalse(registerSet.ccCarrySet());
    }

    @Test
    public void testRotateRightMovesOneBitToZero() {
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testRotateRightSetsZeroBit() {
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0), result);
        assertTrue(registerSet.ccZeroSet());
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testRotateRightSetsNegativeBit() {
        registerSet.setCCCarry();
        UnsignedByte result = cpu.rotateRight(new UnsignedByte(0x1));
        assertEquals(new UnsignedByte(0x80), result);
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccCarrySet());
        assertTrue(registerSet.ccNegativeSet());
    }

    @Test
    public void testArithmeticShiftRightOneCorrect() {
        UnsignedByte result = cpu.arithmeticShiftRight(new UnsignedByte(0x1));
        assertEquals(0, result.getShort());
        assertTrue(registerSet.ccZeroSet());
        assertFalse(registerSet.ccNegativeSet());
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftRightHighBitRetained() {
        UnsignedByte result = cpu.arithmeticShiftRight(new UnsignedByte(0x81));
        assertEquals(0xC0, result.getShort());
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccNegativeSet());
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftLeftOneCorrect() {
        UnsignedByte result = cpu.arithmeticShiftLeft(new UnsignedByte(0x1));
        assertEquals(0x2, result.getShort());
        assertFalse(registerSet.ccZeroSet());
        assertFalse(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccNegativeSet());
        assertFalse(registerSet.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftLeftHighBitShiftedToCarry() {
        UnsignedByte result = cpu.arithmeticShiftLeft(new UnsignedByte(0x81));
        assertEquals(0x2, result.getShort());
        assertFalse(registerSet.ccZeroSet());
        assertFalse(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccNegativeSet());
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftLeftOverflowSet() {
        UnsignedByte result = cpu.arithmeticShiftLeft(new UnsignedByte(0xC0));
        assertEquals(0x80, result.getShort());
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccOverflowSet());
        assertTrue(registerSet.ccNegativeSet());
        assertTrue(registerSet.ccCarrySet());
    }

    @Test
    public void testRotateLeftOneCorrect() {
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0x1));
        assertEquals(0x2, result.getShort());
        assertFalse(registerSet.ccCarrySet());
        assertFalse(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccNegativeSet());
        assertFalse(registerSet.ccZeroSet());
    }

    @Test
    public void testRotateLeftSetsCarry() {
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0x80));
        assertEquals(0x0, result.getShort());
        assertTrue(registerSet.ccCarrySet());
        assertFalse(registerSet.ccOverflowSet());
        assertTrue(registerSet.ccZeroSet());
        assertFalse(registerSet.ccNegativeSet());
    }

    @Test
    public void testRotateLeftRotatesCarryToLowestBit() {
        registerSet.setCCCarry();
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0x1));
        assertEquals(0x3, result.getShort());
        assertFalse(registerSet.ccCarrySet());
        assertFalse(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccZeroSet());
        assertFalse(registerSet.ccNegativeSet());
    }

    @Test
    public void testRotateLeftSetsOverflow() {
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0xC0));
        assertEquals(0x80, result.getShort());
        assertTrue(registerSet.ccCarrySet());
        assertTrue(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccNegativeSet());
    }

    @Test
    public void testDecrementOneCorrect() {
        UnsignedByte result = cpu.decrement(new UnsignedByte(0x1));
        assertEquals(0x0, result.getShort());
        assertFalse(registerSet.ccOverflowSet());
        assertTrue(registerSet.ccZeroSet());
        assertFalse(registerSet.ccNegativeSet());
    }

    @Test
    public void testDecrementZeroCorrect() {
        UnsignedByte result = cpu.decrement(new UnsignedByte(0x0));
        assertEquals(0xFF, result.getShort());
        assertTrue(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccNegativeSet());
    }

    @Test
    public void testDecrementHighValueCorrect() {
        UnsignedByte result = cpu.decrement(new UnsignedByte(0xFF));
        assertEquals(0xFE, result.getShort());
        assertFalse(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccNegativeSet());
    }

    @Test
    public void testIncrementOneCorrect() {
        UnsignedByte result = cpu.increment(new UnsignedByte(0x1));
        assertEquals(0x2, result.getShort());
        assertFalse(registerSet.ccOverflowSet());
        assertFalse(registerSet.ccZeroSet());
        assertFalse(registerSet.ccNegativeSet());
    }

    @Test
    public void testIncrementSetsOverflow() {
        UnsignedByte result = cpu.increment(new UnsignedByte(0x7F));
        assertEquals(0x80, result.getShort());
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccOverflowSet());
        assertTrue(registerSet.ccNegativeSet());
    }

    @Test
    public void testTestZeroCorrect() {
        cpu.test(new UnsignedByte(0x0));
        assertTrue(registerSet.ccZeroSet());
        assertFalse(registerSet.ccNegativeSet());
    }

    @Test
    public void testTestNegativeCorrect() {
        cpu.test(new UnsignedByte(0x81));
        assertFalse(registerSet.ccZeroSet());
        assertTrue(registerSet.ccNegativeSet());
    }

    @Test
    public void testJumpUpdatesPCCorrect() {
        UnsignedWord address = new UnsignedWord(0xABCD);
        cpu.jump(address);
        assertEquals(address, registerSet.getPC());
    }

    @Test
    public void testClearWorksCorrect() {
        UnsignedByte result = cpu.clear(new UnsignedByte(0x4));
        assertEquals(0, result.getShort());
        assertTrue(registerSet.ccZeroSet());
    }
}
