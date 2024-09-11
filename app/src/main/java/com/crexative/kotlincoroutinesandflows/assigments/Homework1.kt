package com.crexative.kotlincoroutinesandflows.assigments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * Easy: Assignment #1
 *
 * Each morning, you wake up to the sound of birds.
 * Over time, you’ve noticed three unique bird sounds, each repeating at a different pace.
 * One bird makes a sound every second, the other every 2 seconds, and the last every 3 seconds.
 *
 * Instructions
 * Recreate the timing of each bird’s sounds using a single coroutine for each bird. Each coroutine should only print four times before completing.
 * The first bird makes a “Coo” sound.
 * The second bird makes a “Caw” sound.
 * The last bird makes a “Chirp” sound.
 */
fun soundOfBirds(coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        repeat(4) {
            println("Coo")
            delay(1000L)
        }
    }

    coroutineScope.launch {
        repeat(4) {
            println("Caw")
            delay(2000L)
        }
    }

    coroutineScope.launch {
        repeat(4) {
            println("Chirp")
            delay(3000L)
        }
    }
}

/**
 * Medium: Assignment #2
 *
 * Once woken up by the birds, you enjoy listening to them for a while.
 * Afterward, you must prepare for the day, so after listening to the birds for a while, you close your window and get ready.
 *
 * Instructions
 * Extend the previous assignment by removing the limitation of only printing four times to the console;
 * each coroutine can print indefinitely now. Add a mechanism to cancel all running coroutines after 10 seconds.
 */
fun soundOfBirdsExtended(coroutineScope: CoroutineScope) {
    val bird1 = coroutineScope.launch {
        while (true) {
            println("Coo")
            delay(1000L)
        }
    }

    val bird2 = coroutineScope.launch {
        while (true) {
            println("Caw")
            delay(2000L)
        }
    }

    val bird3 = coroutineScope.launch {
        while (true) {
            println("Chirp")
            delay(3000L)
        }
    }

    coroutineScope.launch {
        val measureTime = measureTimeMillis {
            delay(10000L)
            bird1.cancel()
            bird2.cancel()
            bird3.cancel()
        }
        println("Time spent is: $measureTime")
    }
}

/**
 * Hard: Assignment #3
 *
 * You decide to make a simple mobile application to simulate the birds’ sounds you hear in the morning.
 * After planning, you’ve decided on a single screen with three buttons.
 * Each button will show the bird's name on the screen and print its sound to the console.
 * Tapping a new button will replace the previous bird's name on the screen, and only the new bird’s sounds will print to the console.
 *
 * Instructions
 *
 * Create a single-screen app using coroutines in Compose.
 * Add three buttons, each representing a different bird.
 * Create a composable function for each bird that displays the bird’s name and launches a coroutine to print the bird's sound to the console.
 * Only render the composable of the bird represented by the last button tapped and ensure only the selected bird's sounds are printed.
 */
@Composable
fun SoundOfBirdsApp() {
    var isBirdSelected by remember {
        mutableStateOf(false)
    }

    var selectedBird by remember {
        mutableStateOf(Bird())
    }

    if (isBirdSelected) {
        DetailBirdSound(
            bird = selectedBird,
            onStopSound = {
                isBirdSelected = false
                selectedBird = Bird()
            }
        )
    } else {
        ListBirds(
            birds = listOfBirds.map { it.name },
            onBirdSelected = {
                selectedBird = listOfBirds.first { bird -> bird.name == it }
                isBirdSelected = true
            }
        )
    }
}

data class Bird(
    val name: String = "",
    val sound: String = "",
    val durationEachSound: Long = 0L,
)

val listOfBirds = listOf(
    Bird(name = "Bird 1", sound = "Coo", durationEachSound = 1000L),
    Bird(name = "Bird 2", sound = "Caw", durationEachSound = 2000L),
    Bird(name = "Bird 3", sound = "Chirp", durationEachSound = 3000L),
)

@Composable
fun ListBirds(
    birds: List<String>,
    onBirdSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "List of Birds",
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.padding(16.dp))

        birds.forEach { bird ->
            Button(
                modifier = Modifier.fillMaxWidth(0.8f),
                onClick = {
                    onBirdSelected(bird)
                }
            ) {
                Text(text = bird)
            }
        }
    }
}

@Composable
fun DetailBirdSound(
    bird: Bird,
    onStopSound: () -> Unit,
) {
    LaunchedEffect(key1 = bird.name) {
        while (true) {
            println(bird.sound)
            delay(bird.durationEachSound)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = bird.name)
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = {
            onStopSound()
        }) {
            Text(text = "Stop")
        }
    }
}