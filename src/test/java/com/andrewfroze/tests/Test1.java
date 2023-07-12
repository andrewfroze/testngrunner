package com.andrewfroze.tests;

import com.andrewfroze.annotation.Suite;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.andrewfroze.annotation.SuiteValue.SMOKE;

public class Test1 {

    @Suite(value = SMOKE, description = "Smoke test 1.")
    @Test
    public void test1() {
        System.out.println("test1");
    }
}
