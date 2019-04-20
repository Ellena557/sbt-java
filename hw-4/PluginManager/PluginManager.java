package ru.edhunter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {
    private final String pluginRootDirectory;
    private final Map<UniquePluginName, Class<?>> classesLoaded = new HashMap<>();

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin load(String pluginName, String pluginClassName) throws MalformedURLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException {

        UniquePluginName uniqueName = new UniquePluginName(pluginName, pluginClassName);
        Class<?> clazz = classesLoaded.get(uniqueName);

        //check either this class has already been loaded in this PluginManager
        if (clazz == null){
            //class hasn't been loaded

            URL url = new URL(pluginRootDirectory + pluginName + "/");
            URL[] urls = new URL[]{url};
            URLClassLoader pluginClassLoader = new PluginURLClassloader(urls);

            clazz = pluginClassLoader.loadClass(pluginClassName);
            classesLoaded.put(uniqueName, clazz);

            try {
                pluginClassLoader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Plugin plugin = (Plugin) clazz.newInstance();

        return plugin;
    }
}
