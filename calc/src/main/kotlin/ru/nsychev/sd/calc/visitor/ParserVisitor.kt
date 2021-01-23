package ru.nsychev.sd.calc.visitor

import ru.nsychev.sd.calc.ExpectedRightBracketException
import ru.nsychev.sd.calc.UnexpectedRightBracketException
import ru.nsychev.sd.calc.token.Brace
import ru.nsychev.sd.calc.token.BraceType
import ru.nsychev.sd.calc.token.NumberToken
import ru.nsychev.sd.calc.token.Operation
import ru.nsychev.sd.calc.token.Token

class ParserVisitor : TokenVisitor {
    private val postfixNotation: MutableList<Token> = mutableListOf()
    private val toBeUsed: MutableList<Token> = mutableListOf()

    override fun visit(token: NumberToken) {
        postfixNotation.add(token)
    }

    override fun visit(token: Brace) {
        when (token.type) {
            BraceType.LEFT -> toBeUsed.add(token)
            BraceType.RIGHT -> {
                while (true) {
                    if (toBeUsed.size == 0) {
                        throw UnexpectedRightBracketException()
                    }

                    when (val next = toBeUsed.removeLast()) {
                        is Brace -> break
                        else -> postfixNotation.add(next)
                    }
                }
            }
        }
    }

    override fun visit(token: Operation) {
        while (toBeUsed.size > 0) {
            val top = toBeUsed.last()

            if (top !is Operation) {
                break
            }

            if (top.type.priority >= token.type.priority) {
                toBeUsed.removeLast()
                postfixNotation.add(toBeUsed.removeLast())
            }
        }

        toBeUsed.add(token)
    }

    fun getResult(): List<Token> {
        while (toBeUsed.size > 0) {
            when (val top = toBeUsed.removeLast()) {
                is Operation -> postfixNotation.add(top)
                else -> throw ExpectedRightBracketException()
            }
        }

        return postfixNotation
    }
}
