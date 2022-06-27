package com.example.compose_animations_tooltips_library

sealed class AnimationState(val state: Int) {

    class LOADING : AnimationState(0) {
    }

    object SHOW_BIG_CIRCLE: AnimationState(1)
    object SHOW_SMALL_CIRCLE: AnimationState(2)
    object SHOW_ICON: AnimationState(3)


    class ANIMATIONS_PHASE(value: Int): AnimationState(value)

    object DISAPPEAR_BIG_CIRCLE_PHASE: AnimationState(-1)
    object DISAPPEAR_BIG_CIRCLE_PHASE2: AnimationState(-2)
    object DISAPPEAR_BIG_CIRCLE_PHASE3: AnimationState(-3)
    object DISAPPEAR_BIG_CIRCLE_PHASE4: AnimationState(-4)
    object FINISHED: AnimationState(-4)

    fun isAfter(animationState: AnimationState): Boolean{
        val animationStateValue = animationState.state

        this.state.let {
            when{
                it > 0 && animationStateValue > 0 && it > animationStateValue -> return true
                it < 0 && animationStateValue < 0 && it < animationStateValue -> return true
                it < 0 && animationStateValue > 0  -> return true
                else -> { return false }
            }
        }
    }

    fun nextPhase(animationListSize: Int): AnimationState{
        this.state.let {
            return when{
                it == 0 -> SHOW_BIG_CIRCLE
                it == 1 -> SHOW_SMALL_CIRCLE
                it == 2 -> SHOW_ICON
                it > 3 && it <= animationListSize -> ANIMATIONS_PHASE(it+1)
                it == animationListSize+1 -> DISAPPEAR_BIG_CIRCLE_PHASE
                it == DISAPPEAR_BIG_CIRCLE_PHASE.state -> DISAPPEAR_BIG_CIRCLE_PHASE2
                it == DISAPPEAR_BIG_CIRCLE_PHASE2.state -> DISAPPEAR_BIG_CIRCLE_PHASE3
                it == DISAPPEAR_BIG_CIRCLE_PHASE3.state -> DISAPPEAR_BIG_CIRCLE_PHASE4
                else -> DISAPPEAR_BIG_CIRCLE_PHASE4
            }
        }
    }

    fun isDisappearingPhase(): Boolean{
        if (this.state<0){
            return true
        }
        return false
    }

    operator fun compareTo(animationState: AnimationState): Int {
        val animationStateValue = animationState.state

        this.state.let {
            when{
                it > 0 && animationStateValue > 0 && it > animationStateValue -> return 1
                it < 0 && animationStateValue < 0 && it < animationStateValue -> return 1
                it < 0 && animationStateValue > 0  -> return 1
                else -> { return -1 }
            }
        }
    }

}