package ru.nsychev.sd.stock.client

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object UserStorage {
    private val mutex = Mutex()
    private val users = mutableListOf<User>()

    suspend fun addUser(name: String) = mutex.withLock {
        if (users.find { it.name == name } != null) {
            throw IllegalArgumentException("User exists")
        }

        users.add(User(name))
    }

    suspend fun changeFunds(name: String, topUp: Double) = mutex.withLock {
        val user = users.find { it.name == name }
            ?: throw IllegalArgumentException("No such user")

        users.removeIf { it.name == name }

        users.add(User(
            user.name,
            user.funds + topUp,
            user.stocks
        ))
    }

    fun getUser(name: String) = users.find { it.name == name }
        ?: throw IllegalArgumentException("No such user")

    suspend fun changeStock(name: String, ticker: String, delta: Int) = mutex.withLock {
        val user = users.find { it.name == name }
            ?: throw IllegalArgumentException("No such user")

        val quantity = user.stocks.find { it.ticker == ticker }?.quantity ?: 0


        if (-delta > quantity) {
            throw IllegalArgumentException("Insufficient stocks")
        }

        users.removeIf { it.name == name }

        users.add(
            user.copy(
                stocks = user.stocks.filter { it.ticker != ticker }
                    .plus(UserStock(ticker, quantity + delta))
            )
        )
    }
}
