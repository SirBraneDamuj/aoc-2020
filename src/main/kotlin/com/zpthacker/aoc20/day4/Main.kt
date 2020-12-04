package com.zpthacker.aoc20.day4

import com.zpthacker.aoc20.getInput
import java.lang.NumberFormatException
import java.lang.RuntimeException

fun main() {
    val passportStrings = getInput("day4").split("\n\n")
    val validCount = passportStrings.count {
        val fields = findFieldsInPassportString(it)
        validatePassportFields(fields)
    }
    println(validCount)
}

fun findFieldsInPassportString(passportString: String): Map<String, String> {
    return passportString.split(Regex("[\n ]"))
        .mapNotNull {
            val tokens = it.split(":")
            if (tokens.count() != 2) {
                null
            } else {
                val (name, value) = tokens
                Pair(name, value)
            }
        }
        .toMap()
}

val requiredFields = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
val validEyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

fun validatePassportFields(fields: Map<String, String>): Boolean {
    val present = fields.keys.containsAll(requiredFields)
    if (!present) {
        return false
    }
    return fields.all { (fieldName, value) ->
        when (fieldName) {
            "byr" -> value.toInt() in 1920..2002
            "iyr" -> value.toInt() in 2010..2020
            "eyr" -> value.toInt() in 2020..2030
            "hgt" -> validateHeight(value)
            "hcl" -> validateColor(value)
            "ecl" -> value in validEyeColors
            "pid" -> value.matches(Regex("[0-9]{9}"))
            "cid" -> true
            else -> false
        }
    }
}

fun validateHeight(height: String): Boolean {
    val units = height.takeLast(2)
    val value = try {
        height.dropLast(2).toInt()
    } catch(e: NumberFormatException) {
        return false
    }
    return when(units) {
        "in" -> value in 59..76
        "cm" -> value in 150..193
        else -> false
    }
}

fun validateColor(color: String): Boolean {
    return color.matches(Regex("#[a-f0-9]{6}"))
}
