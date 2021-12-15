package com.epam;

import static org.junit.Assert.*;


import org.junit.Test;


public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( Utils.isAllPositive("12", "79") );
    }
    @Test
    public void shouldAnswerWithFalse()
    {
        assertFalse(Utils.isAllPositive((String) null));
    }
}
