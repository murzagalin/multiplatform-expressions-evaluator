# Multiplatform expressions evaluator

This is a kotlin multiplatform runtime [infix](https://en.wikipedia.org/wiki/Infix_notation) expressions evaluator.

## Overview

### Operators
The library supports the following operators and special symbols:

* `+`, `-`, `*`, `/`, `% (modulo)`, `^ (exponentiation)` - mathematical operators
* `&&`, `||`, `!` - logical 'and', 'or', 'not' operators
* `==`, `!=` - equality operators
* `<`, `>`, `<=`, `>=` - comparison operators
* `a ? b : c` - ternary operator

### Constants
The library supports the following constants:
* `pi` - [PI](https://en.wikipedia.org/wiki/Pi)
* `e` - [e](https://en.wikipedia.org/wiki/E_(mathematical_constant))
* any custom constants.

### Functions
The library supports the following functions:
* `abs(x)` - [absolute value](https://en.wikipedia.org/wiki/Absolute_value)
* `acos(x)` - [arccosine](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions)
* `asin(x)` - [arcsine](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions)
* `atan(x)` - [arctangent](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions)
* `avg(a1, a2, a3, ..., an)` - average of N values
* `ceil(x)` - [ceiling](https://en.wikipedia.org/wiki/Floor_and_ceiling_functions)
* `cos(x)` - [cosine](https://en.wikipedia.org/wiki/Trigonometric_functions)
* `cosh(x)` - [hyperbolic cosine](https://en.wikipedia.org/wiki/Hyperbolic_functions)
* `floor(x)` - [floor](https://en.wikipedia.org/wiki/Floor_and_ceiling_functions)
* `ln(x)` - [natural logarithm](https://en.wikipedia.org/wiki/Natural_logarithm)
* `log(x, base)` - [logarithm](https://en.wikipedia.org/wiki/Logarithm)
* `max(a1, a2, a3, ..., an)` - maximum of N values
* `min(a1, a2, a3, ..., an)` - minimum of N values
* `round(x)` - [rounding to the nearest integer](https://en.wikipedia.org/wiki/Rounding#Rounding_to_the_nearest_integer)
* `sin(x)` - [sine](https://en.wikipedia.org/wiki/Trigonometric_functions)
* `sinh(x)` - [hyperbolic sine](https://en.wikipedia.org/wiki/Hyperbolic_functions)
* `sum(a1, a2, a3, ..., an)` - summation of N values
* `tan(x)` - [tangent](https://en.wikipedia.org/wiki/Trigonometric_functions)
* `tanh(x)` - [hyperbolic tangent](https://en.wikipedia.org/wiki/Trigonometric_functions)
* any custom function with any number of arguments

### Variables
Any symbols other than constants and function calls are resolved as variables during evaluation.

## How to get

### Gradle


Kotlin DSL
```kotlin
repositories {
    maven("https://maven.pkg.github.com/azamat-murzagalin/multiplatform-expressions-evaluator") {
        credentials {
            username = "<your github username>"
            password = "<your github token>"
        }
    }
}

dependencies {
    implementation("com.murzagalin:multiplatform-expressions-evaluator:0.8.2")
}
```

Groovy
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/azamat-murzagalin/multiplatform-expressions-evaluator")
        credentials {
            username = "<your github username>"
            password = "<your github token>"
        }
    }
}

dependencies {
    implementation "com.murzagalin:multiplatform-expressions-evaluator:0.8.2"
}
```
## Example

```kotlin
import com.murzagalin.evaluator.Evaluator

val evaluator = Evaluator()

//numeric values
evaluator.evaluateDouble("23 + 0.123 * (124 / -60.124)")

//boolean values
evaluator.evaluateBoolean(
    "x > 1 && var",
    mapOf("x" to 3, "var" to true)
)

//variables
evaluator.evaluateDouble(
    "1 + x + y",
    mapOf("x" to 3, "y" to 4)
)

//functions
evaluator.evaluateDouble(
    "sin(x)^2+cos(x)^2",
    mapOf("x" to 2.0)
)

//constants
evaluator.evaluateDouble("cos(pi)")
evaluator.evaluateDouble("ln(e^3)")

/* 
More: 
* custom functions
* custom constants
*/
```
