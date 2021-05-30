package ru.geekbrains.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        initOnClickListener();
        scoreboard = findViewById(R.id.scoreboard);
        showTextScore();
    }

    private void showTextScore() {
        scoreboard.setText(calculator.getTextScore());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CALC, calculator);
    }

    private void initOnClickListener() {

        initButtonOnClickListener(R.id.key_0);
        initButtonOnClickListener(R.id.key_1);
        initButtonOnClickListener(R.id.key_2);
        initButtonOnClickListener(R.id.key_3);
        initButtonOnClickListener(R.id.key_4);
        initButtonOnClickListener(R.id.key_5);
        initButtonOnClickListener(R.id.key_6);
        initButtonOnClickListener(R.id.key_7);
        initButtonOnClickListener(R.id.key_8);
        initButtonOnClickListener(R.id.key_9);
        initButtonOnClickListener(R.id.key_dot);
        initButtonOnClickListener(R.id.key_backspace);
        initButtonOnClickListener(R.id.key_c);
        initButtonOnClickListener(R.id.key_div);
        initButtonOnClickListener(R.id.key_mul);
        initButtonOnClickListener(R.id.key_plus);
        initButtonOnClickListener(R.id.key_minus);
        initButtonOnClickListener(R.id.key_result);
        initButtonOnClickListener(R.id.key_percent);
    }

    private void initButtonOnClickListener(int viewId) {
        findViewById(viewId).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.key_0:
                calculator.put('0');
                break;
            case R.id.key_1:
                calculator.put('1');
                break;
            case R.id.key_2:
                calculator.put('2');
                break;
            case R.id.key_3:
                calculator.put('3');
                break;
            case R.id.key_4:
                calculator.put('4');
                break;
            case R.id.key_5:
                calculator.put('5');
                break;
            case R.id.key_6:
                calculator.put('6');
                break;
            case R.id.key_7:
                calculator.put('7');
                break;
            case R.id.key_8:
                calculator.put('8');
                break;
            case R.id.key_9:
                calculator.put('9');
                break;
            case R.id.key_dot:
                calculator.put('.');
                break;
            case R.id.key_backspace:
                calculator.del();
                break;
            case R.id.key_c:
                calculator.clear();
                break;
            case R.id.key_plus:
                calculator.plus();
                break;
            case R.id.key_minus:
                calculator.minus();
                break;
            case R.id.key_div:
                calculator.div();
                break;
            case R.id.key_mul:
                calculator.mul();
                break;
            case R.id.key_percent:
                calculator.percent();
                break;
            case R.id.key_result:
                calculator.result();
                break;
        }
        showTextScore();
    }
}