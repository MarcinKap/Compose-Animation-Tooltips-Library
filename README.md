# Animation Tooltips Library - Jetpack Compose

This function is used to easily create a series of animations describing subsequent functions in the application.
The function consists of 7 phases:
- phase 0: No new objects on the screen (state: LOADING)
- phase 1: The appearance of a large circle from the first object on the tooltipsList: List <AnimationObject> (state: SHOW_BIG_CIRCLE)
- phase 2: during this phase, the animations included in the list start to appear, in order to
           switch between animations it is required to overwrite the screen by the user (state: ANIMATIONS_PHASE)
- phase 3: disappearance of the icon, description and small circle (state == DISAPPEAR_PHASE_1_HIDE_CONTENT)
- phase 4: changing the color of the large circle  (state == DISAPPEAR_PHASE_2_CHANGE_BIG_CIRCLE_COLOR)
- phase 5: big circle disappearing animation (state == DISAPPEAR_PHASE_3_HIDE_BIG_CIRCLE_SIZE)
- phase 6: end of onboarding (state == FINISHED)

# Download

Add the code below to your gradle files

	repositories {
		google()
		mavenCentral()
		maven { url 'https://jitpack.io' }
		...
	}

	dependencies {
		implementation 'com.github.MarcinKap:Compose_Animation_Tooltips_Library:1.0.0'
	}

# Example of use

https://user-images.githubusercontent.com/53196103/167458382-cb2c4e7d-9d29-4878-9ef2-ae1450f73e8b.mov

# How to use
	
The main function of the animation is the "AnimationTooltips" function.
One of the parameters of this animation is the "tooltipsList" that is the "AnimationObject" list
	
	fun AnimationTooltips(
	    modifier: Modifier = Modifier,
	    tooltipsList: List<AnimationObject>,
	    state: (Int) -> Unit,
	    ...
	)

Each element of the list must have the following data:
- the size and color of the main circle
- the displayed object
- the location of the object
- the size of the object

The position of a given object and its size should be obtained by declaring variables as follows:

	var personIconPosition by remember {mutableStateOf (Offset.Zero)}
	var personIconIntSize by remember {mutableStateOf (IntSize.Zero)}

And obtaining their final sizes in the place where the displayed object was triggered:

	PersonIcon (
	    modifier = Modifier
		.onSizeChanged {
			personIconIntSize = it
		}
		.onGloballyPositioned {
			personIconPosition = it.positionInRoot ()
		}
	)

Every time the animation state changes, the application sets the state 'state' and its final value is -4. Therefore, I recommend that you use the following code to disable the animation:

	var state by remember { mutableStateOf(0) }
	if (state > AnimationState.FINISHED.value) {
	    AnimationTooltips(
		modifier = Modifier
		    .fillMaxSize()
		    .background(Color.Black.copy(0.3f)),
		tooltipsList = animationTooltips,
		state = { state = it },
	    )
	}
