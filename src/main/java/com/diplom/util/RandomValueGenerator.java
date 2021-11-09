package com.diplom.util;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomValueGenerator {

    public static final Random RANDOM = new Random();

    public static String getRandomString(List<String> values){
        return values.get(RANDOM.nextInt(values.size()));
    }

    public static boolean getRandomBoolean(){
        return RANDOM.nextBoolean();
    }

    public static long getRandomLong(float valueFrom, float valueTo){
        return  (long)valueFrom + (long) (Math.random() * (valueTo - valueFrom));
    }

    public static float getRandomFloat(float valueFrom, float valueTo){
        return valueFrom + RANDOM.nextFloat() * (valueTo - valueFrom);
    }

    public static String getRandomDate(float valueFrom, float valueTo){
        return randomDate((long) valueFrom, (long) valueTo);
    }

    private static String randomDate(long from, long to) {
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        long random = ThreadLocalRandom
                .current()
                .nextLong(from, to);
        return df2.format(random);
    }
}
