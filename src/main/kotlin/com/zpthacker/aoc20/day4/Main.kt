package com.zpthacker.aoc20.day4

import com.zpthacker.aoc20.getInput

fun main() {
    val passportStrings = getInput("day4").split("\n\n")
    println("Part 1")
    val validRequiredFields = passportStrings.count {
        val fields = findFieldsInPassportString(it)
        validatePassportRequiredFields(fields)
    }
    println("Found $validRequiredFields passports with the required fields")
    println("Part 2")
    val validCount = passportStrings.count {
        val fields = findFieldsInPassportString(it)
        validatePassportFieldsAndValues(fields)
    }
    println("Found $validCount passports with valid fields")
}

fun findFieldsInPassportString(passportString: String): Map<String, String> {
    return passportString
        .split(Regex("[\n ]"))
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

fun validatePassportRequiredFields(fields: Map<String, String>) =
    fields.keys.containsAll(requiredFields)

fun validatePassportFieldsAndValues(fields: Map<String, String>): Boolean {
    if (!validatePassportRequiredFields(fields)) {
        return false
    }
    return fields.all { (fieldName, value) ->
        when (fieldName) {
            "byr" -> value.toInt() in 1920..2002
            "iyr" -> value.toInt() in 2010..2020
            "eyr" -> value.toInt() in 2020..2030
            "hgt" -> validateHeight(value)
            "hcl" -> value.matches(Regex("#[a-f0-9]{6}"))
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
    } catch (e: NumberFormatException) {
        return false
    }
    return when (units) {
        "in" -> value in 59..76
        "cm" -> value in 150..193
        else -> false
    }
}
