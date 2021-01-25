package ru.nsychev.sd.feed.api

open class VKException(message: String) : Exception(message)

class UnexpectedKeyException(key: String) : VKException("Invalid API response: unexpected key '$key'")

class BadStatusCodeException(code: Int, val responseText: String): VKException("API responded with error: $code")

class TimeoutException(override val cause: Exception) : VKException("Can't call API because of timeout: $cause")

class RequestFailedException(
    override val cause: Exception
) : VKException("Can't call API because of network error: $cause")
