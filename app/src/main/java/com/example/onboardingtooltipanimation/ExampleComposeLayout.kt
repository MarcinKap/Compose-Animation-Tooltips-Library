package com.example.onboardingtooltipanimation

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.compose_animations_tooltips_library.AnimationObject
import com.example.compose_animations_tooltips_library.AnimationTooltips


@Composable
fun ExampleView() {

    var personIconPosition by remember { mutableStateOf(Offset.Zero) }
    var personIconIntSize by remember { mutableStateOf(IntSize.Zero) }
    var searchIconPosition by remember { mutableStateOf(Offset.Zero) }
    var searchIconSize by remember { mutableStateOf(IntSize.Zero) }
    var informationIconPosition by remember { mutableStateOf(Offset.Zero) }
    var informationIconSize by remember { mutableStateOf(IntSize.Zero) }
    var customTextFieldPosition by remember { mutableStateOf(Offset.Zero) }
    var customTextFieldSize by remember { mutableStateOf(IntSize.Zero) }
    var emojiIconPosition by remember { mutableStateOf(Offset.Zero) }
    var emojiIconSize by remember { mutableStateOf(IntSize.Zero) }
    var emailIconPosition by remember { mutableStateOf(Offset.Zero) }
    var emailIconSize by remember { mutableStateOf(IntSize.Zero) }
    var photoIconPosition by remember { mutableStateOf(Offset.Zero) }
    var photoIconSize by remember { mutableStateOf(IntSize.Zero) }
    var localizationIconPosition by remember { mutableStateOf(Offset.Zero) }
    var localizationIconSize by remember { mutableStateOf(IntSize.Zero) }

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
                )
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
                )
            ),
            //InformationIcon
            AnimationObject(
                bigCircleRadius = 400.dp,
                bigCircleColor = color4,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { InfoIcon(color = iconAnimationColor) },
                objectOffset = informationIconPosition,
                objectSize = informationIconSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Information Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "this icon is used to check information \nabout the application",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    personIconPosition.x.plus(personIconIntSize.width.times(2)),
                    personIconPosition.y.plus(personIconIntSize.height.times(2)),
                )
            ),
            //CustomTextField
            AnimationObject(
                bigCircleRadius = 400.dp,
                bigCircleColor = color4,
                smallCircleRadius = 240.dp,
                smallCircleColor = Color.White,
                objectToShow = { Box(modifier = Modifier.padding(end = 32.dp)) { customTextField() } },
                objectOffset = customTextFieldPosition,
                objectSize = customTextFieldSize,
                composeDescription = {

                    Column(
                        Modifier
                            .padding(top = 250.dp, start = 50.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Text field",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "It's a place to write a message to send",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
            ),
            //EmojiIcon
            AnimationObject(
                bigCircleRadius = 300.dp,
                bigCircleColor = color4,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { IconEmoji(color = iconAnimationColor) },
                objectOffset = emojiIconPosition,
                objectSize = emojiIconSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Emoji Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "Button for inserting emoji icons into text",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    emojiIconPosition.x.plus(personIconIntSize.width).minus(100),
                    emojiIconPosition.y.minus(personIconIntSize.height.times(2)).minus(50),
                )
            ),
            //EmailIcon
            AnimationObject(
                bigCircleRadius = 300.dp,
                bigCircleColor = color4,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { EmailIcon(color = iconAnimationColor) },
                objectOffset = emailIconPosition,
                objectSize = emailIconSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Email Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "Button for linking text",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    emailIconPosition.x.plus(personIconIntSize.width).minus(100),
                    emailIconPosition.y.minus(personIconIntSize.height.times(2)).minus(50),
                )
            ),
            //PhotoIcon
            AnimationObject(
                bigCircleRadius = 300.dp,
                bigCircleColor = color4,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { PhotoIcon(color = iconAnimationColor) },
                objectOffset = photoIconPosition,
                objectSize = photoIconSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Photo Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "Button for inserting photos",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    photoIconPosition.x.plus(personIconIntSize.width).minus(100),
                    photoIconPosition.y.minus(personIconIntSize.height.times(2)).minus(50),
                )
            ),
            //LocalizationIcon
            AnimationObject(
                bigCircleRadius = 300.dp,
                bigCircleColor = color4,
                smallCircleRadius = 50.dp,
                smallCircleColor = Color.White,
                objectToShow = { LocationIcon(color = iconAnimationColor) },
                objectOffset = localizationIconPosition,
                objectSize = localizationIconSize,
                composeDescription = {
                    Column() {
                        Text(
                            text = "Localization Icon",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = "Button for sending your \nlocation",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                },
                composeDescriptionOffset = Offset(
                    photoIconPosition.x.plus(personIconIntSize.width).minus(90),
                    photoIconPosition.y.minus(personIconIntSize.height.times(2)).minus(50),
                )
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
                    InfoIcon(
                        modifier = Modifier
                            .onSizeChanged {
                                informationIconSize = it
                            }
                            .onGloballyPositioned {
                                informationIconPosition = it.positionInRoot()
                            }
                    )
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
                Box(modifier = Modifier
                    .onSizeChanged {
                        customTextFieldSize = it
                    }
                    .onGloballyPositioned {
                        customTextFieldPosition = it.positionInRoot()
                    }
                ) {
                    customTextField()
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
                            modifier = Modifier
                            .onSizeChanged {
                                emojiIconSize = it
                            }
                            .onGloballyPositioned {
                                emojiIconPosition = it.positionInRoot()
                            }
                        )
                        EmailIcon(
                            modifier = Modifier
                                .onSizeChanged {
                                    emailIconSize = it
                                }
                                .onGloballyPositioned {
                                    emailIconPosition = it.positionInRoot()
                                }
                        )
                        PhotoIcon(
                            modifier = Modifier
                                .onSizeChanged {
                                    photoIconSize = it
                                }
                                .onGloballyPositioned {
                                    photoIconPosition= it.positionInRoot()
                                }
                        )
                        LocationIcon(
                            modifier = Modifier
                                .onSizeChanged {
                                    localizationIconSize = it
                                }
                                .onGloballyPositioned {
                                    localizationIconPosition = it.positionInRoot()
                                }
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
            AnimationTooltips(
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
fun customTextField() {
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
