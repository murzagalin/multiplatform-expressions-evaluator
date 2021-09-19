import evaluator.BooleanEvaluator
import evaluator.DoubleEvaluator

fun String.evaluateDouble(
    values: Map<String, Any> = emptyMap(),
    tokenizer: Tokenizer = Tokenizer(),
    converter: Converter = Converter(),
    evaluator: DoubleEvaluator = DoubleEvaluator()
): Double {
    val tokenized = tokenizer.tokenize(this)
    val converted = converter.convert(tokenized)

    return evaluator.evaluate(converted, values)
}

fun String.evaluateBoolean(
    values: Map<String, Any> = emptyMap(),
    tokenizer: Tokenizer = Tokenizer(),
    converter: Converter = Converter(),
    evaluator: BooleanEvaluator = BooleanEvaluator()
): Boolean {
    val tokenized = tokenizer.tokenize(this)
    val converted = converter.convert(tokenized)

    return evaluator.evaluate(converted, values)
}