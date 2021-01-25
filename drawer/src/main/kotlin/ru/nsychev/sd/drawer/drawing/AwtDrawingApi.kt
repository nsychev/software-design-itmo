package ru.nsychev.sd.drawer.drawing

import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.system.exitProcess

class AwtDrawingApi : DrawingApi, Frame() {
    private val actions: MutableList<(Graphics2D) -> Unit> = mutableListOf()

    init {
        addWindowListener(CloseAdapter)
        isResizable = false
    }

    override fun paint(g: Graphics) {
        actions.forEach { it(g as Graphics2D) }
    }

    override fun setSize(size: Int) {
        setSize(size, size)
    }

    override fun drawCircle(coords: Pair<Int, Int>, radius: Int) {
        actions.add {
            it.fill(
                Ellipse2D.Double(
                    (coords.first - radius).toDouble(),
                    (coords.second - radius).toDouble(),
                    (radius + radius).toDouble(),
                    (radius + radius).toDouble()
                )
            )
        }
    }

    override fun drawLine(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        actions.add {
            it.draw(
                Line2D.Double(
                    start.first.toDouble(),
                    start.second.toDouble(),
                    end.first.toDouble(),
                    end.second.toDouble()
                )
            )
        }
    }

    override fun finish() {
        isVisible = true
    }

    companion object {
        object CloseAdapter : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                exitProcess(0)
            }
        }
    }
}
