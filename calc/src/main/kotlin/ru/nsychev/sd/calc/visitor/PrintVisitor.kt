package ru.nsychev.sd.calc.visitor

import ru.nsychev.sd.calc.token.Brace
import ru.nsychev.sd.calc.token.NumberToken
import ru.nsychev.sd.calc.token.Operation
import java.io.PrintStream

class PrintVisitor(
    private val outputStream: PrintStream = System.out
) : TokenVisitor {
    override fun visit(token: NumberToken) {
        outputStream.print("NUMBER(${token.value}) ")
    }

    override fun visit(token: Brace) {
        throw IllegalArgumentException("PrintVisitor works only in RPN.")
    }

    override fun visit(token: Operation) {
        outputStream.print("${token.type} ")
    }
}
