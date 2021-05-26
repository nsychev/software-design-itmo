package ru.nsychev.sd.stock.client.test

import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.Parameters
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.nsychev.sd.stock.client.StockClient
import ru.nsychev.sd.stock.client.UserStock
import ru.nsychev.sd.stock.client.UserStockView
import ru.nsychev.sd.stock.client.UserStorage
import ru.nsychev.sd.stock.client.provider.ServerProvider
import ru.nsychev.sd.stock.client.test.core.BaseTest
import ru.nsychev.sd.stock.client.test.core.TestApplication
import kotlin.test.assertEquals

class StockClientTest : BaseTest() {
    private val serverProvider: ServerProvider by lazy { TestApplication.get() }
    private val stockClient: StockClient by lazy { TestApplication.get() }

    @BeforeEach
    fun prepare(): Unit = runBlocking {
        Stock.STOCKS.forEach {
            client.submitForm(
                url = "${serverProvider.getBaseUrl()}/add-stock",
                formParameters = Parameters.build {
                    append("ticker", it.ticker)
                    append("company", it.companyName)
                    append("quantity", "1000")
                }
            )
        }
    }

    @Test
    fun `should get empty stocks`(): Unit = runBlocking {
        UserStorage.addUser("test1")

        assertEquals(listOf(), stockClient.getStocks("test1"))
    }

    @Test
    fun `should get empty funds`(): Unit = runBlocking {
        UserStorage.addUser("test2")

        assertEquals(0.0, stockClient.getFunds("test2"))
    }

    @Test
    fun `should buy a stock`(): Unit = runBlocking {
        UserStorage.addUser("test3")
        UserStorage.changeFunds("test3", 10000.0)

        stockClient.changeStock("test3", "YNDX", 1)

        assertEquals(listOf(UserStock("YNDX", 1)), UserStorage.getUser("test3").stocks)
    }

    @Test
    fun `should spend money when buying a stock`(): Unit = runBlocking {
        UserStorage.addUser("test4")
        UserStorage.changeFunds("test4", 10000.0)

        val price = getPrice("GOOG")
        stockClient.changeStock("test4", "GOOG", 1)

        assertEquals(10000 - price, UserStorage.getUser("test4").funds, 2.0)
    }

    @Test
    fun `should affect info when buying a stock`(): Unit = runBlocking {
        UserStorage.addUser("test5")
        UserStorage.changeFunds("test5", 10000.0)

        val price = getPrice("AMZN")
        stockClient.changeStock("test5", "AMZN", 1)

        assertEquals(
            listOf(UserStockView("AMZN", 1, price)),
            stockClient.getStocks("test5")
        )
    }

    private suspend fun getPrice(ticker: String): Double {
        val stockInfo: String = client.get("${serverProvider.getBaseUrl()}/stock/$ticker") {}
        return stockInfo.trim().split(" ").last().toDouble()
    }
}

internal data class Stock(val ticker: String, val companyName: String) {
    companion object {
        val STOCKS = listOf(
            Stock("YNDX", "Yandex"),
            Stock("GOOG", "Alphabet Class C"),
            Stock("AMZN", "Amazon")
        )
    }
}
