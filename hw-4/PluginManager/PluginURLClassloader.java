package ru.edhunter;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class PluginURLClassloader extends URLClassLoader {

    public PluginURLClassloader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PluginURLClassloader(URL[] urls) {
        super(urls);
    }

    public PluginURLClassloader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return findClass(name);
        } catch (ClassNotFoundException e) {
            return super.loadClass(name);
        }
    }
}
