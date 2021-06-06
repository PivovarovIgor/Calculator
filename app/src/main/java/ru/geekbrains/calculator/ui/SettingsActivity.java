package ru.geekbrains.calculator.ui;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.calculator.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsStorage settingsStorage = new SettingsStorage(this);
        setTheme(settingsStorage.getTheme().getResource());
        setContentView(R.layout.activity_settings);

        RadioGroup rg = findViewById(R.id.select_theme);

        RadioButton rb;
        if (settingsStorage.getTheme() == AppTheme.CUSTOM) {
            rb = findViewById(R.id.theme_custom_selected);
        } else {
            rb = findViewById(R.id.theme_default_selected);
        }
        rb.setChecked(true);

        findViewById(R.id.btn_apply_settings).setOnClickListener(v -> {
            AppTheme theme = AppTheme.DEFAULT;
            if (rg.getCheckedRadioButtonId() == R.id.theme_custom_selected) {
                theme = AppTheme.CUSTOM;
            }
            if (settingsStorage.getTheme() != theme) {
                settingsStorage.setTheme(theme);
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        });

        findViewById(R.id.btn_cancel_settings).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}