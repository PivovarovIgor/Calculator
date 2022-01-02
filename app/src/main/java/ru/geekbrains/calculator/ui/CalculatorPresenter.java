package ru.geekbrains.calculator.ui;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

import ru.geekbrains.calculator.domain.Calculator;

public class CalculatorPresenter implements Parcelable {

    public static final Creator<CalculatorPresenter> CREATOR = new Creator<CalculatorPresenter>() {
        @Override
        public CalculatorPresenter createFromParcel(Parcel in) {
            return new CalculatorPresenter(in);
        }

        @Override
        public CalculatorPresenter[] newArray(int size) {
            return new CalculatorPresenter[size];
        }
    };
    private static final int NO_PRESSED_DOT = -1;
    private final Calculator calculator;
    private CalculatorView view;
    private BigDecimal number;
    private int positionOfPoint = NO_PRESSED_DOT;
    private boolean inputNewNumber;

    public CalculatorPresenter(CalculatorView view, Calculator calculator) {
        this.calculator = calculator;
        this.view = view;
        this.number = BigDecimal.ZERO;
        setViewNumber();
    }

    protected CalculatorPresenter(Parcel in) {
        calculator = in.readParcelable(Calculator.class.getClassLoader());
        positionOfPoint = in.readInt();
        inputNewNumber = in.readByte() != 0;
        number = (BigDecimal) in.readSerializable();
    }

    public void setView(CalculatorView view) {
        this.view = view;
        setViewNumber();
    }

    public void keyDelPressed() {
        if (number == null) {
            return;
        }
        if (positionOfPoint != 0) {
            number = number.movePointLeft(1)
                    .setScale(0, BigDecimal.ROUND_FLOOR);
        }
        if (positionOfPoint != NO_PRESSED_DOT) {
            positionOfPoint--;
        }
        setViewNumber();
    }

    public void keyClearPressed() {
        number = calculator.clear();
        positionOfPoint = NO_PRESSED_DOT;
        setViewNumber();
    }

    public void keyDotPressed() {
        if (number == null) {
            return;
        }
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

    public void keyPercentPressed() {
        if (number == null) {
            return;
        }
        number = calculator.setPercent(number);
        setInputNewNumber();
        setViewNumber();
    }

    public void keyResultPressed() {
        if (number == null) {
            return;
        }
        number = calculator.result(getNumberWithPoint());
        setInputNewNumber();
        setViewNumber();
    }

    private void setOperator(Calculator.Operations op) {
        if (number == null) {
            return;
        }
        number = calculator.setOperator(op,
                (inputNewNumber) ? null : getNumberWithPoint()
        );
        setInputNewNumber();
        setViewNumber();
    }

    private void setInputNewNumber() {
        inputNewNumber = true;
        positionOfPoint = NO_PRESSED_DOT;
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
                view.setViewNumber(getNumberWithPoint()
                        + ((positionOfPoint == 0) ? "." : ""));
            }
        }
    }

    private BigDecimal getNumberWithPoint() {
        return (positionOfPoint > 0) ? number.movePointLeft(positionOfPoint) : number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(calculator, flags);
        dest.writeInt(positionOfPoint);
        dest.writeByte((byte) (inputNewNumber ? 1 : 0));
        dest.writeSerializable(number);
    }

    public void setNumber(String strNumber) {
        number = new BigDecimal(strNumber);
        setViewNumber();
    }

    public interface CalculatorView {
        void setViewNumber(String num);
    }
}