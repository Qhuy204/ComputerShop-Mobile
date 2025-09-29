package com.example.computerstore.navigation

import com.example.computerstore.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val iconRes: Int
) {
    object Home : BottomBarScreen("home", "Home", R.drawable.home)
    object Category : BottomBarScreen("category", "Category", R.drawable.filter)

    object News : BottomBarScreen("news", "News", R.drawable.paper)
    object Cart : BottomBarScreen("cart", "Cart", R.drawable.bag)
    object Profile : BottomBarScreen("profile", "Profile", R.drawable.profile)


}