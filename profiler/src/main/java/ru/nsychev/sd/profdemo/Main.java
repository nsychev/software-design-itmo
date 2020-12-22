package ru.nsychev.sd.profdemo;

import ru.nsychev.sd.profdemo.profiled.SmartFactorial;
import ru.nsychev.sd.profdemo.profiled.Faker;

public class Main {
    public static void main(String[] args) {
        Faker generator = new Faker();
        SmartFactorial factorial = new SmartFactorial();

        for (int i = 0; i < 5000; i++) {
            int type = generator.getRandomType();
            int arg = generator.getRandomArgument();

            if (type == 1) {
                factorial.factorialMinusOne(arg);
            } else {
                factorial.factorialPlusOne(arg);
            }
        }
    }
}
