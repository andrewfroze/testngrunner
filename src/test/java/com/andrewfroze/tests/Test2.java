package com.andrewfroze.tests;

import com.andrewfroze.annotation.Suite;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.andrewfroze.annotation.SuiteValue.REGRESSION;
import static com.andrewfroze.annotation.SuiteValue.SMOKE;

public class Test2 {

    @Suite(value = REGRESSION, description = "Regression test 1.")
    @Test
    public void test2() {
        System.out.println("test2");
    }
}
