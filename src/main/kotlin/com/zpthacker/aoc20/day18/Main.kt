package com.zpthacker.aoc20.day18

import com.zpthacker.aoc20.getInputLines

fun main() {
    println(parseExpression("2 * 3 + (4 * 5)".replace(" ", "")))
    val lines = getInputLines("day18")
    var sum = 0L
    lines.forEach {
        sum += parseExpression(it.replace(" ", ""))
    }
    println(sum)
}

enum class State {
    NUMBER,
    MULTIPLY,
    ADD,
}

class ParserStack {
    val operands: MutableList<Long> = mutableListOf()
    val operator: (Long, Long) -> Long = Long::times
    var state: State = State.NUMBER

    fun push(l: Long) = operands.add(l)
    fun last() = operands.last()
    fun popAndAdd(augend: Long) {
        val addend = this.last()
        operands[operands.lastIndex] = addend + augend
    }

    val reduction
        get() = operands.reduce(operator)
}

fun parseExpression(expr: String): Long {
    val stacks = mutableListOf(ParserStack())
    var index = 0
    while (index < expr.length) {
        val token = expr[index]
        val stack = stacks.last()
        val advance = when (token) {
            in ('0'..'9') -> {
                val numberToken = expr.substring(index).takeWhile { it in ('0'..'9') }
                val number = numberToken.toLong()
                when (stack.state) {
                    State.NUMBER, State.MULTIPLY -> {
                        stacks.last().push(number)
                    }
                    State.ADD -> {
                        stack.popAndAdd(number)
                    }
                }
                numberToken.length
            }
            '*' -> {
                stack.state = State.MULTIPLY
                1
            }
            '+' -> {
                stack.state = State.ADD
                1
            }
            '(' -> {
                stacks.add(ParserStack())
                1
            }
            ')' -> {
                val stackResult = stack.reduction
                stacks.removeLast()
                val nextStack = stacks.last()
                when (nextStack.state) {
                    State.NUMBER, State.MULTIPLY -> {
                        nextStack.push(stackResult)
                    }
                    State.ADD -> {
                        nextStack.popAndAdd(stackResult)
                    }
                }
                1
            }
            else -> throw RuntimeException()
        }
        index += advance
    }
    return stacks.single().reduction
}

