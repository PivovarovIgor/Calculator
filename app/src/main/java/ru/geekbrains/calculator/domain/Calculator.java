package ru.geekbrains.calculator.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator implements Serializable {

    private final BigDecimal[] args;
    private int pointer;
    private Operations currentOperation;

    public Calculator() {
        this.args = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
    }

    public BigDecimal clear() {
        args[0] = BigDecimal.ZERO;
        args[1] = BigDecimal.ZERO;
        pointer = 0;
        currentOperation = null;
        return args[0];
    }

    public BigDecimal result(BigDecimal number) {
        args[pointer] = number;
        calculate();
        pointer = 0;
        return args[0];
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
                    args[0] = args[0].divide(args[1], 20, RoundingMode.HALF_UP);
                }
                break;
        }
    }

    public enum Operations {
        ADD, SUB, DIV, MUL
    }
}