package com.zpthacker.aoc20.day22

import com.zpthacker.aoc20.getInput

fun main() {
    val playerChunks = getInput("day22").split("\n\n")

    val players = playerChunks.mapIndexed { index, chunk ->
        val cards = chunk
            .split("\n")
            .drop(1)
            .filter(String::isNotBlank)
            .map(String::toInt)
        Player(
            index + 1,
            cards
        )
    }

    while (players.all { it.cards.isNotEmpty() }) {
        val playerCards = mutableMapOf<Int, Int>().apply {
            players.forEachIndexed { i, player ->
                this[i] = player.draw()
            }
        }
        val (playerIndex, card) = playerCards.entries.maxByOrNull { (_, card) -> card } ?: error("this is impossible")
        val remainingCards = playerCards.values
        remainingCards.remove(card)
        val newCards = listOf(card) + remainingCards
        players[playerIndex].take(newCards)
    }

    val winningPlayer = players.single { it.cards.isNotEmpty() }
    val winningDeck = winningPlayer.cards
    var score = 0
    for (cardIndex in winningDeck.indices) {
        score += (winningDeck.count() - cardIndex) * winningDeck[cardIndex]
    }
    println(score)
}

class Player(
    val number: Int,
    var cards: List<Int>
) {
    fun draw() = cards.first()
        .also {
            cards = cards.drop(1)
        }

    fun take(newCards: List<Int>) {
        cards += newCards
    }
}

