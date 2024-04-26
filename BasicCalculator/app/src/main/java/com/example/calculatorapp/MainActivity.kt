package com.example.calculatorapp
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var tvInput: TextView
    private lateinit var resultsTv: TextView
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
        resultsTv = findViewById(R.id.resultsTv)
    }

    fun onDigit(view: View) {
        if (view is Button) {
            tvInput.append(view.text)
            lastNumeric = true
            lastDot = false // Allow adding a new decimal point after entering another number
        }
    }

    fun onClear(view: View) {
        tvInput.text = ""
        resultsTv.text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) {
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onBackspace(view: View) {
        val currentText = tvInput.text.toString()
        if (currentText.isNotEmpty()) {
            tvInput.text = currentText.dropLast(1)
            lastNumeric = currentText.dropLast(1).isNotEmpty() && currentText.last() != '.'
            lastDot = tvInput.text.contains(".")
        }
    }

    fun onOperator(view: View) {
        if (view is Button && lastNumeric) {
            tvInput.append(view.text)
            lastNumeric = false
            lastDot = false // Reset the flag as we now expect a new number
        }
    }

    fun onEqual(view: View) {
        val tvValue = tvInput.text.toString()
        if (lastNumeric) {
            try {
                val result = calculateResult(tvValue)
                resultsTv.text = DecimalFormat("#.########").format(result)
            } catch (e: Exception) {
                resultsTv.text = "Error"
            }
        }
    }

    private fun calculateResult(expression: String): Double {
        val parser = ExpressionParser(expression)
        return parser.parseExpression()
    }
}

class ExpressionParser(private val expression: String) {
    private var currentPosition = 0
    private val totalLength = expression.length

    private fun getNextToken(): String {
        while (currentPosition < totalLength && expression[currentPosition].isWhitespace()) {
            currentPosition++
        }

        if (currentPosition == totalLength) {
            return ""
        }

        val currentChar = expression[currentPosition]
        return when {
            currentChar.isDigit() || currentChar == '.' -> {
                val start = currentPosition
                currentPosition++
                while (currentPosition < totalLength && (expression[currentPosition].isDigit() || expression[currentPosition] == '.')) {
                    currentPosition++
                }
                expression.substring(start, currentPosition)
            }
            currentChar in "+-*/()" -> {
                currentPosition++
                currentChar.toString()
            }
            else -> throw IllegalArgumentException("Unexpected character: $currentChar")
        }
    }

    private fun parsePrimary(): Double {
        val nextToken = getNextToken()
        return when {
            nextToken.isEmpty() -> throw IllegalArgumentException("Unexpected end of expression")
            nextToken == "(" -> {
                val value = parseExpression()
                if (getNextToken() != ")") throw IllegalArgumentException("Expected ')'")
                value
            }
            nextToken == "-" -> -parsePrimary()
            else -> nextToken.toDouble()
        }
    }

    private fun parseTerm(): Double {
        var value = parsePrimary()
        while (true) {
            value = when (val operator = getNextToken()) {
                "*" -> value * parsePrimary()
                "/" -> value / parsePrimary()
                "", ")", "+", "-" -> return value
                else -> throw IllegalArgumentException("Unexpected token: $operator")
            }
        }
    }

    fun parseExpression(): Double {
        val outputQueue = mutableListOf<String>()
        val operatorStack = mutableListOf<String>()

        while (true) {
            val token = getNextToken()
            if (token.isEmpty()) break

            if (token.isNumeric()) {
                outputQueue.add(token)
            } else if (token in "+-*/") {
                while (operatorStack.isNotEmpty() && hasHigherPrecedence(operatorStack.last(), token)) {
                    outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                }
                operatorStack.add(token)
            } else if (token == "(") {
                operatorStack.add(token)
            } else if (token == ")") {
                while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                    outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                }
                operatorStack.removeAt(operatorStack.size - 1) // Remove "("
            }
        }

        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
        }

        val evaluationStack = mutableListOf<Double>()
        for (element in outputQueue) {
            if (element.isNumeric()) {
                evaluationStack.add(element.toDouble())
            } else if (element in "+-*/") {
                val operand2 = evaluationStack.removeAt(evaluationStack.size - 1)
                val operand1 = evaluationStack.removeAt(evaluationStack.size - 1)
                when (element) {
                    "+" -> evaluationStack.add(operand1 + operand2)
                    "-" -> evaluationStack.add(operand1 - operand2)
                    "*" -> evaluationStack.add(operand1 * operand2)
                    "/" -> evaluationStack.add(operand1 / operand2)
                }
            }
        }

        return if (evaluationStack.size == 1) evaluationStack[0] else throw IllegalArgumentException("Invalid expression")
    }

    private fun hasHigherPrecedence(op1: String, op2: String): Boolean {
        val precedenceMap = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)
        return (precedenceMap[op1] ?: 0) >= (precedenceMap[op2] ?: 0)
    }

    private fun String.isNumeric(): Boolean {
        return this.matches("-?\\d+(\\.\\d+)?".toRegex())
    }
}
