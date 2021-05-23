package ru.nsychev.sd.reactive.controllers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.entity.Currency
import ru.nsychev.sd.reactive.entity.User
import ru.nsychev.sd.reactive.service.UserService
import rx.Observable
import java.lang.IllegalArgumentException

class RegistrationController : Controller(), KoinComponent {
    private val userService by inject<UserService>()

    override fun handle(req: HttpServerRequest<ByteBuf>): Observable<String> {
        val name = req.queryParameters.getOrDefault("username", null)
            ?.getOrNull(0)
            ?: throw HttpError(HttpResponseStatus.BAD_REQUEST, "Missing parameter 'username'")

        val currency = try {
            req.queryParameters.getOrDefault("currency", null)
                ?.getOrNull(0)
                ?.let(Currency::valueOf)
                ?: throw HttpError(HttpResponseStatus.BAD_REQUEST, "Missing parameter 'currency'")
        } catch (e: IllegalArgumentException) {
            throw HttpError(HttpResponseStatus.BAD_REQUEST, "Invalid currency")
        }

        return userService
            .add(User(name, currency))
            .map { "Welcome!" }
    }
}
