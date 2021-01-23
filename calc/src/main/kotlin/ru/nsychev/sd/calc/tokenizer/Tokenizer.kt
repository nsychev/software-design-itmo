package ru.nsychev.sd.calc.tokenizer

import ru.nsychev.sd.calc.InvalidCharacterException
import ru.nsychev.sd.calc.token.Brace
import ru.nsychev.sd.calc.token.BraceType
import ru.nsychev.sd.calc.token.NumberToken
import ru.nsychev.sd.calc.token.Operation
import ru.nsychev.sd.calc.token.OperationType
import ru.nsychev.sd.calc.token.Token

class Tokenizer {
    private var currentState: TokenizerState = StartState()
    private val tokens: MutableList<Token> = mutableListOf()

    fun feed(c: Int) {
        if (Character.isWhitespace(c)) {
            return
        }

        when (currentState) {
            is StartState -> {
                when {
                    Character.isDigit(c) -> {
                        currentState = NumberState().apply {
                            add(c.toChar())
                        }
                    }

                    c == 40 -> tokens.add(Brace(BraceType.LEFT))
                    c == 41 -> tokens.add(Brace(BraceType.RIGHT))
                    c == 43 -> tokens.add(Operation(OperationType.ADD))
                    c == 45 -> tokens.add(Operation(OperationType.SUBTRACT))
                    c == 42 -> tokens.add(Operation(OperationType.MULTIPLY))
                    c == 47 -> tokens.add(Operation(OperationType.DIVIDE))
                    c == -1 -> currentState = EndState()

                    else -> {
                        currentState = ErrorState()
                        throw InvalidCharacterException(c.toChar())
                    }
                }
            }
            is NumberState -> {
                if (Character.isDigit(c)) {
                    (currentState as NumberState).add(c.toChar())
                } else {
                    tokens.add(NumberToken((currentState as NumberState).get()))
                    currentState = StartState()
                    feed(c)
                }
            }
            is ErrorState -> {
                throw IllegalArgumentException("Can't parse characters in error state.")
            }
            is EndState -> {
                throw IllegalArgumentException("Can't parse characters after EOF.")
            }
        }
    }

    fun getResult(): List<Token> {
        return tokens
    }
}
