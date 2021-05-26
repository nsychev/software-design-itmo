package ru.nsychev.sd.stock.client.provider

class DefaultServerProvider : ServerProvider {
    override fun getBaseUrl(): String {
        return "http://127.0.0.1:8080"
    }

}
