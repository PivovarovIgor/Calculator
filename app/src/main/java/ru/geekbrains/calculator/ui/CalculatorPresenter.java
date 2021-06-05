package ru.geekbrains.calculator.ui;

import java.math.BigDecimal;

import ru.geekbrains.calculator.domain.Calculator;

public class CalculatorPresenter {

    private final int NO_PRESSED_DOT = -1;

    private final CalculatorView view;
    private final Calculator calculator;
    private BigDecimal number;
    private int positionOfPoint = NO_PRESSED_DOT;
    private boolean inputNewNumber;

    public CalculatorPresenter(CalculatorView view) {
        this.calculator = new Calculator();
        this.view = view;
        this.number = BigDecimal.ZERO;
        setViewNumber();
    }

    public void keyDelPressed() {
        if (number == null) {
            return;
        }
        number = number.movePointLeft(1)
                .setScale(0, BigDecimal.ROUND_FLOOR);
        if (positionOfPoint > 1) {
            positionOfPoint--;
        } else {
            positionOfPoint = NO_PRESSED_DOT;
        }
        setViewNumber();
    }

    public void keyClearPressed() {
        number = calculator.clear();
        positionOfPoint = NO_PRESSED_DOT;
        setViewNumber();
    }

    public void keyDotPressed() {
        checkInputNewNumber();
        if (positionOfPoint == NO_PRESSED_DOT) {
            positionOfPoint = 0;
            setViewNumber();
        }
    }

    public void keyZeroPressed() {
        addDigit(0);
    }

    public void keyOnePressed() {
        addDigit(1);
    }

    public void keyTwoPressed() {
        addDigit(2);
    }

    public void keyThreePressed() {
        addDigit(3);
    }

    public void keyFourPressed() {
        addDigit(4);
    }

    public void keyFivePressed() {
        addDigit(5);
    }

    public void keySixPressed() {
        addDigit(6);
    }

    public void keySevenPressed() {
        addDigit(7);
    }

    public void keyEightPressed() {
        addDigit(8);
    }

    public void keyNinePressed() {
        addDigit(9);
    }

    public void keyPlusPressed() {
        setOperator(Calculator.Operations.ADD);
    }

    public void keyMinusPressed() {
        setOperator(Calculator.Operations.SUB);
    }

    public void keyMulPressed() {
        setOperator(Calculator.Operations.MUL);
    }

    public void keyDivPressed() {
        setOperator(Calculator.Operations.DIV);
    }

    public void keyResultPressed() {
        if (number == null) {
            return;
        }
        number = calculator.result(getNumberWithPoint());
        setViewNumber();
    }

    private void setOperator(Calculator.Operations op) {
        if (number == null) {
            return;
        }
        number = calculator.setOperator(op,
                (inputNewNumber) ? null : getNumberWithPoint()
        );
        inputNewNumber = true;
        setViewNumber();
    }

    private void addDigit(int digit) {
        if (number == null) {
            return;
        }
        checkInputNewNumber();
        number = number.movePointRight(1)
                .add(new BigDecimal(digit));
        if (positionOfPoint != NO_PRESSED_DOT) {
            positionOfPoint++;
        }
        setViewNumber();
    }

    private void checkInputNewNumber() {
        if (inputNewNumber) {
            number = BigDecimal.ZERO;
            positionOfPoint = NO_PRESSED_DOT;
            inputNewNumber = false;
        }
    }

    private void setViewNumber() {
        if (view != null) {
            if (number == null) {
                view.setViewNumber("error division by zero");
            } else {
                view.setViewNumber(String.valueOf(getNumberWithPoint()));
            }
        }
    }

    private BigDecimal getNumberWithPoint() {
        return (positionOfPoint > 0) ? number.movePointLeft(positionOfPoint) : number;
    }

    public interface CalculatorView {
        void setViewNumber(String num);
    }
}
