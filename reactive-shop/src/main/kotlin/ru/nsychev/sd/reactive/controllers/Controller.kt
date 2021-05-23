package ru.nsychev.sd.reactive.controllers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

abstract class Controller {
    abstract fun handle(req: HttpServerRequest<ByteBuf>): Observable<String>

    fun handleSafe(req: HttpServerRequest<ByteBuf>, res: HttpServerResponse<ByteBuf>): Observable<Void> {
        val responseContent = try {
            handle(req)
                .onErrorReturn {
                    when (it) {
                        is HttpError -> {
                            res.status = it.code
                            it.message
                        }
                        else -> {
                            res.status = HttpResponseStatus.INTERNAL_SERVER_ERROR
                            it.stackTraceToString()
                        }
                    }
                }
        } catch (e: HttpError) {
            res.status = e.code
            Observable.just(e.message)
        } catch (e: Exception) {
            res.status = HttpResponseStatus.INTERNAL_SERVER_ERROR
            Observable.just(e.stackTraceToString())
        }

        return res.writeString(responseContent)
    }
}
