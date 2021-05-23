package ru.nsychev.sd.reactive.http

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.controllers.AddController
import ru.nsychev.sd.reactive.controllers.ListController
import ru.nsychev.sd.reactive.controllers.RegistrationController
import rx.Observable

class Handler : KoinComponent {
    private val addController by inject<AddController>()
    private val listController by inject<ListController>()
    private val registrationController by inject<RegistrationController>()

    fun handle(req: HttpServerRequest<ByteBuf>, res: HttpServerResponse<ByteBuf>): Observable<Void> {
        return when (req.decodedPath) {
            "/register" -> registrationController
            "/list-items" -> listController
            "/add-item" -> addController
            else -> {
                res.status = HttpResponseStatus.NOT_FOUND
                return res.writeString(Observable.just("No such page."))
            }
        }.handleSafe(req, res)
    }
}
