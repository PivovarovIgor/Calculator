package ru.geekbrains.calculator.ui;

import ru.geekbrains.calculator.R;

public enum AppTheme {

    DEFAULT(R.style.Theme_Calculator, "default"),
    CUSTOM(R.style.Theme_Calculator_Custom, "custom");

    AppTheme(int resource, String key) {
        this.resource = resource;
        this.key = key;
    }

    private final int resource;
    private final String key;

    public int getResource() {
        return resource;
    }

    public String getKey() {
        return key;
    }
}
