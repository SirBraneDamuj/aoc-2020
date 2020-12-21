package com.zpthacker.aoc20.day21

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day21")
    val allIngredients = mutableSetOf<String>()
    val allergenIngredients = mutableMapOf<String, Set<String>>()
    val foods = lines.map { line ->
        val tokens = line.split(" ")
        val ingredients = tokens
            .takeWhile { !it.contains("(") }
            .toSet()
        val allergens = tokens
            .drop(ingredients.count())
            .drop(1)
            .map { it.replace(Regex("[,)]"), "") }
            .toSet()
        allIngredients.addAll(ingredients)
        allergens.forEach { allergen ->
            allergenIngredients.computeIfAbsent(allergen) { ingredients }
            allergenIngredients.computeIfPresent(allergen) { _, possibleIngredients ->
                possibleIngredients.intersect(ingredients)
            }
        }
        Food(ingredients, allergens)
    }
    val allergensToCheck = allergenIngredients.keys
    val ingredientToAllergen = mutableMapOf<String, String>()
    while (allergensToCheck.isNotEmpty()) {
        val next = allergensToCheck.find { allergenIngredients.getValue(it).count() == 1 }!!
        val ingredient = allergenIngredients.getValue(next).single()
        ingredientToAllergen[ingredient] = next
        allergensToCheck.forEach { otherAllergen ->
            allergenIngredients.computeIfPresent(otherAllergen) { _, set ->
                set.minus(ingredient)
            }
        }
        allergensToCheck.remove(next)
    }
    val result = foods.sumBy { food ->
        food.ingredients.count { ingredient ->
            !ingredientToAllergen.keys.contains(ingredient)
        }
    }
    println(result)
    val dangerousIngredients = ingredientToAllergen.keys
        .sortedBy {
            ingredientToAllergen.getValue(it)
        }
        .joinToString(",")
    println(dangerousIngredients)
}

class Food(
    val ingredients: Set<String>,
    val allergens: Set<String>
) {

}

