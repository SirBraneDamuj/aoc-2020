package com.zpthacker.aoc20.day22

import com.zpthacker.aoc20.getInput
import java.util.*

fun main() {
    val playerChunks = getInput("day22").split("\n\n")

    val players = playerChunks.mapIndexed { index, chunk ->
        val cards = chunk
            .split("\n")
            .drop(1)
            .filter(String::isNotBlank)
            .map(String::toInt)
            .let(::ArrayDeque)
        Player(index, cards)
    }

    val previousRounds = mutableSetOf<String>()
    previousRounds.add(roundState(players))

    val winningPlayer = recursiveCombat(players)

    val winningDeck = winningPlayer.cards.toList()
    var score = 0
    for (cardIndex in winningDeck.indices) {
        score += (winningDeck.count() - cardIndex) * winningDeck[cardIndex]
    }
    println(score)
}

fun recursiveCombat(
    players: List<Player>,
    depth: Int = 0
): Player {
    var roundCount = 0
    val previousRounds = mutableSetOf(roundState(players))
    while (players.all { it.cards.isNotEmpty() }) {
        roundCount++
        val playerCards = players.map(Player::draw)
        val winner = when {
            players.all { it.cards.count() >= playerCards[it.number] } -> {
                val newPlayers = players.map {
                    Player(it.number, ArrayDeque(it.cards.take(playerCards[it.number])))
                }
                recursiveCombat(newPlayers, depth + 1).number
            }
            else -> {
                playerCards.indices.maxByOrNull { playerCards[it] } ?: error("this is impossible")
            }
        }
        val newCards = listOf(playerCards[winner], playerCards[1 - winner])
        players[winner].take(newCards)
        val state = roundState(players)
        if (previousRounds.contains(state)) {
            return players[0]
        } else {
            previousRounds.add(state)
        }
    }
    return players.single { it.cards.isNotEmpty() }
}

fun roundState(players: List<Player>) = players
    .joinToString(" ") { player ->
        player.cards.joinToString(",") { it.toString() }
    }


class Player(
    val number: Int,
    val cards: Deque<Int>
) {
    fun draw(): Int = cards.remove()

    fun take(newCards: List<Int>) {
        cards.addAll(newCards)
    }
}

