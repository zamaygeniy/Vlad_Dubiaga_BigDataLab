package com.epam.util;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils
{
    public static boolean isPositiveNumber(String str){
        return NumberUtils.isCreatable(str) && str.charAt(0) != '-' && !NumberUtils.createNumber(str).equals(0);
    }
}
