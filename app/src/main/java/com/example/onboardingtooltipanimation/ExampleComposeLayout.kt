package com.example.onboardingtooltipanimation

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@Composable
fun ExampleView() {

    var personIconPosition by remember { mutableStateOf(Offset.Zero) }
    var personIconIntSize by remember { mutableStateOf(IntSize.Zero) }
    var searchIconPosition by remember { mutableStateOf(Offset.Zero) }
    var searchIconSize by remember { mutableStateOf(IntSize.Zero) }

    val color1 = Color(0xff15a8a6)
    val color2 = Color(0xffefaf23)
    val firstCircleColor = Color(0xff0082d5)
    val color4 = Color(0xffe4335d)
    val iconAnimationColor = Color(0xff2c3a8c)

    var animationTooltips: List<AnimationObject> =
        listOf(
            //PersonIcon
            AnimationObject(
                bigCircleRadius = 300.dp,
                bigCircleColor = firstCircleColor,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { PersonIcon(color = color2) },
                objectOffset = Offset(
                    personIconPosition.x,
                    personIconPosition.y
                ),
                objectSize = personIconIntSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Person Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "This icon is used to \nactivate the user menu",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    personIconPosition.x.plus(personIconIntSize.width.times(2)),
                    personIconPosition.y.plus(personIconIntSize.height.times(2)),
                ),
                damping = Spring.DampingRatioLowBouncy,
                stiffing = Spring.StiffnessLow
            ),
            //SearchingIcon
            AnimationObject(
                bigCircleRadius = 400.dp,
                bigCircleColor = color4,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { SearchIcon(color = iconAnimationColor) },
                objectOffset = searchIconPosition,
                objectSize = searchIconSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Search Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "This icon is for searching \nother people",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    personIconPosition.x.plus(personIconIntSize.width.times(2)),
                    personIconPosition.y.plus(personIconIntSize.height.times(2)),
                ),
                damping = Spring.DampingRatioLowBouncy,
                stiffing = Spring.StiffnessLow
            )

        )


    Surface() {
        Column() {
            Row(
                modifier = Modifier
                    .background(Color(0xFFB8C3FF))
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PersonIcon(
                    modifier = Modifier
                        .onSizeChanged {
                            personIconIntSize = it
                        }
                        .onGloballyPositioned {
                            personIconPosition = it.positionInRoot()
                        }
                )

                Box(Modifier.weight(1f)) {}

                Row {
                    SearchIcon(
                        modifier = Modifier
                            .onSizeChanged {
                                searchIconSize = it
                            }
                            .onGloballyPositioned {
                                searchIconPosition = it.positionInRoot()
                            }
                    )
                    InfoIcon()
                }
            }
            Box(modifier = Modifier.weight(1f)) {
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFB8C3FF))
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .height(56.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = "",
                        onValueChange = {}
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconEmoji(
//                            modifier = Modifier.onGloballyPositioned {
//                                personIconPosition = it.positionInWindow()
//                            }
                        )
                        EmailIcon(
                            modifier = Modifier.onGloballyPositioned {
//                                personIconPosition = it.positionInWindow()
                            }
                        )
                        PhotoIcon(
//                            modifier = Modifier.onGloballyPositioned {
//                                personIconPosition = it.positionInWindow()
//                            }
                        )
                        LocationIcon(
//                            modifier = Modifier.onGloballyPositioned {
//                                personIconPosition = it.positionInWindow()
//                            }
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .wrapContentWidth(),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "SEND")
                    }
                }
            }
        }

        var state by remember { mutableStateOf(0) }

        if (state > -4) {
            AnimtionTooltips(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.3f)),
                tooltipsList = animationTooltips,
                state = { state = it },
                bigCircleColorBeforeDisappearing = color1
            )
        }
    }
}

@Composable
fun IconEmoji(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_emoji_emotions_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
fun EmailIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_alternate_email_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
fun PhotoIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_insert_photo_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
fun LocationIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_location_on_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
fun PersonIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_person_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
fun SearchIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
fun InfoIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Box(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_info_black_24dp),
                contentDescription = null,
                tint = color
            )
        }
    }
}
