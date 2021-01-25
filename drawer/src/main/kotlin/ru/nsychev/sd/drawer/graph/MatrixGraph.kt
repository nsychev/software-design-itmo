package ru.nsychev.sd.drawer.graph

import ru.nsychev.sd.drawer.drawing.DrawingApi
import java.io.File
import java.lang.NumberFormatException

class MatrixGraph(drawingApi: DrawingApi) : Graph(drawingApi) {
    override fun drawGraph(fileName: String) {
        File(fileName).bufferedReader().use {
            val n = try {
                it.readLine().toInt()
            } catch (exc: NumberFormatException) {
                throw GraphException("The first line should contain vertex number: $exc")
            }

            drawingApi.setSize(getSize(n))

            val edges = mutableSetOf<Pair<Int, Int>>()

            for (i in 0 until n) {
                val line = try {
                    it.readLine().split(" ").map(String::toInt)
                } catch (exc: NumberFormatException) {
                    throw GraphException("File should consist of integers: $exc")
                }
                check(line.size == n, "Each line should contain exactly $n zeroes or ones.")

                for (j in 0 until i) {
                    when (line[j]) {
                        0 -> check(!edges.contains(j to i), "Graph should be undirected.")
                        1 -> check(edges.contains(j to i),  "Graph should be undirected.")
                        else -> throw GraphException("Each item should be 0 or 1.")
                    }
                }

                check(line[i] == 0, "No loops allowed.")

                for (j in (i + 1) until n) {
                    when (line[j]) {
                        0 -> Unit
                        1 -> {
                            edges.add(i to j)
                            drawingApi.drawLine(getVertex(i, n), getVertex(j, n))
                        }
                        else -> throw GraphException("Each item should be 0 or 1.")
                    }
                }
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
