package ru.edhunter.TestClasses;

import ru.edhunter.Plugin;

public class TestPlugin implements Plugin {
    @Override
    public String doUseful() {
        return "Passed";
    }
}
