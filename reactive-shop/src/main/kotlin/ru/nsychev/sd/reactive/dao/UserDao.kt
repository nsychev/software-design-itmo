package ru.nsychev.sd.reactive.dao

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.Success
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.Document
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.entity.User
import rx.Observable

class UserDao : KoinComponent {
    private val client by inject<MongoClient>()
    private val database = client.getDatabase("default")

    private fun getCollection(): MongoCollection<Document> =
        database.getCollection(User.COLLECTION_NAME)

    fun one(name: String): Observable<User> = getCollection()
        .find(BsonDocument(User.NAME, BsonString(name)))
        .toObservable()
        .map(User::fromDocument)

    fun add(user: User): Observable<Success> = getCollection()
        .insertOne(user.toDocument())

}
