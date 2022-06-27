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
    state: (AnimationState) -> Unit,
    delayShowBigCircle: Long = 2000,
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
    var currentState by remember { mutableStateOf(AnimationState.LOADING() as AnimationState) }
    state(currentState)

    Log.d("mytag", "state "+currentState)

    val transition by mutableStateOf(updateTransition(currentState, label = ""))

    var showContent by remember { mutableStateOf(false) }

    val bigCircleRadius = getFirstCircleRadiusByTransition(
        animationObjectList = tooltipsList,
        transition = transition ,
        bigCircleRadiusAnimation = if (transition.currentState.isDisappearingPhase()) disappearingBigCircleAnim else bigCircleRadiusAnimation
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

    if (currentState == AnimationState.LOADING()) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayShowBigCircle)
                currentState = AnimationState.SHOW_BIG_CIRCLE
            }
        )
    }
    if (currentState == AnimationState.SHOW_BIG_CIRCLE) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase2)
                currentState = AnimationState.SHOW_SMALL_CIRCLE
            }
        )
    }

    if (currentState == AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase4)
                currentState = AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE2
            }
        )
    }

    if (currentState == AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE2) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase5)
                currentState = AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE3
            }
        )
    }
    if (currentState == AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE3) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayPhase6)
                currentState = AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE4
            }
        )
    }

    Box(
        modifier = modifier
    ) {
        if (tooltipsList.isNotEmpty()) {
            showContent = transition.targetState >= AnimationState.SHOW_SMALL_CIRCLE

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
                            if (it.isAfter(AnimationState.SHOW_BIG_CIRCLE)  && it.state <= tooltipsList.size + 1) {
                                tooltipsList[it.state - 2].objectToShow()
                            } else if (it.state < 0) {
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
                            if (it.isAfter(AnimationState.SHOW_SMALL_CIRCLE) && it.state <= tooltipsList.size + 1) {
                                tooltipsList[it.state - 2].composeDescription()
                            } else if (it.state < 0) {
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
                        currentState = currentState.nextPhase(tooltipsList.size)

//                    if (currentState.state == tooltipsList.size + 1) {
//                        currentState = AnimationState.DISAPPEAR_BIG_CIRCLE_PHASE
//                    } else if (currentState >= 0 && currentState.state < tooltipsList.size + 1) {
//                        currentState += 1
//                    } else if (currentState.state < 0) {
//                        currentState = currentState.nextPhase(tooltipsList.size)
//                    }
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
    transition: Transition<AnimationState>,
    bigCircleRadiusAnimation: FiniteAnimationSpec<Dp>
): Dp {

    val value by transition.animateDp(
        transitionSpec = {
            bigCircleRadiusAnimation
        }, label = "bigCircleRadius"
    ) {
        if (it == AnimationState.LOADING()) {
            0.dp
        } else if (it == AnimationState.SHOW_BIG_CIRCLE) {
            animationObjectList.first().bigCircleRadius
        } else if (it.state > 1) {
            animationObjectList[it.state - 2].bigCircleRadius
        } else if (it.state <= -3) {
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
    transition: Transition<AnimationState>,
    smallCircleRadiusAnimation: FiniteAnimationSpec<Dp>
): Dp {
    val value by transition.animateDp(
        transitionSpec = {
            smallCircleRadiusAnimation
        }, label = ""
    ) {
        if (it.state == 0 || it.state == 1) {
            0.dp
        } else if (it.state > 1) {
            animationObjectList[it.state - 2].smallCircleRadius
        } else {
            animationObjectList.last().smallCircleRadius
        }
    }
    return value
}

@Composable
fun getBigCircleColorByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<AnimationState>,
    bigCircleColorAnimation: FiniteAnimationSpec<Color>,
    bigCircleColorBeforeDisappearing: Color?
): Color {
    val value by transition.animateColor(
        transitionSpec = {
            bigCircleColorAnimation
        }, label = ""
    ) {
        if (it.state == 0 || it.state == 1) {
            animationObjectList.first().bigCircleColor
        } else if (it.state > 1) {
            animationObjectList[it.state - 2].bigCircleColor
        } else if (it.state == -1) {
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
    transition: Transition<AnimationState>,
    smallCircleColorAnimation: FiniteAnimationSpec<Color>
): Color {

    val value by transition.animateColor(
        transitionSpec = {
            smallCircleColorAnimation
        }, label = ""
    ) {
        if (it.state == 0 || it.state == 1) {
            animationObjectList.first().smallCircleColor
        } else if (it.state > 2) {
            animationObjectList[it.state - 2].smallCircleColor
        } else {
            animationObjectList.last().smallCircleColor
        }
    }
    return value
}

@Composable
fun getCircleOffsetByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<AnimationState>,
    offsetAnimation: FiniteAnimationSpec<Offset>
): Offset {
    val value by transition.animateOffset(
        transitionSpec = {
            offsetAnimation
        }, label = "bigCircleRadius"
    ) {

        val animObject: AnimationObject = if (it.state == 0 || it.state == 1) {
            animationObjectList.first()
        } else if (it.state > 1) {
            animationObjectList[it.state - 2]
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
    transition: Transition<AnimationState>,
    offsetAnimation: FiniteAnimationSpec<Offset>
): Offset {
    val value by transition.animateOffset(
        transitionSpec = {
            offsetAnimation
        }, label = ""
    ) {
        if (it.state == 0 || it.state == 1) {
            animationObjectList.first().objectOffset
        } else if (it.state > 1) {
            animationObjectList[it.state - 2].objectOffset
        } else {
            animationObjectList.last().objectOffset
        }
    }
    return value
}

@Composable
fun getDescriptionOffsetByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<AnimationState>,
    offsetAnimation: FiniteAnimationSpec<Offset>
): Offset {
    val value by transition.animateOffset(
        transitionSpec = {
            offsetAnimation
        }, label = ""
    ) {
        if (it.state == 0 || it.state == 1) {
            animationObjectList.first().composeDescriptionOffset
        } else if (it.state > 1) {
            animationObjectList[it.state - 2].composeDescriptionOffset
        } else {
            animationObjectList.last().composeDescriptionOffset
        }
    }
    return value
}