package com.github.murzagalin.evaluator.ast

internal class BooleanAstEvaluator {

    fun evaluate(expression: Expression, values: Map<String, Any> = emptyMap()): Boolean {
        val baseEvaluator = AstEvaluator(values)
        val evaluated = baseEvaluator.evaluate(expression)

        require(evaluated is Boolean)

        return evaluated
    }
}