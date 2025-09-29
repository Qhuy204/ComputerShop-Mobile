package com.example.computerstore

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.computerstore.navigation.BottomBar
import com.example.computerstore.navigation.BottomBarScreen
import com.example.computerstore.screens.CartScreen
import com.example.computerstore.screens.CategoryScreen
import com.example.computerstore.screens.CheckoutScreen
import com.example.computerstore.screens.HomeScreen
import com.example.computerstore.screens.NewsDetailsScreen
import com.example.computerstore.screens.NewsScreen
import com.example.computerstore.screens.OrderSuccessScreen
import com.example.computerstore.screens.ProductDetailsScreen
import com.example.computerstore.screens.ProfileScreen


@Composable
fun MainScaffold(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val homeListState = rememberLazyListState()

    // Debug log
    Log.d("MainScaffold", "Start destination: ${BottomBarScreen.Home.route}")

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(
                    onLogout = onLogout,
                    listState = homeListState,
                    navController = navController
                )
            }

            composable("product/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0

                ProductDetailsScreen(
                    productId = productId,
                    navController = navController,
                )
            }



            composable(BottomBarScreen.News.route) {
                NewsScreen(
                    onBlogClick = { blog ->
                        navController.navigate("newsDetails/${blog.blog_id}")
                    },
                    navController = navController
                )
            }

            composable(
                route = "newsDetails/{blogId}",
                arguments = listOf(
                    navArgument("blogId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val blogId = backStackEntry.arguments?.getInt("blogId") ?: 0
                NewsDetailsScreen(
                    blogId = blogId,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("cart") {
                CartScreen(
                    navController = navController,
                    onCheckoutClick = {
                        navController.navigate("checkout")
                    }
                )
            }


            composable("checkout") {
                CheckoutScreen(
                    onOrderPlaced = { orderId ->
                        navController.navigate("order_success/$orderId")
                    }
                )
            }

            composable("order_success/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")?.toIntOrNull()
                OrderSuccessScreen(
                    orderId = orderId,
                    onGoHome = { navController.navigate("home") },
                    onViewOrders = { navController.navigate("orders") }
                )
            }

            composable(BottomBarScreen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        onLogout()
                    }
                )
            }

            composable("productDetails/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toInt()
                productId?.let {
                    ProductDetailsScreen(
                        productId = it,
                        navController = navController
                    )
                }
            }

            composable(BottomBarScreen.Category.route) {
                // Trang danh sách Category
                CategoryScreen(
                    navController = navController,
                    categoryId = 0,
                    categoryName = "Danh mục"
                )
            }


            composable("category/{categoryId}/{categoryName}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId")?.toInt() ?: 0
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Danh mục"
                CategoryScreen(
                    navController = navController,
                    categoryId = categoryId,
                    categoryName = categoryName
                )
            }


        }
    }
}