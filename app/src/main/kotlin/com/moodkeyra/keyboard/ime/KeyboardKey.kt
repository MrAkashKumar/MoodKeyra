package com.moodkeyra.keyboard.ime

sealed interface KeyboardKey {
    data class Character(val value: Char) : KeyboardKey
    data object Shift : KeyboardKey
    data object Backspace : KeyboardKey
    data object Symbols : KeyboardKey
    data object MoodPicker : KeyboardKey
    data object Space : KeyboardKey
    data object SwitchKeyboard : KeyboardKey
    data object Enter : KeyboardKey
}
