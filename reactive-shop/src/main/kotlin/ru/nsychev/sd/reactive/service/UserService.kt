package ru.nsychev.sd.reactive.service

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.reactive.dao.UserDao
import ru.nsychev.sd.reactive.entity.User
import rx.Observable

class UserService : KoinComponent {
    private val dao by inject<UserDao>()

    fun add(user: User) = dao.add(user)

    fun one(name: String) = dao.one(name).single()
}
