package com.crexative.kotlincoroutinesandflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.crexative.kotlincoroutinesandflows.assigments.AssignmentTwoScreen
import com.crexative.kotlincoroutinesandflows.assigments.soundOfBirdsName
import com.crexative.kotlincoroutinesandflows.ui.theme.KotlinCoroutinesAndFlowsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        soundOfBirdsName(lifecycleScope)

        setContent {
            KotlinCoroutinesAndFlowsTheme {
                AssignmentTwoScreen()
            }
        }
    }
}
