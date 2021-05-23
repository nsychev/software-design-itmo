package ru.nsychev.sd.reactive.entity

enum class Currency {
    EUR,
    USD,
    RUB;
}

fun Currency.toRoubles() = when (this) {
    Currency.EUR -> 90
    Currency.USD -> 75
    Currency.RUB -> 1
}

fun Double.convert(src: Currency, dst: Currency): Double {
    return this * src.toRoubles() / dst.toRoubles()
}
