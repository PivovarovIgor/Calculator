package ru.geekbrains.calculator.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.calculator.R;
import ru.geekbrains.calculator.businessLogic.Calculator;

public class MainActivity extends AppCompatActivity implements Calculator.OnUpdateScoreListener{

    private final String KEY_CALC = "calculator";
    private Calculator calculator;
    private TextView scoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            calculator = new Calculator();
        } else {
            calculator = savedInstanceState.getParcelable(KEY_CALC);
        }
        scoreboard = findViewById(R.id.scoreboard);

        initOnClickListener();
        calculator.setOnUpdateScoreListener(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CALC, calculator);
    }

    private void initOnClickListener() {

        int[] numbersButton = new int[]{
                R.id.key_0,
                R.id.key_1,
                R.id.key_2,
                R.id.key_3,
                R.id.key_4,
                R.id.key_5,
                R.id.key_6,
                R.id.key_7,
                R.id.key_8,
                R.id.key_9
        };
        for (int i = 0; i < numbersButton.length; i++) {
            char CH = (char) ('0' + i);
            findViewById(numbersButton[i])
                    .setOnClickListener(v -> calculator.put(CH));
        }
        findViewById(R.id.key_dot).setOnClickListener(v -> calculator.put('.'));
        findViewById(R.id.key_backspace).setOnClickListener(v -> calculator.del());
        findViewById(R.id.key_c).setOnClickListener(v -> calculator.clear());
        findViewById(R.id.key_div).setOnClickListener(v -> calculator.div());
        findViewById(R.id.key_mul).setOnClickListener(v -> calculator.mul());
        findViewById(R.id.key_plus).setOnClickListener(v -> calculator.plus());
        findViewById(R.id.key_minus).setOnClickListener(v -> calculator.minus());
        findViewById(R.id.key_result).setOnClickListener(v -> calculator.result());
        findViewById(R.id.key_percent).setOnClickListener(v -> calculator.percent());
    }

    @Override
    public void onUpdateScore(String textScore) {
        scoreboard.setText(calculator.getTextScore());
    }
}