package ru.geekbrains.calculator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import ru.geekbrains.calculator.R;

public class CalculatorActivity extends AppCompatActivity implements CalculatorPresenter.CalculatorView {

    private static final String KEY_CALC = "calculator";
    private TextView scoreboard;
    private CalculatorPresenter calculatorPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreboard = findViewById(R.id.scoreboard);
        if (savedInstanceState == null) {
            calculatorPresenter = new CalculatorPresenter(this);
        } else {
            calculatorPresenter = savedInstanceState.getParcelable(KEY_CALC);
            calculatorPresenter.setView(this);
        }

        initOnClickListener();

        View buttonSetDark = findViewById(R.id.set_dark);
        if (buttonSetDark != null) {
            buttonSetDark.setOnClickListener(v -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES));
        }
        View buttonSetLight = findViewById(R.id.set_light);
        if (buttonSetLight != null) {
            buttonSetLight.setOnClickListener(v -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CALC, calculatorPresenter);
    }

    private void initOnClickListener() {

        findViewById(R.id.key_0).setOnClickListener(v -> calculatorPresenter.keyZeroPressed());
        findViewById(R.id.key_1).setOnClickListener(v -> calculatorPresenter.keyOnePressed());
        findViewById(R.id.key_2).setOnClickListener(v -> calculatorPresenter.keyTwoPressed());
        findViewById(R.id.key_3).setOnClickListener(v -> calculatorPresenter.keyThreePressed());
        findViewById(R.id.key_4).setOnClickListener(v -> calculatorPresenter.keyFourPressed());
        findViewById(R.id.key_5).setOnClickListener(v -> calculatorPresenter.keyFivePressed());
        findViewById(R.id.key_6).setOnClickListener(v -> calculatorPresenter.keySixPressed());
        findViewById(R.id.key_7).setOnClickListener(v -> calculatorPresenter.keySevenPressed());
        findViewById(R.id.key_8).setOnClickListener(v -> calculatorPresenter.keyEightPressed());
        findViewById(R.id.key_9).setOnClickListener(v -> calculatorPresenter.keyNinePressed());
        findViewById(R.id.key_dot).setOnClickListener(v -> calculatorPresenter.keyDotPressed());
        findViewById(R.id.key_backspace).setOnClickListener(v -> calculatorPresenter.keyDelPressed());
        findViewById(R.id.key_c).setOnClickListener(v -> calculatorPresenter.keyClearPressed());
        findViewById(R.id.key_div).setOnClickListener(v -> calculatorPresenter.keyDivPressed());
        findViewById(R.id.key_mul).setOnClickListener(v -> calculatorPresenter.keyMulPressed());
        findViewById(R.id.key_plus).setOnClickListener(v -> calculatorPresenter.keyPlusPressed());
        findViewById(R.id.key_minus).setOnClickListener(v -> calculatorPresenter.keyMinusPressed());
        findViewById(R.id.key_result).setOnClickListener(v -> calculatorPresenter.keyResultPressed());
        findViewById(R.id.key_percent).setOnClickListener(v -> calculatorPresenter.keyPercentPressed());
    }

    @Override
    public void setViewNumber(String num) {
        scoreboard.setText(num);
    }
}