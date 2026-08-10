package com.moodkeyra.keyboard.ime

import android.inputmethodservice.InputMethodService
import android.os.Build
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.moodkeyra.keyboard.data.MoodPreferences
import com.moodkeyra.keyboard.model.Mood

class MoodInputMethodService : InputMethodService() {
    private lateinit var keyboardView: MoodKeyboardView
    private lateinit var preferences: MoodPreferences
    private var isPasswordField = false

    override fun onCreate() {
        super.onCreate()
        preferences = MoodPreferences(this)
    }

    override fun onCreateInputView(): View = MoodKeyboardView(this).also { view ->
        keyboardView = view
        view.mood = preferences.selectedMood
        view.onMood = ::selectMood
        view.onKey = ::handleKey
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        isPasswordField = info?.inputType?.let(::isPasswordInput) == true
        keyboardView.symbols = info?.inputType?.and(InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER
        keyboardView.mood = if (isPasswordField) Mood.NORMAL else preferences.selectedMood
    }

    private fun handleKey(key: KeyboardKey) {
        val connection = currentInputConnection ?: return
        when (key) {
            is KeyboardKey.Character -> {
                val char = if (keyboardView.shifted) key.value.uppercaseChar() else key.value
                connection.commitText(char.toString(), 1)
                if (keyboardView.shifted) keyboardView.shifted = false
            }
            KeyboardKey.Backspace -> connection.deleteSurroundingTextInCodePoints(1, 0)
            KeyboardKey.Enter -> {
                val action = currentInputEditorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_NONE
                if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
                    connection.performEditorAction(action)
                } else {
                    connection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                    connection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                }
                clearOneMessageMood()
            }
            KeyboardKey.MoodPicker -> selectMood(Mood.NORMAL)
            KeyboardKey.Shift -> keyboardView.shifted = !keyboardView.shifted
            KeyboardKey.Space -> connection.commitText(" ", 1)
            KeyboardKey.SwitchKeyboard -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    switchToNextInputMethod(false)
                } else {
                    (getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager)
                        .showInputMethodPicker()
                }
            }
            KeyboardKey.Symbols -> keyboardView.symbols = !keyboardView.symbols
        }
    }

    private fun selectMood(mood: Mood) {
        if (isPasswordField) return
        preferences.selectedMood = mood
        keyboardView.mood = mood
    }

    private fun clearOneMessageMood() {
        if (preferences.oneMessageOnly) selectMood(Mood.NORMAL)
    }

    private fun isPasswordInput(inputType: Int): Boolean {
        val inputClass = inputType and InputType.TYPE_MASK_CLASS
        val variation = inputType and InputType.TYPE_MASK_VARIATION
        return when (inputClass) {
            InputType.TYPE_CLASS_TEXT -> variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
            InputType.TYPE_CLASS_NUMBER -> variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD
            else -> false
        }
    }
}
