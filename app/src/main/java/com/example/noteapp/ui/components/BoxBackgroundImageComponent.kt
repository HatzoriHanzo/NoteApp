package com.example.noteapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.noteapp.feature_note.domain.model.Note
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay


@Composable
fun BoxBackgroundImageComponent(
    modifier: Modifier = Modifier,
    innerPaddings: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    val colorIndex = remember { Animatable(0f) }
    val colors = Note.noteColors
    val maxIndex = colors.size - 1
    val visible = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        colorIndex.animateTo(
            targetValue = maxIndex.toFloat(), animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 10000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val animatedColors = listOf(
        colors[colorIndex.value.toInt()], colors[(colorIndex.value.toInt() + 1) % colors.size]
    )

    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPaddings.calculateBottomPadding())
                .background(
                    brush = Brush.horizontalGradient(colors = animatedColors)
                ), contentAlignment = contentAlignment
        ) {
            content()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            visible.value = !visible.value
            delay(1000)
        }
    }
}