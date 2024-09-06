package com.crexative.kotlincoroutinesandflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.crexative.kotlincoroutinesandflows.assigments.SoundOfBirdsApp
import com.crexative.kotlincoroutinesandflows.assigments.soundOfBirds
import com.crexative.kotlincoroutinesandflows.assigments.soundOfBirdsExtended
import com.crexative.kotlincoroutinesandflows.ui.theme.KotlinCoroutinesAndFlowsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinCoroutinesAndFlowsTheme {
                SoundOfBirdsApp()
            }
        }
    }
}
