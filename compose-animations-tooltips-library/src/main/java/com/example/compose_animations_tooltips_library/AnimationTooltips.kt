package com.example.compose_animations_tooltips_library


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


/**
 * This function is used to easily create a series of animations describing subsequent functions in
 * the application.
 * The function consists of 5 phases:
- phase 0: No new objects on the screen (state: 0)
- phase 1: The appearance of a large circle from the first object on the tooltipsList: List <AnimationObject> (state: 1)
- phase 2: during this phase, the animations included in the list start to appear, in order to
           switch between animations it is required to overwrite the screen by the user (state: from 2 to tooltips.size+2)
- phase 3: disappearance of the icon, description and small circle (state == -1)
- phase 4: disappearing icon, description and small circle (state == -2)
- phase 5: changing the color of the large circle  (state == -3)
- phase 6: big circle disappearing animation (state == -4)
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun AnimationTooltips(
    modifier: Modifier = Modifier,
    tooltipsList: List<AnimationObject>,
    state: (Int) -> Unit,
    delayPhase1: Long = 2000,
    delayPhase2: Long = 1000,
    delayPhase4: Long = 500,
    delayPhase5: Long = 500,
    delayPhase6: Long = 500,

    bigCircleRadiusAnimation: FiniteAnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    disappearingBigCircleAnim: FiniteAnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    ),
    smallCircleRadiusAnimation: FiniteAnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    bigCircleColorAnimation: FiniteAnimationSpec<Color> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    smallCircleColorAnimation: FiniteAnimationSpec<Color> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    changingPositionAnim: FiniteAnimationSpec<Offset> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    ),
    indication: Indication = LocalIndication.current,
    bigCircleColorBeforeDisappearing: Color? = null
) {
    var currentState by remember { mutableStateOf(0) }
    state(currentState)

    Log.d("mytag", "state "+currentState)

    val transition by mutableStateOf(updateTransition(currentState, label = ""))
    var showContent by remember { mutableStateOf(false) }

    val bigCircleRadius = getFirstCircleRadiusByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        bigCircleRadiusAnimation = if (transition.currentState < 0) disappearingBigCircleAnim else bigCircleRadiusAnimation
    )

    val bigCircleColor = getBigCircleColorByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        bigCircleColorAnimation = bigCircleColorAnimation,
        bigCircleColorBeforeDisappearing = bigCircleColorBeforeDisappearing
    )

    val smallCircleRadius = getSecondCircleRadiusByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        smallCircleRadiusAnimation = smallCircleRadiusAnimation
    )

    val smallCircleColor = getSmallCircleColorByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        smallCircleColorAnimation = smallCircleColorAnimation
    )

    val circleOffset = getCircleOffsetByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        offsetAnimation = changingPositionAnim
    )

    val iconOffset = getIconOffsetByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        offsetAnimation = changingPositionAnim
    )

    val descriptionOffset = getDescriptionOffsetByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        offsetAnimation = changingPositionAnim
    )

    if (currentState == 0) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase1)
                currentState += 1
            }
        )
    }
    if (currentState == 1) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase2)
                currentState += 1
            }
        )
    }

    if (currentState == -1) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase4)
                currentState -= 1
            }
        )
    }
    if (currentState == -2) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase5)
                currentState -= 1
            }
        )
    }
    if (currentState == -3) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase6)
                currentState -= 1
            }
        )
    }

    Box(
        modifier = modifier
    ) {
        if (tooltipsList.isNotEmpty()) {
            showContent = transition.targetState >= 2

            DrawFirstCircle(
                centerPosition = circleOffset,
                radius = bigCircleRadius,
                color = bigCircleColor
            )

            Crossfade(targetState = showContent) { showContent ->
                if (showContent) {
                    DrawSecondCircle(
                        centerPosition = circleOffset,
                        radius = smallCircleRadius,
                        color = smallCircleColor
                    )
                    val iconOffsetX = with(LocalDensity.current) { iconOffset.x.toDp() }
                    val iconOffsetY = with(LocalDensity.current) { iconOffset.y.toDp() }
                    Box(modifier = Modifier.offset(iconOffsetX, iconOffsetY)) {
                        Crossfade(targetState = transition.targetState) {
                            if (it > 1 && it <= tooltipsList.size + 1) {
                                tooltipsList[it - 2].objectToShow()
                            } else if (it < 0) {
                                tooltipsList.last().objectToShow()
                            }
                        }
                    }
                    val descriptionOffsetX =
                        with(LocalDensity.current) { descriptionOffset.x.toDp() }
                    val descriptionOffsetY =
                        with(LocalDensity.current) { descriptionOffset.y.toDp() }
                    Box(modifier = Modifier.offset(descriptionOffsetX, descriptionOffsetY)) {
                        Crossfade(targetState = transition.targetState) {
                            if (it > 1 && it <= tooltipsList.size + 1) {
                                tooltipsList[it - 2].composeDescription()
                            } else if (it < 0) {
                                tooltipsList.last().composeDescription
                            }
                        }
                    }


                }
            }

        }

        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    if (currentState == tooltipsList.size + 1) {
                        currentState = -1
                    } else if (currentState >= 0 && currentState < tooltipsList.size + 1) {
                        currentState += 1
                    } else if (currentState < 0) {
                        currentState -= 1
                    }
                },
                indication = indication,
                interactionSource = remember { MutableInteractionSource() }
            )
            .background(Color.Transparent))


    }
}


@Composable
fun DrawFirstCircle(
    centerPosition: Offset,
    radius: Dp,
    color: Color
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = color,
            center = centerPosition,
            radius = radius.toPx()
        )
    }
}

@Composable
fun DrawSecondCircle(
    centerPosition: Offset,
    radius: Dp,
    color: Color

) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = color,
            center = centerPosition,
            radius = radius.toPx()
        )
    }
}

@Composable
fun getFirstCircleRadiusByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    bigCircleRadiusAnimation: FiniteAnimationSpec<Dp>
): Dp {

    val value by transition.animateDp(
        transitionSpec = {
            bigCircleRadiusAnimation
        }, label = "bigCircleRadius"
    ) {
        if (it == 0) {
            0.dp
        } else if (it == 1) {
            animationObjectList.first().bigCircleRadius
        } else if (it > 1) {
            animationObjectList[it - 2].bigCircleRadius
        } else if (it <= -3) {
            0.dp
        } else {
            animationObjectList.last().bigCircleRadius
        }
    }
    return value
}


@Composable
fun getSecondCircleRadiusByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    smallCircleRadiusAnimation: FiniteAnimationSpec<Dp>
): Dp {
    val value by transition.animateDp(
        transitionSpec = {
            smallCircleRadiusAnimation
        }, label = ""
    ) {
        if (it == 0 || it == 1) {
            0.dp
        } else if (it > 1) {
            animationObjectList[it - 2].smallCircleRadius
        } else {
            animationObjectList.last().smallCircleRadius
        }
    }
    return value
}

@Composable
fun getBigCircleColorByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    bigCircleColorAnimation: FiniteAnimationSpec<Color>,
    bigCircleColorBeforeDisappearing: Color?
): Color {
    val value by transition.animateColor(
        transitionSpec = {
            bigCircleColorAnimation
        }, label = ""
    ) {
        if (it == 0 || it == 1) {
            animationObjectList.first().bigCircleColor
        } else if (it > 1) {
            animationObjectList[it - 2].bigCircleColor
        } else if (it == -1) {
            animationObjectList.last().bigCircleColor
        } else {
            bigCircleColorBeforeDisappearing ?: animationObjectList.last().bigCircleColor
        }
    }
    return value
}

@Composable
fun getSmallCircleColorByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    smallCircleColorAnimation: FiniteAnimationSpec<Color>
): Color {

    val value by transition.animateColor(
        transitionSpec = {
            smallCircleColorAnimation
        }, label = ""
    ) {
        if (it == 0 || it == 1) {
            animationObjectList.first().smallCircleColor
        } else if (it > 2) {
            animationObjectList[it - 2].smallCircleColor
        } else {
            animationObjectList.last().smallCircleColor
        }
    }
    return value
}

@Composable
fun getCircleOffsetByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    offsetAnimation: FiniteAnimationSpec<Offset>
): Offset {
    val value by transition.animateOffset(
        transitionSpec = {
            offsetAnimation
        }, label = "bigCircleRadius"
    ) {

        val animObject: AnimationObject = if (it == 0 || it == 1) {
            animationObjectList.first()
        } else if (it > 1) {
            animationObjectList[it - 2]
        } else {
            animationObjectList.last()
        }
        val circleOffset = animObject.objectOffset
        val objectSize = animObject.objectSize
        val x = circleOffset.x + (objectSize.width / 2)
        val y = circleOffset.y + (objectSize.height / 2)
        Offset(x, y)
    }
    return value
}

@Composable
fun getIconOffsetByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    offsetAnimation: FiniteAnimationSpec<Offset>
): Offset {
    val value by transition.animateOffset(
        transitionSpec = {
            offsetAnimation
        }, label = ""
    ) {
        if (it == 0 || it == 1) {
            animationObjectList.first().objectOffset
        } else if (it > 1) {
            animationObjectList[it - 2].objectOffset
        } else {
            animationObjectList.last().objectOffset
        }
    }
    return value
}

@Composable
fun getDescriptionOffsetByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    offsetAnimation: FiniteAnimationSpec<Offset>
): Offset {
    val value by transition.animateOffset(
        transitionSpec = {
            offsetAnimation
        }, label = ""
    ) {
        if (it == 0 || it == 1) {
            animationObjectList.first().composeDescriptionOffset
        } else if (it > 1) {
            animationObjectList[it - 2].composeDescriptionOffset
        } else {
            animationObjectList.last().composeDescriptionOffset
        }
    }
    return value
}