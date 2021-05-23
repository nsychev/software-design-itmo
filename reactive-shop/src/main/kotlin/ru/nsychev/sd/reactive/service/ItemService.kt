package ru.nsychev.sd.reactive.service

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.dao.ItemDao
import ru.nsychev.sd.reactive.entity.Currency
import ru.nsychev.sd.reactive.entity.Item
import ru.nsychev.sd.reactive.entity.convert

class ItemService : KoinComponent {
    private val dao by inject<ItemDao>()

    fun add(item: Item) = dao.add(item)

    fun all(currency: Currency) = dao.all()
        .map { item ->
            Item(item.name, item.cost.convert(item.currency, currency), currency)
        }
}
