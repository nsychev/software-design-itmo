package ru.nsychev.sd.calc

import java.lang.Exception

open class InvalidExpressionException(message: String) : Exception(message)
class UnexpectedRightBracketException : InvalidExpressionException("Unexpected right bracket")
class ExpectedRightBracketException : InvalidExpressionException("Expected right bracket")
class NotEnoughArgumentsException : InvalidExpressionException("Not enough arguments to operation")
class TooManyArgumentsException : InvalidExpressionException("Too many arguments to operation")
class InvalidCharacterException(c: Char) : InvalidExpressionException("Invalid character: '$c'")
class DivisionByZeroException : InvalidExpressionException("Can't divide by zero")
