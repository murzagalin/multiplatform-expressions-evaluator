package com.github.murzagalin.evaluator.ast

internal class DoubleAstEvaluator {

    fun evaluate(expression: Expression, values: Map<String, Any> = emptyMap()): Double {
        val baseEvaluator = AstEvaluator(values)
        val evaluated = baseEvaluator.evaluate(expression)

        require(evaluated is Number)

        return evaluated.toDouble()
    }
}