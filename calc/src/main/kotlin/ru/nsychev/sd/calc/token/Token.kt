package ru.nsychev.sd.calc.token

import ru.nsychev.sd.calc.visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

class NumberToken(val value: Int) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

enum class BraceType {
    LEFT,
    RIGHT
}

class Brace(val type: BraceType) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

enum class OperationType(val value: String, val priority: Int) {
    ADD("+", 1),
    SUBTRACT("-", 1),
    MULTIPLY("*", 2),
    DIVIDE("/", 2)
}

class Operation(val type: OperationType) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)

    }
}
