package ru.nsychev.sd.calc.visitor

import ru.nsychev.sd.calc.token.Brace
import ru.nsychev.sd.calc.token.NumberToken
import ru.nsychev.sd.calc.token.Operation

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: Brace)
    fun visit(token: Operation)
}
