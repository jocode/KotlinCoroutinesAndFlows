package com.crexative.kotlincoroutinesandflows.assigments

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.crexative.kotlincoroutinesandflows.util.RotatingBoxScreen
import com.crexative.kotlincoroutinesandflows.util.compressor.PhotoProcessor
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * [Easy] Assignment #1
 *
 * Throughout your day, you came up with a name for each bird: Tweety, Zazu, and Woodstock.
 *
 * Instructions
 * Continuing from the previous program you’ve written in Coroutine Basics, give each coroutine a name
 * and print them out alongside the sounds they make.
 */
fun soundOfBirdsName(coroutineScope: CoroutineScope) {
    val bird1 = coroutineScope.launch(CoroutineName("Tweety")) {
        repeat(4) {
            println("Tweety: Coo")
            delay(1000L)
        }
    }

    val bird2 = coroutineScope.launch(CoroutineName("Zazu")) {
        repeat(4) {
            println("Zazu: Caw")
            delay(2000L)
        }
    }

    val bird3 = coroutineScope.launch(CoroutineName("Woodstock")) {
        repeat(4) {
            println("Woodstock: Chirp")
            delay(3000L)
        }
    }

    coroutineScope.launch {
        val timeSpent = measureTimeMillis {
            bird1.join()
            bird2.join()
            bird3.join()
        }
        println("Time spent: $timeSpent ms")
    }
}

/**
 * [Medium] Assignment #2
 *
 * You’ve taken a couple of pictures of the birds you hear each morning, and you decide to write an
 * application to select one of these pictures and then set the app’s background to the predominant color
 * in the image.
 *
 * Instructions
 *
 * Create an application with a single screen containing a button and the RotatingBox composable.
 * Tapping the button should launch a picker that allows the user to select an image.
 * Once an image is selected, use the function findDominantColor to get the primary color in the image.
 * Change the app’s background to this new color.
 *
 * When executing this program, the rotating box should be running smoothly at all times.
 */
@Composable
fun AssignmentTwoScreen() {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var backgroundColor by remember {
        mutableStateOf(Color.White)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        photoUri = it
    }

    LaunchedEffect(photoUri) {
        if (photoUri != null) {
            val bitmap = context.contentResolver.openInputStream(photoUri!!).use {
                BitmapFactory.decodeStream(it)
            }
            isLoading = true
            val dominantColor = PhotoProcessor.findDominantColor(bitmap)
            isLoading = false
            backgroundColor = Color(dominantColor)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        RotatingBoxScreen()
        Spacer(modifier = Modifier.height(64.dp))
        Button(onClick = {
            scope.launch {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }
        }) {
            Text(text = "Pick Image")
        }
        if (isLoading) {
            Text(text = "Finding dominant color...")
        }

        if (photoUri != null) {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(context)
                    .data(data = photoUri)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null
            )
        }
    }
}