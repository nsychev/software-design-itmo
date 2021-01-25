package ru.nsychev.sd.drawer.graph

import ru.nsychev.sd.drawer.drawing.DrawingApi

abstract class Graph(protected val drawingApi: DrawingApi) {
    abstract fun drawGraph(fileName: String)
}
