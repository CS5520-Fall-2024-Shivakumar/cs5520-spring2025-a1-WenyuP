package com.example.numad25sp_wenyupan_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {

    private TextView calcDisplay;
    private StringBuilder currentInput = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calcDisplay = findViewById(R.id.textView);

        setButtonClickListeners();
    }

    private void setButtonClickListeners() {
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnPlus, R.id.buttonMinus
        };

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                currentInput.append(clickedButton.getText().toString());
                calcDisplay.setText(currentInput.toString()); // Update display
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(numberClickListener);
        }

        findViewById(R.id.btnDivide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentInput.length() > 0) {
                    String result = evaluateMathExpression(currentInput.toString());
                    calcDisplay.setText(result);
                    currentInput.setLength(0);
                    if (!result.equals("Error")) {
                        currentInput.append(result);
                    }
                }
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentInput.length() > 0) {
                    currentInput.deleteCharAt(currentInput.length() - 1);
                    calcDisplay.setText(currentInput.length() > 0 ? currentInput.toString() : "CALC");
                }
            }
        });
    }

    private String evaluateMathExpression(String expression) {
        try {
            return String.valueOf(evaluate(expression));
        } catch (Exception e) {
            return "Error";
        }
    }

    private int evaluate(String expression) {
        int result = 0;
        int currentNumber = 0;
        int sign = 1; // 1 for '+', -1 for '-'

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                currentNumber = currentNumber * 10 + (c - '0');
            } else if (c == '+' || c == '-') {
                result += sign * currentNumber;
                currentNumber = 0;
                sign = (c == '+') ? 1 : -1;
            }
        }
        result += sign * currentNumber;

        return result;
    }
}
