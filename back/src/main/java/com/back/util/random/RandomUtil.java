package com.back.util.random;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    /**
     * 约1/6概率输出自己输入的布尔值
     * */
    public static boolean russianRoulette(boolean targetValue) {
        return (ThreadLocalRandom.current().nextDouble() < 0.1666666666666667d) == targetValue;
    }

    /**
     * 输出true和false的概率相等
     * */
    public static boolean coinToss() {
        return ThreadLocalRandom.current().nextDouble() < 0.5d;
    }


}
