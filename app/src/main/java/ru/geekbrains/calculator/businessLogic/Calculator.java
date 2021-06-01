package ru.geekbrains.calculator.businessLogic;

import android.os.Parcel;
import android.os.Parcelable;

public class Calculator implements Parcelable {

    private StringBuilder textScore;
    private final double[] args;
    private int pointer;
    private Operations currentOperation;
    private boolean isEnteringNewArgument;
    private OnUpdateScoreListener onUpdateScoreListener;

    public Calculator() {
        this.textScore = new StringBuilder("0");
        this.args = new double[2];
    }

    protected Calculator(Parcel in) {
        this.args = in.createDoubleArray();
        this.pointer = in.readInt();
        this.isEnteringNewArgument = in.readByte() != 0;
        this.textScore = new StringBuilder(in.readString());
        this.currentOperation = (Operations) in.readSerializable();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(args);
        dest.writeInt(pointer);
        dest.writeByte((byte) (isEnteringNewArgument ? 1 : 0));
        dest.writeString(textScore.toString());
        dest.writeSerializable(currentOperation);
    }

    public String getTextScore() {
        return this.textScore.toString();
    }

    public void setOnUpdateScoreListener(OnUpdateScoreListener listener) {
        this.onUpdateScoreListener = listener;
        updateScore();
    }

    public void put(char ch) {
        if (!isEnteringNewArgument) {
            textScore = new StringBuilder("0");
            isEnteringNewArgument = true;
        }
        if (!validateAppendChar(ch)) {
            return;
        }
        this.textScore.append(ch);
        if (this.textScore.length() > 1
                && this.textScore.charAt(0) == '0'
                && this.textScore.charAt(1) != '.') {
            this.textScore.deleteCharAt(0);
        }
        updateScore();
    }

    public void clear() {
        args[0] = 0;
        args[1] = 1;
        pointer = 0;
        currentOperation = null;
        this.textScore = new StringBuilder("0");
        updateScore();
    }

    public void del() {
        char charToDel = textScore.charAt(textScore.length() - 1);
        if ((charToDel < '0' || charToDel > '9')
                && charToDel != '.') {
            return;
        }
        textScore.deleteCharAt(textScore.length() - 1);
        if (textScore.length() == 0
                || (textScore.length() == 1 && textScore.charAt(0) == '-')) {
            textScore = new StringBuilder("0");
        }
        updateScore();
    }

    public void plus() {
        setOperation(Operations.PLUS);
    }

    public void minus() {
        setOperation(Operations.MINUS);
    }

    public void div() {
        setOperation(Operations.DIV);
    }

    public void mul() {
        setOperation(Operations.MUL);
    }

    public void percent() {

        if (currentOperation == null) {
            clear();
            return;
        }

        switch (currentOperation) {
            case MINUS:
            case PLUS:
                setScoreNumber(args[0] * getScoreNumber() / 100);
                break;
            case DIV:
            case MUL:
                setScoreNumber(getScoreNumber() / 100);
                break;
        }
    }

    public interface OnUpdateScoreListener {

        void onUpdateScore(String textScore);
    }

    private void updateScore() {
        if (onUpdateScoreListener == null) {
            return;
        }
        onUpdateScoreListener.onUpdateScore(getTextScore());
    }

    private void setScoreNumber(double number) {
        textScore = new StringBuilder(Double.toString(number));
        updateScore();
    }

    private double getScoreNumber() {
        return Double.parseDouble(textScore.toString());
    }

    public void result() {
        newArgumentApplied();
        getNewResult();
        pointer = 0;
    }

    private boolean validateAppendChar(char ch) {
        if (ch == '.') {
            for (int i = 0; i < textScore.length(); i++) {
                if (textScore.charAt(i) == ch) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setOperation(Operations op) {
        if (newArgumentApplied()) {
            getNewResult();
        }
        currentOperation = op;
    }

    private boolean newArgumentApplied() {
        args[pointer] = getScoreNumber();
        boolean isNewArgument = isEnteringNewArgument && pointer == 1;
        isEnteringNewArgument = false;
        if (pointer == 0) {
            pointer = 1;
        }
        return isNewArgument;
    }

    private void getNewResult() {
        if (currentOperation == null) {
            return;
        }
        switch (currentOperation) {
            case PLUS:
                args[0] = args[0] + args[1];
                break;
            case MINUS:
                args[0] = args[0] - args[1];
                break;
            case MUL:
                args[0] = args[0] * args[1];
                break;
            case DIV:
                args[0] = args[0] / args[1];
                break;
        }
        setScoreNumber(args[0]);
    }

    private enum Operations {
        PLUS, MINUS, DIV, MUL
    }
}