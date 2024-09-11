package com.example.senior.data

//import kotlinx.serialization.Serializable
//
//@Serializable
//data class LedMatrix(
//    val frame_size: Int,
//    val frame_num: Int,
//    val current_frame: Int,
//    val led_data: List<Int>
//)

data class LedMatrix(
    val ledMatrix: List<List<Long>>
)