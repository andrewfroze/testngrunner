package com.andrewfroze.listeners;

import com.andrewfroze.annotation.Suite;
import org.testng.*;

public class Listener1 extends TestListenerAdapter {

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("parallel mode: " + iTestContext.getSuite().getXmlSuite().getParallel());
        System.out.println("thread count: " + iTestContext.getSuite().getXmlSuite().getThreadCount());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Suite.class).description());
    }
}
