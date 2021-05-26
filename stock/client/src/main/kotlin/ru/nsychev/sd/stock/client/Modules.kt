package ru.nsychev.sd.stock.client

import org.koin.dsl.module

val appModule = module {
    single { StockClient() }
    single { Broker() }
}
