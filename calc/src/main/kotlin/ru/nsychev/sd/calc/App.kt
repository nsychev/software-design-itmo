package ru.nsychev.sd.calc

import ru.nsychev.sd.calc.tokenizer.Tokenizer
import ru.nsychev.sd.calc.visitor.CalcVisitor
import ru.nsychev.sd.calc.visitor.ParserVisitor
import ru.nsychev.sd.calc.visitor.PrintVisitor
import java.io.IOException

fun main(args: Array<String>) {
    try {
        val tokenizer = Tokenizer()

        try {
            var next = 0
            while (next != -1) {
                next = System.`in`.read()
                tokenizer.feed(next)
            }
        } catch (exc: IOException) {
            System.err.println("Error while reading data: $exc")
        }

        val parserVisitor = ParserVisitor()
        val printVisitor = PrintVisitor()
        val calcVisitor = CalcVisitor()

        tokenizer.getResult()
            .map { it.accept(parserVisitor) }

        val result = parserVisitor.getResult()

        print("Expression in RPN: ")
        result.map { it.accept(printVisitor) }
        println()

        print("Expression value: ")
        result.map { it.accept(calcVisitor) }
        println(calcVisitor.getResult())
    } catch (exc: InvalidExpressionException) {
        System.err.println("Invalid expression: $exc")
    }

}
