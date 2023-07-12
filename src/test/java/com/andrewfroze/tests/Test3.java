package com.andrewfroze.tests;

import com.andrewfroze.annotation.Suite;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.andrewfroze.annotation.SuiteValue.REGRESSION;

public class Test3 {

    @Suite(value = REGRESSION, description = "Regression test 2.")
    @Test
    public void test3() {
        System.out.println("test3");
    }
}
