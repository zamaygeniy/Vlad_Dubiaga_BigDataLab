package com.epam;

import com.epam.util.StringUtils;

public class Utils
{
    public static boolean isAllPositive(String... str){
        for (String s : str) {
            if (!StringUtils.isPositiveNumber(s)) {
                return false;
            }
        }
        return true;
    }
}
