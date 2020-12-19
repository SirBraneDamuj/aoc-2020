package com.zpthacker.aoc20.day19

import com.zpthacker.aoc20.getInput

fun main() {
    val (rulesString, messagesString) = getInput("day19").split("\n\n")
    val messages = messagesString.split("\n")
    val rulesMap = parseRules(rulesString, messages.maxByOrNull { it.length }!!.length)
    println("finished parsing rules")
    val rule42Matches = rulesMap["42"]!!.matches
    val rule31Matches = rulesMap["31"]!!.matches
    val result = messages.filter { message ->
        stringMatches(
            message,
            rule42Matches,
            rule31Matches
        )
    }
    println("Found ${result.count()} messages matching rule 0")
}

val matchSize = 8
fun stringMatches(s: String, rule42Matches: List<String>, rule31Matches: List<String>): Boolean {
    if (s.length % matchSize != 0) return false
    var matches: List<String> = s.chunked(matchSize)
    while (matches.isNotEmpty() && rule42Matches.contains(matches.first())) {
        matches = matches.drop(1)
        if (matches.isEmpty() || matches.count() % 2 != 0) {
            continue
        } else {
            var suffixMatches = matches.toList()
            var found = true
            while (suffixMatches.isNotEmpty()) {
                if (rule42Matches.contains(suffixMatches.first()) && rule31Matches.contains(suffixMatches.last())) {
                    suffixMatches = suffixMatches.subList(1, suffixMatches.lastIndex)
                } else {
                    found = false
                    break
                }
            }
            if (found) return true
        }
    }
    return false
}

interface Rule {
    val matches: List<String>
}

// #: "a"
class CharRule(
    private val char: Char
) : Rule {
    override val matches = listOf(char.toString())
}

// #: 23
class SingleRule(
    private val rule: Rule,
) : Rule {
    override val matches = rule.matches
}

// #: 23 24
class DoubleRule(
    private val rule1: Rule,
    private val rule2: Rule
) : Rule {
    override val matches = mutableListOf<String>().apply {
        for (i in rule1.matches) {
            for (j in rule2.matches) {
                this.add(i + j)
            }
        }
    }
}

// #: 23 | 24
// #: 23 24 | 25 26
class OrRule(
    private val rule1: Rule,
    private val rule2: Rule
) : Rule {
    override val matches = mutableListOf<String>().apply {
        this.addAll(rule1.matches)
        this.addAll(rule2.matches)
    }
}

fun parseRules(rulesString: String, maxMessageLength: Int): Map<String, Rule> {
    val rulesMap = mutableMapOf<String, Rule>()
    val rulesTokens = rulesString
        .split("\n")
        .map { it.split(" ") }
        .filterNot { it.first() == "8:" || it.first() == "11:" || it.first() == "0:" }
        .toMutableList()
    while (rulesTokens.isNotEmpty()) {
        val nextIndex = rulesTokens.indexOfFirst { tokens ->
            tokens.drop(1).all { token ->
                token.contains("\"") ||
                        token == "|" ||
                        rulesMap.containsKey(token)
            }
        }
        if (nextIndex == -1) {
            error("oh dear I couldn't parse any more rules.")
        }
        val tokens = rulesTokens[nextIndex]
        val name = tokens.first().dropLast(1)
        val rule = when {
            tokens[1].contains("\"") -> CharRule(
                tokens[1].drop(1).dropLast(1).single()
            )
            tokens.contains("|") -> {
                val lhs = tokens.drop(1).takeWhile { it != "|" }
                val rhs = tokens.drop(lhs.count() + 2)
                val lhsRule = when (lhs.count()) {
                    1 -> SingleRule(rulesMap[lhs.single()]!!)
                    2 -> DoubleRule(
                        rulesMap[lhs[0]]!!,
                        rulesMap[lhs[1]]!!
                    )
                    else -> error("an 'OR' rule I can't handle")
                }
                val rhsRule = when (rhs.count()) {
                    1 -> SingleRule(rulesMap[rhs.single()]!!)
                    2 -> DoubleRule(
                        rulesMap[rhs[0]]!!,
                        rulesMap[rhs[1]]!!
                    )
                    else -> error("an 'OR' rule I can't handle")
                }
                OrRule(lhsRule, rhsRule)
            }
            tokens.count() == 3 -> DoubleRule(
                rulesMap[tokens[1]]!!,
                rulesMap[tokens[2]]!!
            )
            tokens.count() == 2 -> SingleRule(
                rulesMap[tokens[1]]!!
            )
            else -> error("a rule I'm not able to handle")
        }
        rulesMap[name] = rule
        rulesTokens.removeAt(nextIndex)
    }
    return rulesMap
}
