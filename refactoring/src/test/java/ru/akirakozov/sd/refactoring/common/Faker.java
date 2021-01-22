package ru.akirakozov.sd.refactoring.common;

import java.util.Random;

public class Faker {
    private static int counter = 0;
    private static final StringGenerator stringGenerator = new StringGenerator();

    public static int getPrice() {
        return counter++;
    }

    public static String getName() {
        return stringGenerator.nextString();
    }

    public static SimpleProduct getSimpleProduct() {
        return new SimpleProduct(getName(), getPrice());
    }

    private static class StringGenerator {
        private final Random rnd = new Random(0);

        String nextString() {
            StringBuilder sb = new StringBuilder();
            int length = rnd.nextInt(30) + 1;
            for (int i = 0; i < length; i++) {
                sb.append((char)(rnd.nextInt(94) + 32));
            }
            return sb.toString();
        }
    }
}
