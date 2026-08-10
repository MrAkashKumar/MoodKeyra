package com.moodkeyra.keyboard.data

import android.content.Context
import com.moodkeyra.keyboard.model.Mood

class MoodPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var selectedMood: Mood
        get() = runCatching {
            Mood.valueOf(preferences.getString(KEY_MOOD, Mood.NORMAL.name).orEmpty())
        }.getOrDefault(Mood.NORMAL)
        set(value) {
            preferences.edit().putString(KEY_MOOD, value.name).apply()
        }

    var oneMessageOnly: Boolean
        get() = preferences.getBoolean(KEY_ONE_MESSAGE, false)
        set(value) {
            preferences.edit().putBoolean(KEY_ONE_MESSAGE, value).apply()
        }

    private companion object {
        const val PREFERENCES_NAME = "mood_keyboard_preferences"
        const val KEY_MOOD = "selected_mood"
        const val KEY_ONE_MESSAGE = "one_message_only"
    }
}
