package ru.geekbrains.calculator.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator implements Parcelable {

    private static final int SCALE_FOR_DIV_OPERATOR = 10;

    private final BigDecimal[] args;
    private int pointer;
    private Operations currentOperation;

    public Calculator() {
        this.args = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
    }

    protected Calculator(Parcel in) {
        pointer = in.readInt();
        args = (BigDecimal[]) in.readArray(getClass().getClassLoader());
        currentOperation = (Operations) in.readSerializable();
    }

    public static final Creator<Calculator> CREATOR = new Creator<Calculator>() {
        @Override
        public Calculator createFromParcel(Parcel in) {
            return new Calculator(in);
        }

        @Override
        public Calculator[] newArray(int size) {
            return new Calculator[size];
        }
    };

    public BigDecimal clear() {
        args[0] = BigDecimal.ZERO;
        args[1] = BigDecimal.ZERO;
        pointer = 0;
        currentOperation = null;
        return args[0];
    }

    public BigDecimal result(BigDecimal number) {
        if (number == null) {
            return null;
        }
        args[pointer] = number;
        calculate();
        pointer = 0;
        return args[0];
    }

    public BigDecimal setPercent(BigDecimal number) {
        if (number == null) {
            return null;
        }
        if (currentOperation == null) {
            clear();
            return args[0];
        }
        switch (currentOperation) {
            case ADD:
            case SUB:
                args[1] = args[0].divide(BigDecimal.valueOf(100),
                        SCALE_FOR_DIV_OPERATOR, RoundingMode.HALF_UP)
                        .multiply(number);
                break;
            case MUL:
            case DIV:
                args[1] = number.divide(BigDecimal.valueOf(100),
                        SCALE_FOR_DIV_OPERATOR, RoundingMode.HALF_UP);
                break;
        }
        return args[1];
    }

    public BigDecimal setOperator(Operations op, BigDecimal number) {

        if (number != null) {
            args[pointer] = number;
        }
        if (pointer == 0) {
            pointer = 1;
        } else if (number != null) {
            calculate();
        }
        currentOperation = op;
        return args[0];
    }

    private void calculate() {
        if (currentOperation == null) {
            return;
        }
        switch (currentOperation) {
            case ADD:
                args[0] = args[0].add(args[1]);
                break;
            case SUB:
                args[0] = args[0].subtract(args[1]);
                break;
            case MUL:
                args[0] = args[0].multiply(args[1]);
                break;
            case DIV:
                if (args[1].equals(BigDecimal.ZERO)) {
                    args[0] = null;
                } else {
                    args[0] = args[0].divide(args[1], SCALE_FOR_DIV_OPERATOR, RoundingMode.HALF_UP);
                }
                break;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pointer);
        dest.writeArray(args);
        dest.writeSerializable(currentOperation);
    }

    public enum Operations {
        ADD, SUB, DIV, MUL
    }
}