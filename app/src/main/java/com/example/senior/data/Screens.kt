package com.example.senior.data

sealed class Screens(val screen: String) {
    data object Graph: Screens("graph")
    data object Image: Screens("image")
}