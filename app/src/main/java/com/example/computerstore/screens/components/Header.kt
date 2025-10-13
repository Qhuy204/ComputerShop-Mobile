package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.computerstore.R
import com.example.computerstore.navigation.BottomBarScreen
import com.example.computerstore.screens.buttons.CustomButton
import com.example.computerstore.ui.components.CustomTextField


@Composable
fun HeaderSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    focusManager: FocusManager,
    navController: NavController
) {
    val navController = rememberNavController()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFE30000))
            .padding(top = 22.dp, start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    navController.navigate(BottomBarScreen.Home.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://file.hstatic.net/200000636033/file/logo-mobile_1e5b7fc485b24cf985b3d63cfa1f88be.svg")
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = "Profile",
                modifier = Modifier.fillMaxSize()
            )
        }


        Spacer(modifier = Modifier.width(8.dp))

        CustomTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = "Search for a product",
            iconRes = R.drawable.search,
            focusManager = focusManager,
            isLastField = true,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(30.dp))
                .height(56.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        CustomButton(
            onClick = {
                navController.navigate(BottomBarScreen.Profile.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            iconRes = R.drawable.ic_user,
            backgroundColor = Color.Black,
            iconTint = Color.White,
            size = 56.dp,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(30.dp)
                )
        )

    }
}

//@Composable
//fun BannerSection1(
//    onCheckNowClick: () -> Unit = {}
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(130.dp)
//            .background(Color.Black)
//            .padding(horizontal = 16.dp)
//    ) {
//        Text(
//            text = "Power Up Your Next Upgrade",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//        Text(
//            text = "Top gear, best performance, trusted by gamers.",
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Normal,
//            color = Color.LightGray
//        )
//
//        Button(
//            onClick = onCheckNowClick,
//            modifier = Modifier
//                .width(200.dp)
//                .padding(top = 16.dp),
//            shape = RoundedCornerShape(5.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFE30000),
//                contentColor = Color.White
//            )
//        ) {
//            Text("Check Now", fontWeight = FontWeight.Bold)
//        }
//    }
//}
