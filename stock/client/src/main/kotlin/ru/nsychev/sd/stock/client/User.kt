package ru.nsychev.sd.stock.client

data class User(
    val name: String,
    val funds: Double = 0.0,
    val stocks: List<UserStock> = listOf()
)

data class UserStock(
    val ticker: String,
    val quantity: Int
)
