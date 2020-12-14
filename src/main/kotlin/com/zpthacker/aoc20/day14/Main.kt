package com.zpthacker.aoc20.day14

import com.zpthacker.aoc20.getInputLines
import kotlin.math.pow

fun main() {
    val lines = getInputLines("day14")
    var mask = maskFromMaskLine(lines.first())
    val memory = mutableMapOf<Long, Long>()
    lines.drop(1).forEach { line ->
        when {
            line.startsWith("mask") -> {
                mask = maskFromMaskLine(line)
            }
            line.startsWith("mem") -> {
                val (address, value) = memoryAssignmentForLine(mask, line)
                memory[address] = value
            }
        }
    }
    val answer = memory.values.sum()
    println(answer)
}

fun maskFromMaskLine(line: String) = line.split(" ").last()

fun memoryAssignmentForLine(mask: String, line: String): Pair<Long, Long> {
    val tokens = line.split(" ")
    val address = tokens
        .first()
        .drop(4)
        .dropLast(1)
        .toLong()
    val value = tokens.last().toLong()
    val bits = numberAsBits(value, 36)
    val maskedBits = bits
        .mapIndexed { index, c ->
            if (mask[index] != 'X') {
                mask[index]
            } else {
                c
            }
        }
        .joinToString("")
    return Pair(address, bitsAsNumber(maskedBits))
}

fun numberAsBits(number: Long, numBits: Int): String {
    var x = number.toDouble()
    return ((numBits - 1) downTo 0).joinToString("") {
        val subtrahend = 2.0.pow(it)
        if (x >= subtrahend) {
            x -= subtrahend
            "1"
        } else {
            "0"
        }
    }
}

fun bitsAsNumber(bits: String): Long {
    return bits
        .foldIndexed(0.0) { index, acc, c ->
            if (c == '1') {
                acc + Math.pow(2.0, (35 - index).toDouble())
            } else {
                acc
            }
        }
        .toLong()
}
