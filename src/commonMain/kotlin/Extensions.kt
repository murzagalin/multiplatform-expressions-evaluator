

fun String.evaluate(
    tokenizer: Tokenizer = Tokenizer(),
    converter: Converter = Converter(),
    evaluator: Evaluator = Evaluator()
): Double {
    val tokenized = tokenizer.tokenize(this)
    val converted = converter.convert(tokenized)

    return evaluator.evaluate(converted)
}