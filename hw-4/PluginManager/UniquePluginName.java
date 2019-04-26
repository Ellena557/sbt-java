package ru.edhunter;

public class UniquePluginName {

    /**
     * Учитываем как название плагина, так и имя класса
     */

    private String className;
    private String pluginName;

    public UniquePluginName(String className, String pluginName) {
        this.className = className;
        this.pluginName = pluginName;
    }

    @Override
    public boolean equals(Object o) {
        UniquePluginName plugObject = (UniquePluginName) o;

        if (o == null ||
            this.getClass() != o.getClass() ||
            !this.pluginName.equals(plugObject.pluginName) ||
            !this.className.equals(plugObject.className)){
            return false;
        } else {
            return true;
        }
    }
}
