package com.andrewfroze.runner;

import com.andrewfroze.annotation.Suite;
import com.andrewfroze.annotation.SuiteValue;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.andrewfroze.annotation.SuiteValue.REGRESSION;
import static com.andrewfroze.annotation.SuiteValue.SMOKE;
import static org.reflections.scanners.Scanners.MethodsAnnotated;

public class TestRunner {
    public static void main(String[] args) {
        SuiteValue suiteValue= SMOKE;
        String pathToSuite = "/src/test/resources/suite-template.xml";

        XmlSuite suite = readXmlSuiteFromFile(System.getProperty("user.dir") + pathToSuite);
        suite.setName(suiteValue.toString());
        List<Method> testMethods = scanAndFilterMethods("com.andrewfroze.tests", suiteValue);
        for (Method testMethod: testMethods) {
            XmlTest xmlTest = new XmlTest(suite);
            XmlClass xmlClass = new XmlClass(testMethod.getDeclaringClass());
            xmlClass.setIncludedMethods(List.of(new XmlInclude(testMethod.getName())));
            xmlTest.setClasses(List.of(xmlClass));
            xmlTest.setName(testMethod.getAnnotation(Suite.class).description());
        }
        TestNG testNG = new TestNG();
        testNG.setXmlSuites(Collections.singletonList(suite));
        testNG.run();
    }

    private static List<Method> scanAndFilterMethods(String packageName, SuiteValue suiteValue) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(MethodsAnnotated)
        );

        return reflections.getMethodsAnnotatedWith(Suite.class).stream()
                .filter(method -> method.getAnnotation(Suite.class).value().equals(suiteValue))
                .filter(method -> method.isAnnotationPresent(Test.class) && method.getAnnotation(Test.class).enabled())
                .toList();
    }


    private static XmlSuite readXmlSuiteFromFile(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();

            XmlSuite xmlSuite = new XmlSuite();
            List<String> listeners = new ArrayList<>();
            NodeList listenersNodes = rootElement.getElementsByTagName("listeners");
            if (listenersNodes.getLength() > 0) {
                Element listenersElement = (Element) listenersNodes.item(0);
                NodeList listenerNodes = listenersElement.getElementsByTagName("listener");
                for (int i = 0; i < listenerNodes.getLength(); i++) {
                    Element listenerElement = (Element) listenerNodes.item(i);
                    String listenerClassName = listenerElement.getAttribute("class-name");
                    listeners.add(listenerClassName);
                }
            }
            xmlSuite.setListeners(listeners);

            String parallelMode = rootElement.getAttribute("parallel");
            if (!parallelMode.isEmpty()) {
                xmlSuite.setParallel(XmlSuite.ParallelMode.valueOf(parallelMode.toUpperCase()));
            }

            String threadCount = rootElement.getAttribute("thread-count");
            if (!threadCount.isEmpty()) {
                xmlSuite.setThreadCount(Integer.parseInt(threadCount));
            }

            return xmlSuite;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
