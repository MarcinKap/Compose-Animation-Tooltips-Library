# Compose Animation Tooltips Library

 This function is used to easily create a series of animations describing subsequent functions in the application.
The function consists of 5 phases:
- phase 0: No new objects on the screen (state: 0)
- phase 1: The appearance of a large circle from the first object on the tooltipsList: List <AnimationObject> (state: 1)
- phase 2: during this phase, the animations included in the list start to appear, in order to switch between animations it is required to overwrite the screen by the user (state: from 2 to tooltips.size+2)
- phase 3: disappearance of the icon, description and small circle (state == -1)
- phase 4: disappearing icon, description and small circle (state == -2)
- phase 5: changing the color of the large circle  (state == -3)
- phase 6: big circle disappearing animation (state == -4)
