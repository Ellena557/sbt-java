package ru.edhunter;

import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class PluginManagerTest {
    String pluginRootDirectory = "file://D:/Java_progs/PluginManager";
    PluginManager pluginManager1 = new PluginManager(pluginRootDirectory);
    PluginManager pluginManager2 = new PluginManager(null);

    @Test
    public void testPluginManager() throws ClassNotFoundException,
            MalformedURLException, InstantiationException, IllegalAccessException {

        Plugin plugin1 = pluginManager1.load("PluginOne",
                "ru.edhunter.TestClasses.TestPlugin");
        Plugin plugin2 = pluginManager1.load("PluginTwo",
                "ru.edhunter.TestClasses.TestPlugin");

        assertTrue(plugin2.getClass().equals(plugin1.getClass()));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFound() throws ClassNotFoundException,
            MalformedURLException, InstantiationException, IllegalAccessException {
        PluginManager pluginManager3 = new PluginManager("file://D:/Javajavajava");
        Plugin plugin1 = pluginManager1.load("PluginOne",
                "ru.edhunter.java");
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedURLException() throws ClassNotFoundException,
            MalformedURLException, InstantiationException, IllegalAccessException {
        pluginManager2.load("MyPlugin", "MyClassPlugin");
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() throws ClassNotFoundException,
            MalformedURLException, InstantiationException, IllegalAccessException {
        pluginManager1.load(null, null);
    }
}