package com.example.senior.pages

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.senior.R
import com.example.senior.data.GridElement
import com.example.senior.ui.theme.LightLightPink
import com.example.senior.viewmodels.GraphViewModel
import com.example.senior.viewmodels.PostViewModel

//@Composable
//fun GraphPage() {
//    Text("Graph")
//}

@SuppressLint("RememberReturnType")
@Composable
fun GridView(context: Context, postViewModel: PostViewModel, graphViewModel: GraphViewModel) {
    val imageList = remember {
        mutableStateListOf(
            GridElement("Mario", R.drawable.mario, resizeImage(R.drawable.mario, context)),
            GridElement("<3", R.drawable.mari, resizeImage(R.drawable.mari, context)),
            GridElement("Jiqi", R.drawable.jiqi, resizeImage(R.drawable.jiqi, context)),
            GridElement("sin(x)", R.drawable.sinx, resizeImage(R.drawable.sinx, context)),
            GridElement("jando", R.drawable.jondo, resizeImage(R.drawable.jondo, context)),
            GridElement("sxila", R.drawable.sxila, resizeImage(R.drawable.sxila, context)),
            GridElement("kvashuna", R.drawable.kvasho, resizeImage(R.drawable.kvasho, context)),
            GridElement("tazuka", R.drawable.tazo, resizeImage(R.drawable.tazo, context))
        )
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val resizedImage = Bitmap.createScaledBitmap(bitmap, 432, 432, true).asImageBitmap()

                val myMatrix = graphViewModel.getPixelFromImage1(resizedImage, context)
                postViewModel.sendArrayAsPackets(context, myMatrix)
            }
        }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(10.dp)
    ) {
        items(imageList.size) { index ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "${imageList[index].text} selected..",
                                Toast.LENGTH_SHORT
                            )
                            .show()

                        val myMatrix = graphViewModel.getPixelFromImage1(imageList[index].imageBitmap, context)
                        postViewModel.sendArrayAsPackets(context, myMatrix)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = LightLightPink,
                ),
                elevation = CardDefaults.elevatedCardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = imageList[index].image),
                        contentDescription = imageList[index].text,
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = imageList[index].text,
                        color = Color.Black
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
//                        Toast
//                            .makeText(
//                                context,
//                                "add selected..",
//                                Toast.LENGTH_SHORT
//                            )
//                            .show()
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                colors = CardDefaults.cardColors(
                    containerColor = LightLightPink,
                ),
                elevation = CardDefaults.elevatedCardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add",
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Choose New",
                        color = Color.Black
                    )
                }
            }
        }
    }
}


fun resizeImage(resourceId: Int, context: Context): ImageBitmap {
    val originalBitmap = BitmapFactory.decodeResource(context.resources, resourceId)
    val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 432, 432, true)
    return scaledBitmap.asImageBitmap()
}

