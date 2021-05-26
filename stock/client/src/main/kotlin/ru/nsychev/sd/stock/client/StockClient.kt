package ru.nsychev.sd.stock.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.Parameters

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.stock.client.provider.ServerProvider

class StockClient : KoinComponent {
    private val serverProvider by inject<ServerProvider>()
    private val client = HttpClient(CIO)

    suspend fun getStocks(name: String): List<UserStockView> =
        UserStorage.getUser(name)
            .stocks
            .map { UserStockView(
                it.ticker,
                it.quantity,
                getPrice(it.ticker)
            ) }

    suspend fun getFunds(name: String): Double {
        val user = UserStorage.getUser(name)

        return user.funds + getStocks(name).sumOf { view ->
            view.quantity * view.price
        }
    }

    suspend fun changeStock(name: String, ticker: String, quantity: Int) {
        UserStorage.changeStock(name, ticker, quantity)

        val response: String = client.submitForm(
            url = "${serverProvider.getBaseUrl()}/stock/$ticker/order",
            formParameters = Parameters.build {
                append("quantity", quantity.toString())
            }
        )

        if (response.startsWith("OK")) {
            UserStorage.changeFunds(name, -quantity * response.split(" ").last().toDouble())
        } else {
            UserStorage.changeStock(name, ticker, -quantity)
            throw IllegalArgumentException(response)
        }
    }

    private suspend fun getPrice(ticker: String): Double {
        val stockInfo: String = client.get("${serverProvider.getBaseUrl()}/stock/$ticker") {}
        return stockInfo.trim().split(" ").last().toDouble()
    }
}

data class UserStockView(
    val ticker: String,
    val quantity: Int,
    val price: Double
)
