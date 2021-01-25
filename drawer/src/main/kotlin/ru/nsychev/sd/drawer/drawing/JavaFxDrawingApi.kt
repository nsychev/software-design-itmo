package ru.nsychev.sd.drawer.drawing

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage

class JavaFxDrawingApi : DrawingApi {
    override fun setSize(size: Int) {
        JavaFXApp.size = size.toDouble()
    }

    override fun drawCircle(coords: Pair<Int, Int>, radius: Int) {
        JavaFXApp.actions.add {
            it.graphicsContext2D.fillOval(
                (coords.first - radius).toDouble(),
                (coords.second - radius).toDouble(),
                (radius + radius).toDouble(),
                (radius + radius).toDouble()
            )
        }
    }

    override fun drawLine(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        JavaFXApp.actions.add {
            print("line")
            it.graphicsContext2D.strokeLine(
                start.first.toDouble(),
                start.second.toDouble(),
                end.first.toDouble(),
                end.second.toDouble()
            )
        }
    }

    override fun finish() {
        launch(JavaFXApp::class.java)
    }

    class JavaFXApp : Application() {
        override fun start(stage: Stage) {
            val canvas = Canvas(size, size)
            stage.scene = Scene(Group(canvas), Color.WHITE)
            stage.isResizable = false
            canvas.graphicsContext2D.stroke = Color.BLACK
            canvas.graphicsContext2D.fill = Color.BLACK
            actions.forEach { it(canvas) }
            stage.show()
        }

        companion object {
            val actions: MutableList<(Canvas) -> Unit> = mutableListOf()
            var size: Double = 0.0
        }
    }
}
