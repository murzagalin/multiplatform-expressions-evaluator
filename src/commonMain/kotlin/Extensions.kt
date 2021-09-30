import evaluator.BooleanEvaluator
import evaluator.DoubleEvaluator
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.sin

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

val COS = Token.Function("cos", 1) {
    val operand = it[0]
    require(operand is Token.Operand.Num)

    return@Function Token.Operand.Num(cos(operand.value))
}

val SIN = Token.Function("sin", 1) {
    val operand = it[0]
    require(operand is Token.Operand.Num)

    return@Function Token.Operand.Num(sin(operand.value))
}

val LN = Token.Function("ln", 1) {
    val operand = it[0]
    require(operand is Token.Operand.Num)

    return@Function Token.Operand.Num(ln(operand.value))
}

val LOG = Token.Function("log", 2) {
    val operand = it[0]
    val base = it[1]
    require(operand is Token.Operand.Num)
    require(base is Token.Operand.Num)

    return@Function Token.Operand.Num(log(operand.value, base.value))
}

val DEFAULT_FUNCTIONS = listOf(COS, SIN, LN, LOG)