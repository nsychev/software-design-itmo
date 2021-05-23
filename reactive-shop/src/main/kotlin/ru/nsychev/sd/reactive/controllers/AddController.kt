package ru.nsychev.sd.reactive.controllers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.entity.Currency
import ru.nsychev.sd.reactive.entity.Item
import ru.nsychev.sd.reactive.service.ItemService
import rx.Observable
import java.lang.IllegalArgumentException

class AddController : Controller(), KoinComponent {
    private val itemService by inject<ItemService>()

    override fun handle(req: HttpServerRequest<ByteBuf>): Observable<String> {
        val name = req.queryParam("name")

        val cost = req.queryParam("cost").toDoubleOrNull()
            ?: throw HttpError(HttpResponseStatus.BAD_REQUEST, "Invalid parameter 'cost'")

        val currency = try {
            req.queryParam("currency").let(Currency::valueOf)
        } catch (e: IllegalArgumentException) {
            throw HttpError(HttpResponseStatus.BAD_REQUEST, "Invalid currency")
        }

        return itemService.add(Item(name, cost, currency))
            .map { "Success" }
    }
}
