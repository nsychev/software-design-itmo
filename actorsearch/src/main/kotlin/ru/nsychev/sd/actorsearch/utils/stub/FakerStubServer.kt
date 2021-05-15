package ru.nsychev.sd.actorsearch.utils.stub

import com.github.javafaker.Faker

class FakerStubServer(port: Int) : BaseStubServer(port) {
    override fun search(): List<String> {
        return (1..5).map { faker.internet().url() }
    }

    companion object {
        val faker: Faker = Faker.instance()
    }
}
