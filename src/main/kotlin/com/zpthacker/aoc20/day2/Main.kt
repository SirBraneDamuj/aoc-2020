package com.zpthacker.aoc20.day2

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day2")
    println("Part 1: Repeating Letter Policy")
    val part1Count = validPasswordCountForPasswordPolicy(lines, RepeatingLetterPolicyFactory)
    println("Found $part1Count valid passwords.")
    println("Part 2: Letter Position Xor Policy")
    val part2Count = validPasswordCountForPasswordPolicy(lines, LetterPositionPolicyFactory)
    println("Found $part2Count valid passwords.")
}

fun validPasswordCountForPasswordPolicy(
    passwordStrings: List<String>,
    policyFactory: PolicyFactory
) = passwordStrings
    .map { passwordForLine(it, policyFactory) }
    .filter(Password::valid)
    .count()

fun passwordForLine(s: String, policyFactory: PolicyFactory): Password {
    val (firstPolicyToken, secondPolicyToken, passwordToken) = s.split(" ")
    return Password(
        passwordString = passwordToken,
        policy = policyFactory.createPolicy(Pair(firstPolicyToken, secondPolicyToken))
    )
}

fun String.toIntRange() = this
    .split("-")
    .let { (low, high) ->
        low.toInt()..high.toInt()
    }

class Password(
    val passwordString: String,
    private val policy: Policy
) {
    fun valid() = policy.passwordIsValid(passwordString)
}

interface Policy {
    fun passwordIsValid(password: String): Boolean
}

interface PolicyFactory {
    fun createPolicy(policyTokens: Pair<String, String>): Policy
}

class RepeatingLetterPolicy(
    private val letter: Char,
    private val range: IntRange
) : Policy {
    override fun passwordIsValid(password: String): Boolean {
        val count = password.count { it == letter }
        return count in range
    }
}

object RepeatingLetterPolicyFactory : PolicyFactory {
    override fun createPolicy(policyTokens: Pair<String, String>): Policy {
        val (rangeToken, letterToken) = policyTokens
        return RepeatingLetterPolicy(
            letter = letterToken.first(),
            range = rangeToken.toIntRange()
        )
    }
}

class LetterPositionPolicy(
    private val letter: Char,
    private val firstPosition: Int,
    private val secondPosition: Int
) : Policy {
    override fun passwordIsValid(password: String): Boolean {
        val firstLetter = password.getOrNull(firstPosition - 1)
        val secondLetter = password.getOrNull(secondPosition - 1)
        return (letter == firstLetter) xor (letter == secondLetter)
    }
}

object LetterPositionPolicyFactory : PolicyFactory {
    override fun createPolicy(policyTokens: Pair<String, String>): Policy {
        val (positionsToken, letterToken) = policyTokens
        val (firstPosition, secondPosition) = positionsToken.split("-")
        return LetterPositionPolicy(
            letter = letterToken.first(),
            firstPosition = firstPosition.toInt(),
            secondPosition = secondPosition.toInt()
        )
    }
}
