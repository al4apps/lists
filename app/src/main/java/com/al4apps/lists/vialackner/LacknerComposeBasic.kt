package com.al4apps.lists.vialackner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.al4apps.lists.R
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun WithLackner() {
    TextFieldsScaffold()
}

@Composable
fun Lists() {
    val graphikFontFamily = FontFamily(
        Font(R.font.graphik_thin, weight = FontWeight.Thin),
        Font(R.font.graphik_extra_light, weight = FontWeight.ExtraLight),
        Font(R.font.graphik_light, weight = FontWeight.Light),
        Font(R.font.graphik_regular, weight = FontWeight.Normal),
        Font(R.font.graphik_medium, weight = FontWeight.Medium),
        Font(R.font.graphik_black, weight = FontWeight.Black),
        Font(R.font.graphik_bold, weight = FontWeight.Bold),
        Font(R.font.graphik_semi_bold, weight = FontWeight.SemiBold),
        Font(R.font.graphik_super, weight = FontWeight.ExtraBold),
    )
    val scrollState = rememberScrollState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(listOf("This", "is", "Jetpack", "Compose")) { i, item ->
            Text(
                text = item,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
//                fontFamily = graphikFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .verticalScroll(scrollState)
//    ) {
//        for (i in 0..50) {
//            Text(
//                text = "Item $i",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Black,
////                fontFamily = graphikFontFamily,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .padding(vertical = 16.dp)
//                    .fillMaxWidth()
//            )
//        }
//    }
}

@Composable
fun TextFieldsScaffold() {

    var textFieldState by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val snackbarState = remember {
        SnackbarHostState()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = textFieldState,
                label = {
                    Text("Enter your name")
                },
                onValueChange = { text ->
                    textFieldState = text
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                scope.launch {
                    snackbarState.showSnackbar("Hello $textFieldState")
                }
            }) {
                Text("Pls greet me")
            }
        }
    }
}


@Composable
fun ColorBox(modifier: Modifier = Modifier) {
    val colorState = remember {
        mutableStateOf(Color.Yellow)
    }
    Box(modifier = modifier
        .background(colorState.value)
        .clickable {
            colorState.value = Color(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat(),
                1f
            )
        }
    )
}

@Composable
fun TextStyling() {
    val graphikFontFamily = FontFamily(
        Font(R.font.graphik_thin, weight = FontWeight.Thin),
        Font(R.font.graphik_extra_light, weight = FontWeight.ExtraLight),
        Font(R.font.graphik_light, weight = FontWeight.Light),
        Font(R.font.graphik_regular, weight = FontWeight.Normal),
        Font(R.font.graphik_medium, weight = FontWeight.Medium),
        Font(R.font.graphik_black, weight = FontWeight.Black),
        Font(R.font.graphik_bold, weight = FontWeight.Bold),
        Font(R.font.graphik_semi_bold, weight = FontWeight.SemiBold),
        Font(R.font.graphik_super, weight = FontWeight.ExtraBold),
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("J")
                }
                append("etpack ")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("C")
                }
                append("ompose")
            },
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = graphikFontFamily,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            textDecoration = TextDecoration.None
        )
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .windowInsetsPadding(WindowInsets.systemBars),
        shape = RoundedCornerShape(15.dp),
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            ) {

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(title, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }
        }
    }
}

@Composable
fun BaseTextColumns() {
    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxWidth()
//            .padding(20.dp)
            .border(6.dp, Color.Black)
            .padding(6.dp)
            .border(6.dp, Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Hello", modifier = Modifier.offset(0.dp, 20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text("My")
        Text("World")
        Spacer(modifier = Modifier.height(20.dp))
    }
}