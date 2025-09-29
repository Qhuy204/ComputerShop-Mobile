package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.computerstore.data.model.Blog
import com.example.computerstore.screens.components.BlogItem
import com.example.computerstore.screens.components.BlogItemGrid
import com.example.computerstore.screens.components.HeaderSection
import com.example.computerstore.viewmodel.BlogViewModel
import com.example.computerstore.R

@Composable
fun NewsScreen(
    blogViewModel: BlogViewModel = viewModel(),
    onBlogClick: (Blog) -> Unit,
    navController: androidx.navigation.NavController,
) {
    val blogs by blogViewModel.blogs.collectAsState()
    var isGrid by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        blogViewModel.loadAllBlogs()
    }

    // Lọc theo search
    val filteredBlogs = blogs.filter {
        it.blog_title.contains(searchQuery, ignoreCase = true) ||
                (it.blog_description?.contains(searchQuery, ignoreCase = true) == true)
    }

    if (isGrid) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header + Toggle nằm trong Grid
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    HeaderSection(
                        searchQuery = "",
                        onSearchChange = {},
                        focusManager = LocalFocusManager.current,
                        navController = navController
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { isGrid = !isGrid },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (isGrid) R.drawable.layout_list else R.drawable.layout_grid
                                ),
                                contentDescription = "Toggle layout",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Blogs
            items(filteredBlogs) { blog ->
                BlogItemGrid(blog, onClick = { onBlogClick(blog) })
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header + Toggle nằm trong List
            item {
                Column {
                    HeaderSection(
                        searchQuery = "",
                        onSearchChange = {},
                        focusManager = LocalFocusManager.current,
                        navController = navController
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { isGrid = !isGrid },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (isGrid) R.drawable.layout_list else R.drawable.layout_grid
                                ),
                                contentDescription = "Toggle layout",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Blogs
            items(filteredBlogs) { blog ->
                BlogItem(blog, onClick = { onBlogClick(blog) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
