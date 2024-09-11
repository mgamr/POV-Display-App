package com.example.senior

import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import net.objecthunter.exp4j.ExpressionBuilder

class GraphViewModel : ViewModel() {

    fun evaluateEquation(equation: String): List<Point> {
        val points = mutableListOf<Point>()
        val xValues = arrayOf(1f, 2f, 3f, 4f)

        for (x in xValues) {
            try {
                val expression = equation.replace("y=", "")

                val expr = ExpressionBuilder(expression)
                    .variable("x")
                    .build()
                    .setVariable("x", x.toDouble())

                val y = expr.evaluate().toFloat()

                points.add(Point(x, y))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return points
    }

//    fun evaluateEquation(equation: String, xValues: Array<Float>): Array<Float> {
//        val yValues = Array(xValues.size) { 0f }
//
//        for (i in xValues.indices) {
//            try {
//                val expression = equation.replace("y=", "")
//
//                val expr = ExpressionBuilder(expression)
//                    .variable("x")
//                    .build()
//                    .setVariable("x", xValues[i].toDouble())
//
//                yValues[i] = expr.evaluate().toFloat()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        return yValues
//    }
}