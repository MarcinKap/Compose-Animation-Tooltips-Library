package com.example.onboardingtooltipanimation


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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@Composable
fun AnimtionTooltips(
    modifier: Modifier = Modifier,
    tooltipsList: List<AnimationObject>,
    delay: Long = 2000,
    state: (Int) -> Unit,
    bigCircleRadiusAnimation: FiniteAnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    disappearingBigCircleAnim: FiniteAnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    ),
    disappearingDelay: Long = 500,
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
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    changingObjectSizeAnim: FiniteAnimationSpec<IntSize> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    indication: Indication = LocalIndication.current,
) {
    var currentState by remember { mutableStateOf(0) }
    state(currentState)

    var transition by mutableStateOf(updateTransition(currentState, label = ""))


//state -1 - znikanie tekstu i ikony wraz z małym kołem
//state -2 - znikanie dużego koła
//0 - zwiekszenie kola duzego z 0 do zadanej wartosći z pozycji 1  - 1
//1 - pokazanie małego koła oraz ikonki i tekstu

//    currentState 0 - (tooltips.size-1) animacje przejścia itd
//    currentState -1 - zniknięcie tekstu i ikony wraz z małym kołem
//    currentState -2 - zmiana koloru dużego koła i zniknięcie dużego koła

//    var bigCircleRadius: Dp by mutableStateOf(0.dp)
//    var bigCircleColor: Color by mutableStateOf(tooltipsList.first().bigCircleColor)
//    var smallCircleRadius: Dp by mutableStateOf(0.dp)
//    var smallCircleColor: Color by mutableStateOf(tooltipsList.first().smallCircleColor)
//    var centerPosition: Offset = tooltipsList.first().centerPosition
//    var objectSize: IntSize = IntSize.Zero

    var showContent by remember { mutableStateOf(false) }

    val bigCircleRadius = getFirstCircleRadiusByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        bigCircleRadiusAnimation = if (transition.currentState < 0) disappearingBigCircleAnim else bigCircleRadiusAnimation
    )

    val bigCircleColor = getBigCircleColorByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        bigCircleColorAnimation = bigCircleColorAnimation
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

//    val iconOffset = getOffsetByTransition(
//        animationObjectList = tooltipsList,
//        transition = transition,
//        offsetAnimation = changingPositionAnim
//    )

    val objectSize = getIntSizeByTransition(
        animationObjectList = tooltipsList,
        transition = transition,
        intSizeAnim = changingObjectSizeAnim
    )



    if (currentState == 0) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(disappearingDelay)
                currentState += 1
            }
        )
    }
    if (currentState < 0) {
        LaunchedEffect(
            key1 = currentState,
            block = {
                delay(500)
                currentState -= 1
            }
        )
    }


    Box(
        modifier = modifier
    ) {
        if (tooltipsList.isNotEmpty()) {
            showContent = transition.targetState >= 1

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
                            if (it > 0 && it <= tooltipsList.size) {
                                tooltipsList[it - 1].objectToShow()
                            } else if (it< 0) {
                                tooltipsList.last().objectToShow()
                            }
                        }
                    }
                    val descriptionOffsetX = with(LocalDensity.current) { iconOffset.x.toDp() }
                    val descriptionOffsetY = with(LocalDensity.current) { iconOffset.y.toDp() }
                    Box(modifier = Modifier.offset(iconOffsetX, iconOffsetY)) {
                        Crossfade(targetState = transition.targetState) {
                            if (it > 0 && it <= tooltipsList.size) {
                                tooltipsList[it - 1].composeDescription()
                            } else if (it< 0) {
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
                    if (currentState == tooltipsList.size) {
                        currentState = -1
                    } else if (currentState >= 0 && currentState < tooltipsList.size) {
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
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1].bigCircleRadius
        } else if (it <= -2) {
            0.dp
        } else {
            animationObjectList.last().bigCircleRadius
        }
    }
    return value
}

//@Composable
//fun getValueByTransition(
//    typeConverter: TwoWayConverter<Any, AnimationVector1D>,
//    animationObjectList: List<AnimationObject>,
//    transition: Transition<Int>,
//    bigCircleRadiusAnimation: FiniteAnimationSpec<Any>
//): Any {
//    val value by transition.animateValue(
//        typeConverter = typeConverter,
//        transitionSpec = {        bigCircleRadiusAnimation    },
//        label = "bigCircleRadius",
//    )
//    {
//        if (it == 0) {
//            0.dp
//        } else if (it > 0 && it <= animationObjectList.size) {
//            animationObjectList[it - 1].bigCircleRadius
//        } else if (it <= -2) {
//            0.dp
//        } else {
//            animationObjectList.last().bigCircleRadius
//        }
//    }
//    return value
//}


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
        if (it == 0) {
            0.dp
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1].smallCircleRadius
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
    bigCircleColorAnimation: FiniteAnimationSpec<Color>
): Color {

    val value by transition.animateColor(
        transitionSpec = {
            bigCircleColorAnimation
        }, label = ""
    ) {
        if (it == 0) {
            animationObjectList.first().bigCircleColor
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1].bigCircleColor
        } else {
            animationObjectList.last().bigCircleColor
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
        if (it == 0) {
            animationObjectList.first().smallCircleColor
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1].smallCircleColor
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

        val animObject: AnimationObject = if (it == 0) {
            animationObjectList.first()
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1]
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
        }, label = "bigCircleRadius"
    ) {
        if (it == 0) {
            animationObjectList.first().objectOffset
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1].objectOffset
        } else {
            animationObjectList.last().objectOffset
        }
    }
    return value
}

@Composable
fun getIntSizeByTransition(
    animationObjectList: List<AnimationObject>,
    transition: Transition<Int>,
    intSizeAnim: FiniteAnimationSpec<IntSize>
): IntSize {

    val value by transition.animateIntSize(
        transitionSpec = {
            intSizeAnim
        }, label = "bigCircleRadius"
    ) {
        if (it == 0) {
            animationObjectList.first().objectSize
        } else if (it > 0 && it <= animationObjectList.size) {
            animationObjectList[it - 1].objectSize
        } else {
            animationObjectList.last().objectSize
        }
    }
    return value
}