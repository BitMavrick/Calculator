package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View){
        val workingsTV = findViewById<TextView>(R.id.workingsTV)
        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal)
                    workingsTV.append(view.text)
                canAddDecimal = false
            }
            else
                workingsTV.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View){
        val workingsTV = findViewById<TextView>(R.id.workingsTV)
        if(view is Button && canAddOperation){
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        val workingsTV = findViewById<TextView>(R.id.workingsTV)
        val resultsTV = findViewById<TextView>(R.id.resultsTV)

        workingsTV.text = ""
        resultsTV.text = ""

    }
    fun backSpaceAction(view: View) {
        val workingsTV = findViewById<TextView>(R.id.workingsTV)

        val length = workingsTV.length()

        if(length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View) {
        val resultsTV = findViewById<TextView>(R.id.resultsTV)

        resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitOperators()

        if(digitsOperators.isEmpty()){
            return ""
        }

        val timeDivision = timesDivisionCalculate(digitsOperators)

        if(timeDivision.isEmpty()){
            return ""
        }

        val result = addSubtractCalculator(timeDivision)
        return result.toString()
    }

    private fun addSubtractCalculator(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float

                if(operator == '+')
                    result += nextDigit

                if(operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('X') || list.contains('/')){
            list = calcTimeDiv(list)
        }
        return list
    }

    private fun calcTimeDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when(operator){
                    'X' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitOperators(): MutableList<Any>
    {
        val workingsTV = findViewById<TextView>(R.id.workingsTV)
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for(character in workingsTV.text){
            if(character.isDigit() || character == '.'){
                currentDigit += character
            }else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }

}











