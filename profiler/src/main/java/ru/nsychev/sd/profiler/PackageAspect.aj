package ru.nsychev.sd.profiler;

import org.aspectj.lang.Signature;

public aspect PackageAspect {
    pointcut everyMethod():
            execution(* *.*(..)) &&
            if(thisJoinPointStaticPart.getSignature().getDeclaringTypeName().startsWith("" + System.getProperty("profiledPackage")));

    Object around(): everyMethod() {
        Signature s = thisEnclosingJoinPointStaticPart.getSignature();
        String methodName = s.getDeclaringTypeName() + "." + s.getName();

        long startTime = System.nanoTime();
        ProfilerStatsManager.getInstance().enterMethod(methodName);

        try {
            return proceed();
        } finally {
            ProfilerStatsManager.getInstance().exitMethod(System.nanoTime() - startTime);
        }
    }
}
