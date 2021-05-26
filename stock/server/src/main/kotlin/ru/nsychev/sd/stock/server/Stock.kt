package ru.nsychev.sd.stock.server

data class Stock(
    val ticker: String,
    val companyName: String,
    val quantity: Int,
    val price: Double
)
