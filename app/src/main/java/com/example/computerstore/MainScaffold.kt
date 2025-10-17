package com.example.computerstore

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.computerstore.navigation.BottomBar
import com.example.computerstore.navigation.BottomBarScreen
import com.example.computerstore.screens.AddOrEditAddressScreen
import com.example.computerstore.screens.AddressScreen
import com.example.computerstore.screens.BrandDetailScreen
import com.example.computerstore.screens.CartScreen
import com.example.computerstore.screens.CategoryDetailScreen
import com.example.computerstore.screens.CategoryScreen
import com.example.computerstore.screens.CheckoutScreen
import com.example.computerstore.screens.HomeScreen
import com.example.computerstore.screens.NewsDetailsScreen
import com.example.computerstore.screens.NewsScreen
import com.example.computerstore.screens.OrderSuccessScreen
import com.example.computerstore.screens.ProductDetailsScreen
import com.example.computerstore.screens.ProfileScreen
import com.example.computerstore.screens.SearchResultScreen
import com.example.computerstore.screens.EditProfileScreen
import com.example.computerstore.screens.LoginScreen
import com.example.computerstore.screens.OrderDetailScreen
import com.example.computerstore.screens.OrderHistoryScreen
import com.example.computerstore.viewmodel.OrderViewModel
import com.example.computerstore.viewmodel.UserAddressViewModel
import com.example.computerstore.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MainScaffold(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val homeListState = rememberLazyListState()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Các màn không hiển thị BottomBar
    val hideBottomBarRoutes = listOf("checkout", "order_success")

    Scaffold(
        bottomBar = {
            // Ẩn BottomBar ở các màn có prefix này
            if (hideBottomBarRoutes.none { currentRoute?.startsWith(it) == true }) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // Login
            composable("login") {
                LoginScreen(
                    navController = navController,
                    onSignupClick = { navController.navigate("signup") },
                    onLoginSuccess = {
                        navController.navigate(BottomBarScreen.Home.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }


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
                    onBackClick = { navController.popBackStack() }
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
                arguments = listOf(navArgument("blogId") { type = NavType.IntType })
            ) { backStackEntry ->
                val blogId = backStackEntry.arguments?.getInt("blogId") ?: 0
                NewsDetailsScreen(
                    navController = navController,
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

            composable(
                route = "checkout/{selectedIds}",
                arguments = listOf(navArgument("selectedIds") { type = NavType.StringType })
            ) { backStackEntry ->
                val selectedIdsArg = backStackEntry.arguments?.getString("selectedIds") ?: ""
                val selectedCartIds = selectedIdsArg.split(",").filter { it.isNotBlank() }

                CheckoutScreen(
                    navController = navController,
                    selectedCartIds = selectedCartIds,
                    onOrderPlaced = { orderId ->
                        navController.navigate("order_success/$orderId") {
                            popUpTo("cart") { inclusive = true }
                        }
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
                val orderViewModel: OrderViewModel = viewModel()
                val addressViewModel: UserAddressViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel()

                ProfileScreen(
                    onLogout = onLogout,
                    orderViewModel = orderViewModel,
                    addressViewModel = addressViewModel,
                    userViewModel = userViewModel,
                    navController = navController
                )
            }


            composable(BottomBarScreen.Category.route) {
                CategoryScreen(navController = navController, categoryId = 0, categoryName = "Danh mục")
            }

            composable("category/{categoryId}/{categoryName}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId")?.toInt() ?: 0
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Danh mục"
                CategoryScreen(navController = navController, categoryId = categoryId, categoryName = categoryName)
            }

            // Search
            composable("search?query={query}") { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                SearchResultScreen(query = query, navController = navController)
            }
             // Category
            composable(
                route = "categoryDetail/{categoryId}/{categoryName}",
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.IntType },
                    navArgument("categoryName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Danh mục"
                CategoryDetailScreen(navController, categoryId, categoryName)
            }

            // Brand
            composable(
                route = "brandDetail/{brandName}",
                arguments = listOf(
                    navArgument("brandName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
                BrandDetailScreen(navController = navController, brandName = brandName)
            }



            composable("edit_profile") {
                EditProfileScreen(navController = navController)
            }

            composable("address_manager") {
                AddressScreen(navController = navController)
            }

            composable("order_history") {
                OrderHistoryScreen(navController = navController)
            }


            // Address
            composable(
                route = "add_or_edit_address?addressId={addressId}&userId={userId}&isEdit={isEdit}",
                arguments = listOf(
                    navArgument("addressId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("userId") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("isEdit") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val addressId = backStackEntry.arguments?.getString("addressId")
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                val isEdit = backStackEntry.arguments?.getBoolean("isEdit") ?: false

                AddOrEditAddressScreen(
                    navController = navController,
                    userId = userId,
                    addressId = addressId,
                    isEdit = isEdit
                )
            }


            // Order
            composable("orders") {
                OrderHistoryScreen(navController = navController)
            }

            // Order details
            composable(
                route = "order_detail/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                OrderDetailScreen(orderId = orderId, navController = navController)
            }




        }
    }
}
