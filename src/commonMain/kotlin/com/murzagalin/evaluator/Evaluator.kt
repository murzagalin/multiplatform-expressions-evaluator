package com.murzagalin.evaluator

class Evaluator(
    functions: List<Function> = DefaultFunctions.ALL,
    constants: List<Constant> = DefaultConstants.ALL,
    doubleDelimiter: Char = '.',
    argumentsDelimiter: Char = ','
) {

    private val booleanEvaluator = BooleanEvaluator()
    private val doubleEvaluator = DoubleEvaluator()
    private val converter = Converter()
    private val tokenizer = Tokenizer(
        functions = functions,
        constants = constants,
        doubleDelimiter = doubleDelimiter,
        argumentsDelimiter = argumentsDelimiter
    )

    fun evaluateDouble(
        expression: String,
        values: Map<String, Any> = emptyMap()
    ): Double {
        val tokenized = tokenizer.tokenize(expression)
        val converted = converter.convert(tokenized)

        return doubleEvaluator.evaluate(converted, values)
    }

    fun evaluateBoolean(
        expression: String,
        values: Map<String, Any> = emptyMap()
    ): Boolean {
        val tokenized = tokenizer.tokenize(expression)
        val converted = converter.convert(tokenized)

        return booleanEvaluator.evaluate(converted, values)
    }

    fun evaluateDouble(
        expression: PreprocessedExpression,
        values: Map<String, Any> = emptyMap()
    ) = doubleEvaluator.evaluate(expression, values)

    fun evaluateBoolean(
        expression: PreprocessedExpression,
        values: Map<String, Any> = emptyMap()
    ) = booleanEvaluator.evaluate(expression, values)

    fun preprocessExpression(expression: String): PreprocessedExpression {
        val tokenized = tokenizer.tokenize(expression)

        return converter.convert(tokenized)
    }
}
