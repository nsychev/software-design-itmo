package ru.nsychev.sd.profiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Profiler {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java ru.nsychev.sd.profiler.Profiler <profiledPackage> <mainClassName> [...]");
            System.exit(1);
        }

        System.setProperty("profiledPackage", args[0]);

        String mainClassName = args[1];
        try {
            Class<?> mainClass = Class.forName(mainClassName);
            Method mainMethod = mainClass.getMethod("main", String[].class);
            String[] mainArgs = Arrays.copyOfRange(args, 2, args.length - 2);
            mainMethod.invoke(null, (Object) mainArgs);
        } catch (ClassNotFoundException exc) {
            System.err.println("Class " + mainClassName + " not found");
            System.exit(1);
        } catch (NoSuchMethodException exc) {
            System.err.println("Class " + mainClassName + " doesn't contain main method");
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Method main is private in " + mainClassName);
            System.exit(1);
        } catch (InvocationTargetException e) {
            System.err.println("Method main of class " + mainClassName + " raised an exception:");
            e.printStackTrace();
            System.exit(1);
        }

        ProfilerStatsManager.getInstance().printStatistics();
    }
}
