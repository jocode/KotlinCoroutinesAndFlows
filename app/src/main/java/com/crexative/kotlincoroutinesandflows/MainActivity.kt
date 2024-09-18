package com.crexative.kotlincoroutinesandflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.crexative.kotlincoroutinesandflows.ui.screens.PhotoPickerScreen
import com.crexative.kotlincoroutinesandflows.ui.theme.KotlinCoroutinesAndFlowsTheme
import com.crexative.kotlincoroutinesandflows.util.compressor.BitmapCompressor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val compressor = BitmapCompressor(applicationContext)

        setContent {
            KotlinCoroutinesAndFlowsTheme {
                PhotoPickerScreen(compressor = compressor)
            }
        }
    }
}
