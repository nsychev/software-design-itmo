package ru.nsychev.sd.profdemo.profiled;

public class SmartFactorial {
    private int factorial(int n) {
        int x = 1;
        for (int i = 1; i <= n; i++) {
            x *= i;
            x %= 1000003;
        }
        return x;
    }

    public int factorialPlusOne(int n) {
        return factorial(n) + 1;
    }

    public int factorialMinusOne(int n) {
        return factorial(n) - 1;
    }
}
