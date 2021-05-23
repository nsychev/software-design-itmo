package ru.nsychev.sd.reactive.controllers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.entity.User
import ru.nsychev.sd.reactive.service.ItemService
import ru.nsychev.sd.reactive.service.UserService
import rx.Observable

class ListController : Controller(), KoinComponent {
    private val itemService by inject<ItemService>()
    private val userService by inject<UserService>()

    override fun handle(req: HttpServerRequest<ByteBuf>): Observable<String> {
        val username = req.queryParameters.getOrDefault("username", null)
            ?.getOrNull(0)
            ?: throw HttpError(HttpResponseStatus.BAD_REQUEST, "Missing parameter 'username'")

        return userService.one(username)
            .singleOrDefault(null)
            .flatMap { user ->
                Observable.merge(
                    Observable.just("Prices in ${user.currency}:\n"),
                    itemService.all(user.currency)
                        .map { item ->
                            "${item.name}: ${item.cost}\n"
                        }
                )
            }
            .onErrorReturn { when (it) {
                is NoSuchElementException -> throw HttpError(HttpResponseStatus.FORBIDDEN, "No such user.")
                else -> throw it
            } }
    }
}
