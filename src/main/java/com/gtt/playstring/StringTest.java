package com.gtt.playstring;

import org.junit.Test;

public class StringTest {

    @Test
    public void testStringSplit() {
        String a = "abcd";
        String[] split = a.split(",");
        System.out.println(split[0] +","+ split.length);
    }
}
