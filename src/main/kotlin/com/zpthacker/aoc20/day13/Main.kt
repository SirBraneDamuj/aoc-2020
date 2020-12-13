package com.zpthacker.aoc20.day13

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day13")
    val (timeString, busIdString) = lines
    val time = timeString.toInt()
    val buses = busIdString
        .split(",")
        .filter { it != "x" }
        .sorted()
        .map {
            Bus(it.toInt())
        }
    var complete = false
    while (!complete) {
        val waiting = buses
            .filter { it.departures.last() < time }
        waiting
            .forEach(Bus::addDeparture)
        complete = buses.all { it.departures.last() >= time }
    }
    buses
        .minByOrNull { bus ->
            bus.departures.maxOrNull()!!
        }!!
        .let {
            println(it.id * (it.departures.last() - time))
        }
}

class Bus(
    val id: Int
) {
    var departures = mutableListOf(id)

    fun addDeparture() {
        departures.add(departures.last() + id)
    }
}
