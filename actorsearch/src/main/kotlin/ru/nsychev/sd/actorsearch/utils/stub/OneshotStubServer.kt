package ru.nsychev.sd.actorsearch.utils.stub

class OneshotStubServer(port: Int) : BaseStubServer(port) {
    override fun search(): List<String> {
        return (1..5).map { RESULT }
    }

    companion object {
        const val RESULT = "http://ya.ru"
    }
}
