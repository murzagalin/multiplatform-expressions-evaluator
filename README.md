# k-x-v

## Example

```kotlin
//numeric values
"23 + 0.123 * (124 / -60.124)".evaluateDouble()

//boolean values
"x > 1 && var".evaluateBoolean(
    values = mapOf("x" to 3, "var" to true)
)

//functions
"1 + cos(2) + sin(3)".evaluateDouble()

//variables
"1 + cos(x) + sin(x)".evaluateDouble()
```

## Operators
The library supports the following operators and special symbols:

* `+`, `-`, `*`, `/` - mathematical operators
* `&&`, `||`, `!` - logical 'and', 'or', 'not' operators
* `==`, `!=` - equality operators 
* `<`, `>`, `<=`, `>=` - comparison operators

## Functions
Currently supported functions:
* `cos(x)`, `sin(x)`, `tan(x)` - trigonometric functions
* `ln(x)`, `log(x, base)` - logarithm

## Variables
Any symbols other than function calls are resolved as arguments/variables during evaluation.

Example:
```kotlin
val expression = "1 + cos(x) + sin(x)"
expression.evaluateDouble(values = mapOf("x" to 2.0, "y" to 3.0))
```