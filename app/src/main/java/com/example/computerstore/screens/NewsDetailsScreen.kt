package com.example.computerstore.screens

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.viewmodel.BlogViewModel
import com.example.computerstore.data.model.Blog
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.navigation.NavController
import com.example.computerstore.R
import com.example.computerstore.screens.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    navController: NavController,
    blogId: Int,
    blogViewModel: BlogViewModel = viewModel(),
    onBackClick: () -> Unit,
) {
    val currentBlog by blogViewModel.currentBlog.collectAsState()

    LaunchedEffect(blogId) {
        blogViewModel.loadBlog(blogId)
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Chi tiết bài viết",
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        },
    )
    { padding ->
        currentBlog?.let { blog ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(8.dp)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Thumbnail
                item {
                    blog.blog_thumbnail_url?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = blog.blog_title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Title
                item {
                    Text(
                        text = blog.blog_title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                }

                // Author + Date
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tác giả
                        Text(
                            text = blog.blog_author_name ?: "Không rõ tác giả",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Ngày xuất bản
                        blog.published_at?.let { ts ->
                            val date = ts.toDate()
                            val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Ngày xuất bản",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = formatted,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }


                // Description
                item {
                    blog.blog_description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                    }
                }

                // Blog Content as HTML with WebView
                item {
                    blog.blog_content?.let { html ->
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 300.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = false
                                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                                    webViewClient = WebViewClient()

                                    // CSS: justify + strip khoảng trắng
                                    val styledHtml = """
                        <html>
                        <head>
                          <style>
                            body {
                              font-family: sans-serif;
                              font-size: 14px;
                              color: #111;
                              line-height: 1.6;
                              padding: 0;
                              margin: 0;
                              text-align: justify;         /* căn đều nội dung */
                              white-space: normal;         /* bỏ giữ khoảng trắng */
                              word-break: break-word;      /* xuống dòng khi từ quá dài */
                            }
                            img {
                              max-width: 100%;
                              height: auto;
                              border-radius: 8px;
                              display: block;
                              margin: 8px auto;           /* căn giữa ảnh */
                            }
                            h2, h3 { color: #0066FF; }
                            a {
                              color: #428bca;
                              text-decoration: none;
                            }
                          </style>
                        </head>
                        <body>$html</body>
                        </html>
                    """.trimIndent()

                                    loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
                                }
                            },
                            update = { webView ->
                                val styledHtml = """
                    <html>
                    <head>
                      <style>
                        body {
                          font-family: sans-serif;
                          font-size: 14px;
                          color: #111;
                          line-height: 1.6;
                          padding: 0;
                          margin: 0;
                          text-align: justify;
                          white-space: normal;
                          word-break: break-word;
                        }
                        img {
                          max-width: 100%;
                          height: auto;
                          border-radius: 8px;
                          display: block;
                          margin: 8px auto;
                        }
                        h2, h3 { color: #0066FF; }
                        a {
                          color: #428bca;
                          text-decoration: none;
                        }
                      </style>
                    </head>
                    <body>${blog.blog_content}</body>
                    </html>
                """.trimIndent()
                                webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
                            }
                        )
                    }
                }


                // Tags
                item {
                    blog.tags?.let { tags ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Tags",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                tags.split(",").forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            text = tag.trim(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Views
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Views",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lượt xem: ${blog.views}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

            }
        } ?: run {
            // Loading state
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
