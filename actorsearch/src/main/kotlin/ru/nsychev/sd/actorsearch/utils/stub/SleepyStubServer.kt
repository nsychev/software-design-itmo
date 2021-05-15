package ru.nsychev.sd.actorsearch.utils.stub

class SleepyStubServer(port: Int) : BaseStubServer(port) {
    override val timeout: Long = 3000

    override fun search(): List<String> {
        return listOf()
    }
}
