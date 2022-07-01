package com.example.compose_animations_tooltips_library


import android.annotation.SuppressLint
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
    state: (OnboardingTootltipsState) -> Unit,
    delayShowBigCircle: Long = 2000,
    delayShowingContent: Long = 1000,
    delayBetweenDisappearContentAndChangingBigCircleColor: Long = 5000,
    delayBetweenChangeColorBigCircleAndDisappearingBigCircle: Long = 500,
    delayTurningOffOnboardingAfterLastPhase: Long = 500,

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
    var currentState by remember { mutableStateOf(OnboardingTootltipsState.LOADING as OnboardingTootltipsState) }
    state(currentState)

    val transition by mutableStateOf(updateTransition(currentState, label = ""))

    var showIconWithSmallCircle by remember { mutableStateOf(false) }

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

    if (currentState == OnboardingTootltipsState.LOADING) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayShowBigCircle)
                currentState = currentState.nextPhase(tooltipsList.size)
            }
        )
    }
    if (currentState == OnboardingTootltipsState.SHOW_BIG_CIRCLE) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayShowingContent)
                currentState = currentState.nextPhase(tooltipsList.size)
            }
        )
    }

    if (currentState == OnboardingTootltipsState.DISAPPEAR_PHASE_1_HIDE_CONTENT) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayBetweenDisappearContentAndChangingBigCircleColor)
                currentState = currentState.nextPhase(tooltipsList.size)
            }
        )
    }

    if (currentState == OnboardingTootltipsState.DISAPPEAR_PHASE_2_CHANGE_BIG_CIRCLE_COLOR) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayBetweenChangeColorBigCircleAndDisappearingBigCircle)
                currentState = currentState.nextPhase(tooltipsList.size)
            }
        )
    }
    if (currentState == OnboardingTootltipsState.DISAPPEAR_PHASE_3_HIDE_BIG_CIRCLE_SIZE) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(delayTurningOffOnboardingAfterLastPhase)
                currentState = currentState.nextPhase(tooltipsList.size)
            }
        )
    }

    Box(
        modifier = modifier
    ) {
        if (tooltipsList.isNotEmpty()) {
            showIconWithSmallCircle = transition.targetState > OnboardingTootltipsState.SHOW_BIG_CIRCLE && transition.targetState < OnboardingTootltipsState.DISAPPEAR_PHASE_1_HIDE_CONTENT

            DrawFirstCircle(
                centerPosition = circleOffset,
                radius = bigCircleRadius,
                color = bigCircleColor
            )

            Crossfade(targetState = showIconWithSmallCircle) { showContent ->
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
                            if (it is OnboardingTootltipsState.ANIMATIONS_PHASE) {
                                tooltipsList[it.value].objectToShow()
                            }
                        }
                    }
                    val descriptionOffsetX =
                        with(LocalDensity.current) { descriptionOffset.x.toDp() }
                    val descriptionOffsetY =
                        with(LocalDensity.current) { descriptionOffset.y.toDp() }
                    Box(modifier = Modifier.offset(descriptionOffsetX, descriptionOffsetY)) {
                        Crossfade(targetState = transition.targetState) {
                            if (it is OnboardingTootltipsState.ANIMATIONS_PHASE) {
                                tooltipsList[it.value].composeDescription()
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
    transition: Transition<OnboardingTootltipsState>,
    bigCircleRadiusAnimation: FiniteAnimationSpec<Dp>
): Dp {

    val value by transition.animateDp(
        transitionSpec = {
            bigCircleRadiusAnimation
        }, label = "bigCircleRadius"
    ) {
        if (it == OnboardingTootltipsState.LOADING) {
            0.dp
        } else if (it == OnboardingTootltipsState.SHOW_BIG_CIRCLE) {
            animationObjectList.first().bigCircleRadius
        } else if (it > OnboardingTootltipsState.SHOW_BIG_CIRCLE && it < OnboardingTootltipsState.DISAPPEAR_PHASE_1_HIDE_CONTENT) {
            animationObjectList[it.state - 2].bigCircleRadius
        } else if (it >= OnboardingTootltipsState.DISAPPEAR_PHASE_3_HIDE_BIG_CIRCLE_SIZE ) {
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
    transition: Transition<OnboardingTootltipsState>,
    smallCircleRadiusAnimation: FiniteAnimationSpec<Dp>
): Dp {
    val value by transition.animateDp(
        transitionSpec = {
            smallCircleRadiusAnimation
        }, label = ""
    ) {
        if (it is OnboardingTootltipsState.LOADING || it is OnboardingTootltipsState.SHOW_BIG_CIRCLE) {
            0.dp
        } else if (it is OnboardingTootltipsState.ANIMATIONS_PHASE) {
            animationObjectList[it.value].smallCircleRadius
        } else {
            animationObjectList.last().smallCircleRadius
        }
    }
    return value
}

@Composable
fun getBigCircleColorByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<OnboardingTootltipsState>,
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
    transition: Transition<OnboardingTootltipsState>,
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
    transition: Transition<OnboardingTootltipsState>,
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
    transition: Transition<OnboardingTootltipsState>,
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
    transition: Transition<OnboardingTootltipsState>,
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