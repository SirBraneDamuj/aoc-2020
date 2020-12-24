package com.zpthacker.aoc20.day23

import java.lang.Integer.min
import kotlin.math.abs

fun main() {
//    val input = "389125467"
//    val times = 10
    val input = "362981754"
    val times = 10_000_000
    val cupList = input.split("")
        .filter(String::isNotBlank)
        .map(String::toLong)
    val cups = HashMap<Long, Node>(1_000_000).apply {
        cupList.forEachIndexed { i, l ->
            this[l] = Node(
                l,
                cupList.modAccess(i + 1)
            )
        }
        (cupList.maxOrNull()!! + 1..1_000_000).forEach { l ->
            this[l] = Node(l, l + 1)
        }
        this[1000000]!!.next = cupList.first()
        this[cupList.last()]!!.next = 10L
    }

    val minCup = 1L
    val maxCup = 1_000_000L
    var currentCup = cups[maxCup]!!.next

    repeat(times) { move ->
        val node = cups[currentCup]!!
        val f = cups[node.next]!!
        val s = cups[f.next]!!
        val t = cups[s.next]!!
        node.next = t.next
        var insertionCup: Long = currentCup
        do {
            insertionCup -= 1
            if (insertionCup < minCup) {
                insertionCup = maxCup
            }
        } while (insertionCup == f.value || insertionCup == s.value || insertionCup == t.value)
        val insertionNode = cups[insertionCup]!!
        t.next = insertionNode.next
        insertionNode.next = f.value
        currentCup = node.next
        if (move % 100 == 0) println(move)
    }
    println("todomeda")
    val oneNode = cups[1L]!!
    val first = cups[oneNode.next]!!
    val second = cups[first.next]!!
    println(first.value)
    println(second.value)
    println(first.value * second.value)
}

fun <V> Map<Long, V>.modAccess(k: Long) = this[k % this.count()]!!

fun <T> List<T>.modAccess(i: Int) = this[i % this.count()]

class Node(
    val value: Long,
    var next: Long
)

