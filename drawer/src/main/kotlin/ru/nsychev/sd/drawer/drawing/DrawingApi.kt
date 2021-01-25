package ru.nsychev.sd.drawer.drawing

interface DrawingApi {
    fun setSize(size: Int)
    fun drawCircle(coords: Pair<Int, Int>, radius: Int)
    fun drawLine(start: Pair<Int, Int>, end: Pair<Int, Int>)
    fun finish()
}
