package com.core.matrixrain.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.core.matrixrain.characters
import kotlinx.coroutines.delay
import kotlin.random.Random

// Created by Victor Hernandez on 27/12/21.
// Proyect MatrixRain
//contact victoralfonso920@gmail.com

@Composable
fun MatrixRain(stripCount: Int = 20) {
    Row {
        repeat(stripCount) {
            MatrixColumn(
                yStartDelay = Random.nextInt(8) * 1000L,
                crawlSpeed = (Random.nextInt(10) * 10L) + 100
            )
        }
    }

}

@Composable
fun RowScope.MatrixColumn(crawlSpeed: Long, yStartDelay: Long) {
    BoxWithConstraints(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        val maxWidthDp = maxWidth
        val matrixStrip = remember {
            Array((maxHeight / maxWidth).toInt()) {
                characters.random()
            }
        }
        var lettersToDraw by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(lettersToDraw) {
                MatrixChar(
                    fontSize = with(LocalDensity.current){maxWidthDp.toSp()},
                    char = matrixStrip[it],
                    crawlSpeed = crawlSpeed,
                    onFinished = {
                        if (it >= matrixStrip.size * 0.6) {
                            lettersToDraw = 0
                        }
                    })
            }
        }
        LaunchedEffect(Unit) {
            delay(yStartDelay)
            while (true) {
                if (lettersToDraw < matrixStrip.size) {
                    lettersToDraw += 1
                }

                if (lettersToDraw > matrixStrip.size * 0.5) {
                    matrixStrip[Random.nextInt(lettersToDraw)] = characters.random()
                }
                delay(crawlSpeed)
            }
        }
    }



}

@Composable
fun MatrixChar(
    fontSize:TextUnit,
    char: String,
    crawlSpeed: Long,
    onFinished: () -> Unit) {
    var textColor by remember { mutableStateOf(Color(0xffcefbe4)) }
    var startFade by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (startFade) 0f else 1f,
        animationSpec = tween(
            durationMillis = 4000,
            easing = LinearEasing
        ),
        finishedListener = {
            onFinished()
        }
    )
    Text(
        text = char,
        color = textColor.copy(alpha = alpha),
        fontSize = fontSize
    )

    LaunchedEffect(Unit) {
        delay(crawlSpeed)
        textColor = Color(0xff43c728)
        startFade = true
    }
}