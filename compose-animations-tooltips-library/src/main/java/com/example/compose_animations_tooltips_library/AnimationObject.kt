package com.example.compose_animations_tooltips_library


import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

data class AnimationObject(
    val bigCircleRadius: Dp,
    val bigCircleColor: Color,
    val smallCircleRadius: Dp = 0.dp,
    val smallCircleColor: Color,
    val objectToShow: @Composable () -> Unit,
    val objectOffset: Offset,
    val objectSize: IntSize,
    val composeDescription: @Composable () -> Unit = {},
    val composeDescriptionOffset: Offset = Offset.Zero,
)