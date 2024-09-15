package com.example.senior.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.example.senior.R
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.BufferedReader
import java.io.InputStreamReader

class GraphViewModel : ViewModel() {

    fun evaluateEquation(equation: String): List<Point> {
        val points = mutableListOf<Point>()
        val xValues = arrayOf(-5f, -4f, -3f, -2f, -1f, 0f, 1f, 2f, 3f, 4f, 5f)

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

    fun parseCoordinates(input: String): Pair<Pair<Int, Int>, List<Pair<Int, Int>>>? {
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

        val context = LocalContext.current

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

//    val mapJson = mapToJson(map)

        // Write the JSON to a file
//    writeToFile(context, "map_output.json", mapJson)


        Log.d("GraphPage", "Total entries in map: ${map.size}")
        Log.d("blaaaa1", map.toString())
        return map
    }

    fun rgbTo32BitColor(red: Int, green: Int, blue: Int, alpha: Int = 0): Int {
        return (green shl 16) or (red shl 8) or blue
    }

    fun generateMask1(context: Context): MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> {
        val map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf()

        val inputStream = context.resources.openRawResource(R.raw.test1)
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

//    val mapJson = mapToJson(map)

        // Write the JSON to a file
//    writeToFile(context, "map_output.json", mapJson)


        Log.d("GraphPage", "Total entries in map: ${map.size}")
        Log.d("blaaaa1", map.toString())
        return map
    }

    fun getPixelFromImage1(imageBitmap: ImageBitmap, context: Context): List<List<Int>> {
//        val imageBitmap = ImageBitmap.imageResource(id = imageId)
        val pixelMap = imageBitmap.toPixelMap()

        val map = generateMask1(context)

        val resMap: MutableMap<Pair<Int, Int>,Int > = mutableMapOf()
        for ((key, valueArray) in map) {
            val key1 = key.first
            val key2 = key.second

            var count = 0
            var sumRed =0.0f
            var sumGre =0.0f
            var sumBlu =0.0f
            for (pair in valueArray) {
                count += 1
                val value1 = pair.first
                val value2 = pair.second

                val pixelColor = pixelMap[value2, value1]
                val red:Float = (pixelColor.red * 255.0f)
                val gre:Float = (pixelColor.green*255.0f)
                val blu:Float = (pixelColor.blue *255.0f)
                sumRed += red
                sumBlu += blu
                sumGre += gre
            }
            var avrRed = (sumRed/count).toInt()
            var avrBlu = (sumBlu/count).toInt()
            var avrGre = (sumGre/count).toInt()
            val color32Bit = rgbTo32BitColor(avrRed, avrGre, avrBlu)

            resMap.put(key,color32Bit)
        }

        val matrix: List<List<Int>> = (0..119).map { i ->
            (1..36).map { j ->
                if (resMap.containsKey(Pair(j, i))) {
                    resMap[Pair(j, i)]?.toInt() ?: 0
                } else {
                    0
                }
            }
        }

        Log.d("matrix", matrix.toString())
        return matrix
    }

    @Composable
    fun getPixelFromImage(imageId: Int): List<List<Int>> {
        val imageBitmap = ImageBitmap.imageResource(id = imageId)
        val pixelMap = imageBitmap.toPixelMap()

        val map = generateMask()

        val resMap: MutableMap<Pair<Int, Int>,Int > = mutableMapOf()
        for ((key, valueArray) in map) {
            val key1 = key.first
            val key2 = key.second

            var count = 0
            var sumRed =0.0f
            var sumGre =0.0f
            var sumBlu =0.0f
            for (pair in valueArray) {
                count += 1
                val value1 = pair.first
                val value2 = pair.second

                val pixelColor = pixelMap[value2, value1]
                val red:Float = (pixelColor.red * 255.0f)
                val gre:Float = (pixelColor.green*255.0f)
                val blu:Float = (pixelColor.blue *255.0f)
                sumRed += red
                sumBlu += blu
                sumGre += gre
            }
            var avrRed = (sumRed/count).toInt()
            var avrBlu = (sumBlu/count).toInt()
            var avrGre = (sumGre/count).toInt()
            val color32Bit = rgbTo32BitColor(avrRed, avrGre, avrBlu)

            resMap.put(key,color32Bit)
        }

        val matrix: List<List<Int>> = (0..119).map { i ->
            (1..36).map { j ->
                if (resMap.containsKey(Pair(j, i))) {
                    resMap[Pair(j, i)]?.toInt() ?: 0
                } else {
                    0
                }
            }
        }

        Log.d("matrix", matrix.toString())
        return matrix
    }

    @Composable
    fun getPixelFromImage2(
        map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>
    ): List<List<Int>> {
        val imageBitmap = ImageBitmap.imageResource(id = R.drawable.marioreal)
        val pixelMap = imageBitmap.toPixelMap()


        val resMap: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        for ((key, valueArray) in map) {
            val key1 = key.first
            val key2 = key.second

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
        val transposedMatrix: List<List<Int>> = matrix[0].indices.map { col ->
            matrix.map { row -> row[col] }
        }
        val result = transposedMatrix.mapIndexed { index, list ->
            listOf(index) + list
        }

//    val extendedMatrix: List<List<Int>> = transposedMatrix.chunked(2).map { chunk ->
//        if (chunk.size == 2) {
//            chunk[0] + chunk[1]  // Concatenate two rows
//        } else {
//            chunk[0]  // If there's only one row left (odd number of rows), just use it as-is
//        }
//    }
        //Log.d("matrix","${extendedMatrix[0].size}" )
        return result
    }
}