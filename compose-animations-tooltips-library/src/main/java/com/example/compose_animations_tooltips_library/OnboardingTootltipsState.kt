package com.example.compose_animations_tooltips_library

sealed class OnboardingTootltipsState(val state: Int) {

    object LOADING : OnboardingTootltipsState(0)
    object SHOW_BIG_CIRCLE : OnboardingTootltipsState(1)
    class ANIMATIONS_PHASE(val value: Int) : OnboardingTootltipsState(value + 2)
    object DISAPPEAR_PHASE_1_HIDE_CONTENT : OnboardingTootltipsState(-1)
    object DISAPPEAR_PHASE_2_CHANGE_BIG_CIRCLE_COLOR : OnboardingTootltipsState(-2)
    object DISAPPEAR_PHASE_3_HIDE_BIG_CIRCLE_SIZE : OnboardingTootltipsState(-3)
    object FINISHED : OnboardingTootltipsState(-4)

    fun isAfter(onboardingTootltipsState: OnboardingTootltipsState): Boolean {
        val animationStateValue = onboardingTootltipsState.state
        this.state.let {
            when {
                it > 0 && animationStateValue > 0 && it > animationStateValue -> return true
                it < 0 && animationStateValue < 0 && it < animationStateValue -> return true
                it < 0 && animationStateValue > 0 -> return true
                else -> {
                    return false
                }
            }
        }
    }

    fun nextPhase(animationListSize: Int): OnboardingTootltipsState {
            when {
                this is LOADING -> return SHOW_BIG_CIRCLE
                this is SHOW_BIG_CIRCLE -> return ANIMATIONS_PHASE(0)
                this is ANIMATIONS_PHASE && this.value != animationListSize-1 -> return ANIMATIONS_PHASE(this.value+1)
                this is ANIMATIONS_PHASE && this.value == animationListSize-1 -> return DISAPPEAR_PHASE_1_HIDE_CONTENT
                this is DISAPPEAR_PHASE_1_HIDE_CONTENT -> return DISAPPEAR_PHASE_2_CHANGE_BIG_CIRCLE_COLOR
                this is DISAPPEAR_PHASE_2_CHANGE_BIG_CIRCLE_COLOR -> return DISAPPEAR_PHASE_3_HIDE_BIG_CIRCLE_SIZE
                else -> return FINISHED
            }
    }

    fun isDisappearingPhase(): Boolean {
        if (this.state < 0) {
            return true
        }
        return false
    }

    operator fun compareTo(onboardingTootltipsState: OnboardingTootltipsState): Int {
        when {
            this.isAfter(onboardingTootltipsState) -> return 1
            (this is ANIMATIONS_PHASE && onboardingTootltipsState is ANIMATIONS_PHASE) && this.state > onboardingTootltipsState.value -> return 1
            (this is ANIMATIONS_PHASE && onboardingTootltipsState is ANIMATIONS_PHASE) && this.state == onboardingTootltipsState.value -> return 0
            this == onboardingTootltipsState -> return 0
            else -> return -1
        }
    }
}