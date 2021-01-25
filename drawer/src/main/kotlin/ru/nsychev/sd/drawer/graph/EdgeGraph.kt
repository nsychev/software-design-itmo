package ru.nsychev.sd.drawer.graph

import ru.nsychev.sd.drawer.drawing.DrawingApi
import java.io.File
import java.lang.NumberFormatException
import kotlin.math.max
import kotlin.math.min

class EdgeGraph(drawingApi: DrawingApi) : Graph(drawingApi) {
    override fun drawGraph(fileName: String) {
        File(fileName).bufferedReader().use {
            val (n, m) = try {
                val line = it.readLine().split(" ").map(String::toInt)
                check(line.size == 2, "The first line should contain vertex and edge number.")
                line
            } catch (exc: NumberFormatException) {
                throw GraphException("The first line contains invalid numbers: $exc")
            }

            drawingApi.setSize(getSize(n))

            val edges = mutableSetOf<Pair<Int, Int>>()

            for (i in 0 until m) {
                val (x, y) = try {
                    val line = it.readLine().split(" ").map(String::toInt)
                    check(line.size == 2, "Edge should contain two vertex number.")
                    line
                } catch (exc: NumberFormatException) {
                    throw GraphException("File should consist of integers: $exc")
                }
                check(x in 1..n && y in 1..n, "Edge should contain valid vertex numbers.")
                check(x != y, "Loops are not allowed.")

                val edge = min(x, y) - 1 to max(x, y) - 1
                check(!edges.contains(edge), "Double edges are not allowed.")

                edges.add(edge)
                drawingApi.drawLine(
                    getVertex(edge.first, n),
                    getVertex(edge.second, n)
                )
            }

            for (i in 0 until n) {
                drawingApi.drawCircle(
                    getVertex(i, n),
                    10
                )
            }
        }
    }
}
