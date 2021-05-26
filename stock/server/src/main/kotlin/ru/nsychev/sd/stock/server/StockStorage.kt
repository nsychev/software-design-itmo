package ru.nsychev.sd.stock.server

import kotlin.random.Random
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


object StockStorage {
    private val mutex = Mutex()
    private val stocks = mutableListOf<Stock>()

    suspend fun addStock(ticker: String, companyName: String, quantity: Int) = mutex.withLock {
        val existingStock = stocks.find { it.ticker == ticker }

        stocks.removeIf { it.ticker == ticker }
        stocks.add(Stock(
            ticker,
            companyName,
            quantity + (existingStock?.quantity ?: 0),
            existingStock?.price ?: Random.nextDouble(100.0, 1000.0)
        ))
    }

    fun getStock(ticker: String) = stocks.find { it.ticker == ticker }?.copy()

    suspend fun changeQuantityAndGetPrice(ticker: String, quantity: Int): Double = mutex.withLock {
        val existingStock = stocks.find { it.ticker == ticker }
            ?: throw IllegalArgumentException("No such stock")

        if (existingStock.quantity < -quantity) {
            throw IllegalArgumentException("Insufficient stock")
        }

        stocks.removeIf { it.ticker == ticker }

        stocks.add(Stock(
            existingStock.ticker,
            existingStock.companyName,
            existingStock.quantity + quantity,
            existingStock.price
        ))

        return existingStock.price
    }

    suspend fun emulateMarket() = mutex.withLock {
        stocks.replaceAll { Stock(
            it.ticker,
            it.companyName,
            it.quantity,
            it.price + Random.nextDouble(-2.0, 2.0)
        ) }
    }
}
