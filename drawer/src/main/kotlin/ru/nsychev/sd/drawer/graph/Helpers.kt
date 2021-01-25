package ru.nsychev.sd.drawer.graph

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun polarToDecart(fraction: Double, bigR: Int, smallR: Int) =
    bigR + (cos(PI * 2 * fraction) * smallR).toInt() to
    bigR + (sin(PI * 2 * fraction) * smallR).toInt()

fun getVertex(i: Int, n: Int) =
    polarToDecart(i.toDouble() / n, getBigRadius(n), getSmallRadius(n))

fun getSmallRadius(n: Int) =
    60 + 15 * n

fun getBigRadius(n: Int) =
    getSmallRadius(n) + 50

fun getSize(n: Int) =
    getBigRadius(n) * 2

fun check(condition: Boolean, message: String) =
    if (!condition) throw GraphException(message) else Unit
