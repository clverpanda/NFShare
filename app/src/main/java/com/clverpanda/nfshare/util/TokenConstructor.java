package com.clverpanda.nfshare.util;

import java.util.Random;

/**
 * Created by clverpanda on 2017/5/23 0023.
 * It's the file for NFShare.
 */

public class TokenConstructor
{
    public static String getToken()
    {
        int length = 10;
        String base = "abcdef0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
