package ru.nsychev.sd.drawer

import ru.nsychev.sd.drawer.drawing.AwtDrawingApi
import ru.nsychev.sd.drawer.drawing.JavaFxDrawingApi
import ru.nsychev.sd.drawer.graph.EdgeGraph
import ru.nsychev.sd.drawer.graph.GraphException
import ru.nsychev.sd.drawer.graph.MatrixGraph
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        System.err.println("Usage: drawer <drawer> <graph-type> <graph-desc-file>")
        exitProcess(1)
    }

    when (args[0]) {
        "awt" -> AwtDrawingApi()
        "javafx" -> JavaFxDrawingApi()
        else -> {
            System.err.println()
            exitProcess(1)
        }
    }.let { drawingApi ->
        when (args[1]) {
            "edges" -> EdgeGraph(drawingApi)
            "matrix" -> MatrixGraph(drawingApi)
            else -> {
                System.err.println()
                exitProcess(1)
            }
        }.apply {
            try {
                drawGraph(args[2])
                drawingApi.finish()
            } catch (exc: GraphException) {
                System.err.println("Can't draw graph: $exc")
                exitProcess(1)
            }
        }
    }
}
