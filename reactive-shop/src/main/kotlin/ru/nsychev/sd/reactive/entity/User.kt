package ru.nsychev.sd.reactive.entity

import org.bson.Document
import org.bson.types.ObjectId

data class User(
    val name: String,
    val currency: Currency
) {
    var id: String = ""

    companion object {
        const val COLLECTION_NAME = "user"

        const val ID = "_id"
        const val NAME = "name"
        const val CURRENCY = "currency"

        fun fromDocument(document: Document) = User(
            document.getString(NAME),
            Currency.valueOf(document.getString(CURRENCY))
        ).also {
            it.id = document[ID].toString()
        }
    }

    fun toDocument(): Document = Document().apply {
        if (id.isNotEmpty()) set(ID, ObjectId(id))
        set(NAME, name)
        set(CURRENCY, currency.toString())
    }
}
