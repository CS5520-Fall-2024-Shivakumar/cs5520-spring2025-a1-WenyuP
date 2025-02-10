package com.example.numad25sp_wenyupan

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentExpression: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator) // Link to your XML file

        display = findViewById(R.id.displayText)

        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonEqual
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { buttonClicked(it as Button) }
        }
    }

    private fun buttonClicked(button: Button) {
        val value = button.text.toString()
        if (value == "=") {
            try {
                currentExpression = evaluateExpression(currentExpression).toString()
            } catch (e: Exception) {
                currentExpression = "Error"
            }
        } else {
            currentExpression += value
        }
        display.text = currentExpression
    }

    private fun evaluateExpression(expression: String): Double {
        return ExpressionBuilder(expression).build().evaluate()
    }
}
