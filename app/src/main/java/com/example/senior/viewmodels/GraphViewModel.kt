package com.example.senior.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.example.senior.R
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.BufferedReader
import java.io.InputStreamReader

class GraphViewModel : ViewModel() {

    fun evaluateEquation(equation: String, start: Float = -10f, end: Float = 10f, steps: Int = 101): List<Point> {
        val points = mutableListOf<Point>()
//        val start = -10f    // Starting value
//        val end = 10f     // Ending value
//        val steps = 101   // Number of steps

        val floatArray = FloatArray(steps) { i ->
            start + i * (end - start) / (steps - 1)
        }

        for (x in floatArray) {
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

    fun resizeImage(resourceId: Int, context: Context): ImageBitmap {
        val originalBitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 432, 432, true)
        return scaledBitmap.asImageBitmap()
    }

    private fun parseCoordinates(input: String): Pair<Pair<Int, Int>, List<Pair<Int, Int>>>? {
        val keyRegex = """\((\d+),(\d+)\)""".toRegex()
        val keyMatch = keyRegex.find(input) ?: return null
        val key = Pair(keyMatch.groupValues[1].toInt(), keyMatch.groupValues[2].toInt())

        val coordRegex = """\[(\d+),\s*(\d+)\]""".toRegex()
        val coordinates = coordRegex.findAll(input).map {
            Pair(it.groupValues[1].toInt(), it.groupValues[2].toInt())
        }.toList()

        return if (coordinates.isNotEmpty()) Pair(key, coordinates) else null
    }

    @Composable
    fun generateMask(): MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> {
        val map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf()


        val inputStream = LocalContext.current.resources.openRawResource(R.raw.test1)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        try {
            bufferedReader.useLines { lines ->
                lines.forEach { line ->
                    val result = parseCoordinates(line)
                    if (result != null) {
                        val (key, coordinates) = result
                        map[key] = coordinates
                    } else {
                        Log.w("GraphPage", "Failed to parse line: $line")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GraphPage", "Error reading file", e)
        }

        Log.d("GraphPage", "Total entries in map: ${map.size}")
        Log.d("blaaaa1", map.toString())
        return map
    }

    private fun rgbTo32BitColor(red: Int, green: Int, blue: Int, alpha: Int = 0): Int {
        return (green shl 16) or (red shl 8) or blue
    }


    fun getPixelFromImage(
        map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>,
        imageBitmap: ImageBitmap
    ): List<List<Int>> {
        val pixelMap = imageBitmap.toPixelMap()
        val resMap: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

        for ((key, valueArray) in map) {
            var count = 0
            var sumRed = 0.0f
            var sumGre = 0.0f
            var sumBlu = 0.0f
            for (pair in valueArray) {
                count += 1
                val value1 = pair.first
                val value2 = pair.second

                val pixelColor = pixelMap[value2, value1]
                val red: Float = (pixelColor.red * 255.0f)
                val gre: Float = (pixelColor.green * 255.0f)
                val blu: Float = (pixelColor.blue * 255.0f)
                sumRed += red
                sumBlu += blu
                sumGre += gre
            }
            var avrRed = (sumRed / count).toInt()
            var avrBlu = (sumBlu / count).toInt()
            var avrGre = (sumGre / count).toInt()
            val color32Bit = rgbTo32BitColor(avrRed, avrGre, avrBlu)

            resMap.put(key, color32Bit)
        }

        val matrix: List<List<Int>> = (0..119).map { i ->
            (1..36).map { j ->
                if (resMap.containsKey(Pair(j, i))) {
                    resMap[Pair(j, i)] ?: 0
                } else {
                    0
                }
            }
        }
//        val transposedMatrix: List<List<Int>> = matrix[0].indices.map { col ->
//            matrix.map { row -> row[col] }
//        }
        val result = matrix.mapIndexed { index, list ->
            listOf(index) + list
        }

//        val res = result.flatten()
////            .chunked(37) // Break the list into chunks of 120
////            .take(360)
//            .chunked(111) // Break the list into chunks of 120
//            .take(120)
        for(i in result){
            Log.d("list2", i.toString())
        }
        Log.d("list2", result.size.toString())
        Log.d("list2", result[0].size.toString())

//        for(i in res) {
//            Log.d("list3", i.toString())
//        }
//        Log.d("list3", res.size.toString())
//        Log.d("list3", res[0].size.toString())
//

//        for(i in reshapedList){
//            Log.d("list", i.toString())
//
//        }

        return result
    }
}