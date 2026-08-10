package com.moodkeyra.keyboard.setup

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import com.moodkeyra.keyboard.ime.MoodInputMethodService

enum class SetupStep {
    ENABLE,
    SELECT,
    READY,
}

class KeyboardSetup(private val context: Context) {
    private val inputMethodManager: InputMethodManager
        get() = context.getSystemService(InputMethodManager::class.java)

    fun currentStep(): SetupStep = when {
        !isEnabled() -> SetupStep.ENABLE
        !isSelected() -> SetupStep.SELECT
        else -> SetupStep.READY
    }

    fun openEnableSettings() {
        context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
    }

    fun showKeyboardPicker() {
        inputMethodManager.showInputMethodPicker()
    }

    private fun isEnabled(): Boolean = inputMethodManager.enabledInputMethodList.any { method ->
        method.packageName == context.packageName &&
            method.serviceName == MoodInputMethodService::class.java.name
    }

    private fun isSelected(): Boolean {
        val selectedMethod = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD,
        )
        val selectedComponent = ComponentName.unflattenFromString(selectedMethod.orEmpty())
        return selectedComponent?.packageName == context.packageName &&
            selectedComponent.className == MoodInputMethodService::class.java.name
    }
}
