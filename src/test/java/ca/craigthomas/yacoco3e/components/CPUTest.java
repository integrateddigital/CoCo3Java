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

public class CPUTest
{
    private CPU cpu;
    private Memory memory;
    private Registers registers;

    @Before
    public void setUp() {
        memory = new Memory();
        registers = new Registers();
        cpu = new CPU(registers, memory);
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
    public void testArithmeticShiftRightOneCorrect() {
        UnsignedByte result = cpu.arithmeticShiftRight(new UnsignedByte(0x1));
        assertEquals(0, result.getShort());
        assertTrue(registers.ccZeroSet());
        assertFalse(registers.ccNegativeSet());
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftRightHighBitRetained() {
        UnsignedByte result = cpu.arithmeticShiftRight(new UnsignedByte(0x81));
        assertEquals(0xC0, result.getShort());
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccNegativeSet());
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftLeftOneCorrect() {
        UnsignedByte result = cpu.arithmeticShiftLeft(new UnsignedByte(0x1));
        assertEquals(0x2, result.getShort());
        assertFalse(registers.ccZeroSet());
        assertFalse(registers.ccOverflowSet());
        assertFalse(registers.ccNegativeSet());
        assertFalse(registers.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftLeftHighBitShiftedToCarry() {
        UnsignedByte result = cpu.arithmeticShiftLeft(new UnsignedByte(0x81));
        assertEquals(0x2, result.getShort());
        assertFalse(registers.ccZeroSet());
        assertFalse(registers.ccOverflowSet());
        assertFalse(registers.ccNegativeSet());
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testArithmeticShiftLeftOverflowSet() {
        UnsignedByte result = cpu.arithmeticShiftLeft(new UnsignedByte(0xC0));
        assertEquals(0x80, result.getShort());
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccOverflowSet());
        assertTrue(registers.ccNegativeSet());
        assertTrue(registers.ccCarrySet());
    }

    @Test
    public void testRotateLeftOneCorrect() {
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0x1));
        assertEquals(0x2, result.getShort());
        assertFalse(registers.ccCarrySet());
        assertFalse(registers.ccOverflowSet());
        assertFalse(registers.ccNegativeSet());
        assertFalse(registers.ccZeroSet());
    }

    @Test
    public void testRotateLeftSetsCarry() {
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0x80));
        assertEquals(0x0, result.getShort());
        assertTrue(registers.ccCarrySet());
        assertFalse(registers.ccOverflowSet());
        assertTrue(registers.ccZeroSet());
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testRotateLeftRotatesCarryToLowestBit() {
        registers.setCCCarry();
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0x1));
        assertEquals(0x3, result.getShort());
        assertFalse(registers.ccCarrySet());
        assertFalse(registers.ccOverflowSet());
        assertFalse(registers.ccZeroSet());
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testRotateLeftSetsOverflow() {
        UnsignedByte result = cpu.rotateLeft(new UnsignedByte(0xC0));
        assertEquals(0x80, result.getShort());
        assertTrue(registers.ccCarrySet());
        assertTrue(registers.ccOverflowSet());
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testDecrementOneCorrect() {
        UnsignedByte result = cpu.decrement(new UnsignedByte(0x1));
        assertEquals(0x0, result.getShort());
        assertFalse(registers.ccOverflowSet());
        assertTrue(registers.ccZeroSet());
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testDecrementZeroCorrect() {
        UnsignedByte result = cpu.decrement(new UnsignedByte(0x0));
        assertEquals(0xFF, result.getShort());
        assertTrue(registers.ccOverflowSet());
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testDecrementHighValueCorrect() {
        UnsignedByte result = cpu.decrement(new UnsignedByte(0xFF));
        assertEquals(0xFE, result.getShort());
        assertFalse(registers.ccOverflowSet());
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testIncrementOneCorrect() {
        UnsignedByte result = cpu.increment(new UnsignedByte(0x1));
        assertEquals(0x2, result.getShort());
        assertFalse(registers.ccOverflowSet());
        assertFalse(registers.ccZeroSet());
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testIncrementSetsOverflow() {
        UnsignedByte result = cpu.increment(new UnsignedByte(0x7F));
        assertEquals(0x80, result.getShort());
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccOverflowSet());
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testTestZeroCorrect() {
        cpu.test(new UnsignedByte(0x0));
        assertTrue(registers.ccZeroSet());
        assertFalse(registers.ccNegativeSet());
    }

    @Test
    public void testTestNegativeCorrect() {
        cpu.test(new UnsignedByte(0x81));
        assertFalse(registers.ccZeroSet());
        assertTrue(registers.ccNegativeSet());
    }

    @Test
    public void testJumpUpdatesPCCorrect() {
        UnsignedWord address = new UnsignedWord(0xABCD);
        cpu.jump(address);
        assertEquals(address, registers.getPC());
    }
}
