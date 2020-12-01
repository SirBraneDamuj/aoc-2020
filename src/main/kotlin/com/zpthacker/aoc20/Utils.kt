package com.zpthacker.aoc20

import java.io.File

fun getFile(s: String) = File("input/${s}.txt")

fun getInput(s: String) = getFile(s).readText()

fun getInputLines(s: String) = getFile(s).readLines()

