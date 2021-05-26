package ru.nsychev.sd.stock.client.test.core

import org.testcontainers.containers.GenericContainer

class StockServerContainer : GenericContainer<StockServerContainer>("nsychev/sd-stock")
