package ru.nsychev.sd.profdemo.profiled;

import java.security.SecureRandom;
import java.util.Random;

public class Faker {
    private final Random rand = new SecureRandom();

    public int getRandomType() {
        return rand.nextInt(2);
    }

    public int getRandomArgument() {
        return rand.nextInt(5000);
    }
}
