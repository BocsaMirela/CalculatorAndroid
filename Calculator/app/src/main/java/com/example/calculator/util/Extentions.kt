package com.example.calculator.util

import android.text.SpannableString
import net.objecthunter.exp4j.operator.Operator

fun String.toSpannableString(): SpannableString {
    return SpannableString(this)
}

fun factorial(): Operator {
    return object : Operator("!", 1, true, PRECEDENCE_POWER + 1) {

        override fun apply(vararg args: Double): Double {
            val arg = args[0].toInt()
            if (arg.toDouble() != args[0]) {
                throw IllegalArgumentException("Operand for factorial has to be an integer")
            }
            if (arg < 0) {
                throw IllegalArgumentException("The operand of the factorial can not be less than zero")
            }
            var result = 1.0
            for (i in 1..arg) {
                result *= i.toDouble()
            }
            return result
        }
    }
}