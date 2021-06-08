package ru.geekbrains.calculator.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator implements Parcelable {

    private static final int SCALE_FOR_DIV_OPERATOR = 10;
    private static final int POINTER_TO_FIRST_ARGUMENT = 0;
    private static final int POINTER_TO_SECOND_ARGUMENT = 1;

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
        args[POINTER_TO_FIRST_ARGUMENT] = BigDecimal.ZERO;
        args[POINTER_TO_SECOND_ARGUMENT] = BigDecimal.ZERO;
        pointer = POINTER_TO_FIRST_ARGUMENT;
        currentOperation = null;
        return args[pointer];
    }

    public BigDecimal result(BigDecimal number) {
        if (number == null) {
            return null;
        }
        args[pointer] = number;
        calculate();
        pointer = POINTER_TO_FIRST_ARGUMENT;
        return args[pointer];
    }

    public BigDecimal setPercent(BigDecimal number) {
        if (number == null) {
            return null;
        }
        if (currentOperation == null) {
            clear();
            return args[POINTER_TO_FIRST_ARGUMENT];
        }
        switch (currentOperation) {
            case ADD:
            case SUB:
                args[POINTER_TO_SECOND_ARGUMENT] = args[POINTER_TO_FIRST_ARGUMENT]
                        .divide(BigDecimal.valueOf(100),
                        SCALE_FOR_DIV_OPERATOR, RoundingMode.HALF_UP)
                        .multiply(number);
                break;
            case MUL:
            case DIV:
                args[POINTER_TO_SECOND_ARGUMENT] = number.divide(BigDecimal.valueOf(100),
                        SCALE_FOR_DIV_OPERATOR, RoundingMode.HALF_UP);
                break;
        }
        return args[POINTER_TO_SECOND_ARGUMENT];
    }

    public BigDecimal setOperator(Operations op, BigDecimal number) {

        if (number != null) {
            args[pointer] = number;
        }
        if (pointer == POINTER_TO_FIRST_ARGUMENT) {
            pointer = POINTER_TO_SECOND_ARGUMENT;
        } else if (number != null) {
            calculate();
        }
        currentOperation = op;
        return args[POINTER_TO_FIRST_ARGUMENT];
    }

    private void calculate() {
        if (currentOperation == null) {
            return;
        }
        switch (currentOperation) {
            case ADD:
                args[POINTER_TO_FIRST_ARGUMENT] = args[POINTER_TO_FIRST_ARGUMENT]
                        .add(args[POINTER_TO_SECOND_ARGUMENT]);
                break;
            case SUB:
                args[POINTER_TO_FIRST_ARGUMENT] = args[POINTER_TO_FIRST_ARGUMENT]
                        .subtract(args[POINTER_TO_SECOND_ARGUMENT]);
                break;
            case MUL:
                args[POINTER_TO_FIRST_ARGUMENT] = args[POINTER_TO_FIRST_ARGUMENT]
                        .multiply(args[POINTER_TO_SECOND_ARGUMENT]);
                break;
            case DIV:
                if (args[POINTER_TO_SECOND_ARGUMENT].equals(BigDecimal.ZERO)) {
                    args[POINTER_TO_FIRST_ARGUMENT] = null;
                } else {
                    args[POINTER_TO_FIRST_ARGUMENT] = args[POINTER_TO_FIRST_ARGUMENT]
                            .divide(args[POINTER_TO_SECOND_ARGUMENT],
                                    SCALE_FOR_DIV_OPERATOR,
                                    RoundingMode.HALF_UP);
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