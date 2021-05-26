package ru.nsychev.sd.stock.server

data class AddStockRequest(
    val ticker: String,
    val companyName: String,
    val quantity: Int
)
