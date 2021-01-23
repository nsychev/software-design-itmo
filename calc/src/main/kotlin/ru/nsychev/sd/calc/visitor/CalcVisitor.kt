package ru.nsychev.sd.calc.visitor

import ru.nsychev.sd.calc.DivisionByZeroException
import ru.nsychev.sd.calc.NotEnoughArgumentsException
import ru.nsychev.sd.calc.TooManyArgumentsException
import ru.nsychev.sd.calc.token.Brace
import ru.nsychev.sd.calc.token.NumberToken
import ru.nsychev.sd.calc.token.Operation
import ru.nsychev.sd.calc.token.OperationType

class CalcVisitor : TokenVisitor {
    private val evaluationStack: MutableList<Int> = mutableListOf()

    override fun visit(token: NumberToken) {
        evaluationStack.add(token.value)
    }

    override fun visit(token: Brace) {
        throw IllegalArgumentException("PrintVisitor works only in RPN.")
    }

    override fun visit(token: Operation) {
        val b = evaluationStack.removeLastOrNull()
            ?: throw NotEnoughArgumentsException()

        val a = evaluationStack.removeLastOrNull()
            ?: throw NotEnoughArgumentsException()

        val result = when (token.type) {
            OperationType.ADD -> a + b
            OperationType.SUBTRACT -> a - b
            OperationType.MULTIPLY -> a * b
            OperationType.DIVIDE -> if (b == 0) throw DivisionByZeroException() else a / b
        }

        evaluationStack.add(result)
    }

    fun getResult(): Int {
        when (evaluationStack.size) {
            0 -> throw NotEnoughArgumentsException()
            1 -> return evaluationStack[0]
            else -> throw TooManyArgumentsException()
        }
    }
}
