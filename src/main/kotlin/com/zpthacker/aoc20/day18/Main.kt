package com.zpthacker.aoc20.day18

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day18")
    var sum = 0L
    lines.forEach {
        sum += parseExpression(it)
    }
    println(sum)
}

fun parseExpression(expr: String): Long {
    var accumulator = 0L
    var i = 0
    var numberAccumulator = ""
    var state = State.LHS
    var lhs: Long? = null
    var operator: ((Long, Long) -> Long)? = null
    while (i <= expr.length) {
        val token = if (i <= expr.lastIndex) expr[i] else '\n'
        if (state == State.LHS && token == '\n') break
        when (token) {
            '(' -> {
                var parenCount = 1
                val subExpr = expr.substring(i + 1).takeWhile {
                    if (it == '(') {
                        parenCount++
                    } else if (it == ')') {
                        parenCount--
                    }
                    parenCount != 0
                }
                when (state) {
                    State.LHS -> {
                        accumulator = parseExpression(subExpr)
                        numberAccumulator = accumulator.toString()
                    }
                    State.RHS -> {
                        accumulator = operator!!.invoke(lhs!!, parseExpression(subExpr))
                        numberAccumulator = accumulator.toString()
                        state = State.LHS
                    }
                    else -> throw RuntimeException()
                }
                i += subExpr.length + 1
            }
            in ('0'..'9') -> {
                numberAccumulator += expr[i]
            }
            '+' -> {
                if (state != State.OPERATOR) throw RuntimeException()
                operator = Long::plus
            }
            '*' -> {
                if (state != State.OPERATOR) throw RuntimeException()
                operator = Long::times
            }
            ' ' -> {
                when (state) {
                    State.LHS -> {
                        lhs = numberAccumulator.toLong()
                        numberAccumulator = ""
                        state = State.OPERATOR
                    }
                    State.RHS -> {
                        accumulator = operator!!.invoke(lhs!!, numberAccumulator.toLong())
                        numberAccumulator = ""
                        lhs = accumulator
                        state = State.OPERATOR
                    }
                    State.OPERATOR -> {
                        state = State.RHS
                    }
                }
            }
            '\n' -> {
                if (state == State.RHS) {
                    accumulator = operator!!.invoke(lhs!!, numberAccumulator.toLong())
                }
            }
        }
        i++
    }
    return accumulator
}

enum class State {
    LHS,
    RHS,
    OPERATOR
}
