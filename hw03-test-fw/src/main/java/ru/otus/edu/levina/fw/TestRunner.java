package ru.otus.edu.levina.fw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ru.otus.edu.levina.fw.annotations.After;
import ru.otus.edu.levina.fw.annotations.Before;
import ru.otus.edu.levina.fw.annotations.Test;
import ru.otus.edu.levina.tests.PartyTest;

public class TestRunner {

    public static void runTest(Class<PartyTest> clazzTest)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        
        List<Method> tests = TestUtils.getAnnotatedMethods(clazzTest, Test.class);
        List<Method> befores = TestUtils.getAnnotatedMethods(clazzTest, Before.class);
        List<Method> afters = TestUtils.getAnnotatedMethods(clazzTest, After.class);
        
        List<String> passedTests = new ArrayList<>();
        List<String> failedTests = new ArrayList<>();
        
        for (Method test : tests) {
            System.out.println("------------------------------------------");
            Object clazzTestInst = TestUtils.newInstance(clazzTest);
            
            for (Method before : befores) {
                // here we allow exceptions, because it is not testing part
                System.out.println("...Begin " + before.getName());
                TestUtils.runMethod(before, clazzTestInst);
                System.out.println("...Finish " + before.getName());
            }
            
            System.out.println("...Begin " + test.getName());
            Throwable e = TestUtils.runTestMethod(test, clazzTestInst);
            if (e == null) {
                passedTests.add(String.format("...Test %s passed", test.getName()));
                System.out.println(passedTests.get(passedTests.size() - 1));
            } else {
                failedTests.add(String.format("...Test %s failed: %s", test.getName(), e.toString()));
                System.out.println(failedTests.get(failedTests.size() - 1));
            }
            
            for (Method after : afters) {
                System.out.println("...Begin " + after.getName());
                // here we allow exceptions, because it is not testing part
                TestUtils.runMethod(after, clazzTestInst);
                System.out.println("...Finish " + after.getName());
            }
            
            System.out.println("------------------------------------------");
        }
        
        System.out.println("Reports:");
        System.out.println("Total tests: " + tests.size());
        System.out.println("Passed tests: " + passedTests.size());
        System.out.println("Failed tests: " + failedTests.size());
        if (failedTests.size() == 0) {
            System.out.println("All tests passed. Be careful: it seems you missed something!");
        } else {
            System.out.println("Yes, they failed! Again. Hehehe.");
        }
    }
}
