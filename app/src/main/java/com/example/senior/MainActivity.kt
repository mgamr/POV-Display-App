package com.example.senior

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.senior.pages.MyBottomAppBar
import com.example.senior.ui.theme.SeniorTheme
import com.example.senior.viewmodels.GraphViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeniorTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MainPage()
//                }

//                LedControlScreen()

                val graphViewModel: GraphViewModel = viewModel()
                val myMatrix = graphViewModel.generateMask()
                Log.d("Main", "started")

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    GraphPage()
                    MyBottomAppBar(myMatrix)
                }
            }
        }
    }
}


//fun writeToFile(context: Context, fileName: String, content: String) {
//    try {
//        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
//            outputStream.write(content.toByteArray())
//        }
//        Log.d("GraphPage", "File written successfully: $fileName")
//    } catch (e: Exception) {
//        Log.e("GraphPage", "Error writing to file: $fileName", e)
//    }
//}
//
//// Function to convert a map to JSON using Gson
//fun mapToJson(map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>): String {
//    val gson = Gson()
//    val mapType = object : TypeToken<MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>>() {}.type
//    return gson.toJson(map, mapType)
//}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SeniorTheme {
//        Greeting("Android")
//    }
//}