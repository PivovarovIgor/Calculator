package ru.geekbrains.calculator.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStorage {

    private static final String FILE_NAME = "app_themes";
    private static final String KEY_APP_THEME = "app_theme";

    private final SharedPreferences sharedPreferences;

    public SettingsStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setTheme(AppTheme theme) {
        sharedPreferences.edit()
                .putString(KEY_APP_THEME, theme.getKey())
                .apply();
    }

    public AppTheme getTheme() {
        String keyTheme = sharedPreferences.getString(KEY_APP_THEME, AppTheme.DEFAULT.getKey());
        for (AppTheme theme : AppTheme.values()) {
            if (theme.getKey().equals(keyTheme)) {
                return theme;
            }
        }
        return AppTheme.DEFAULT;
    }
}
