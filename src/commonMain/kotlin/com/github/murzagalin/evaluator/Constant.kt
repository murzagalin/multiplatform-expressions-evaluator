package com.github.murzagalin.evaluator

data class Constant(val name: String, val value: Double)

object DefaultConstants {
    val PI = Constant("pi", kotlin.math.PI)
    val E = Constant("e", kotlin.math.E)

    val ALL = listOf(PI, E)
}