package com.epam.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void isPositiveNumberTest1() {
        assertFalse(StringUtils.isPositiveNumber("0"));
    }

    @Test
    public void isPositiveNumberTest2() {
        assertFalse(StringUtils.isPositiveNumber("-1"));
    }

    @Test
    public void isPositiveNumberTest3() {
        assertTrue(StringUtils.isPositiveNumber("1"));
    }

    @Test
    public void isPositiveNumberTest4() {
        assertTrue(StringUtils.isPositiveNumber("1.0"));
    }

    @Test
    public void isPositiveNumberTest5() {
        assertFalse(StringUtils.isPositiveNumber(null));
    }
}
