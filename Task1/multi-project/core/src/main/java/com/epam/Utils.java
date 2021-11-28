package com.epam;

import com.epam.util.StringUtils;

public class Utils {
    private Utils(){

    }
    public static boolean isAllPositive(String... str) {
        if (str == null) {
            return false;
        }
        for (String s : str) {
            if (!StringUtils.isPositiveNumber(s)) {
                return false;
            }
        }
        return true;
    }
}
