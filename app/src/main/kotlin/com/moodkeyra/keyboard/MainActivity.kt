package com.moodkeyra.keyboard

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.moodkeyra.keyboard.setup.KeyboardSetup
import com.moodkeyra.keyboard.setup.SetupStep

class MainActivity : Activity() {
    private lateinit var keyboardSetup: KeyboardSetup
    private var returningFromEnableSettings = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyboardSetup = KeyboardSetup(this)
        showCurrentStep()
    }

    override fun onResume() {
        super.onResume()
        if (!::keyboardSetup.isInitialized) return

        val step = keyboardSetup.currentStep()
        showCurrentStep(step)
        if (returningFromEnableSettings && step == SetupStep.SELECT) {
            returningFromEnableSettings = false
            keyboardSetup.showKeyboardPicker()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && ::keyboardSetup.isInitialized) showCurrentStep()
    }

    private fun showCurrentStep(step: SetupStep = keyboardSetup.currentStep()) {
        setContentView(ScrollView(this).apply {
            setBackgroundColor(Color.rgb(255, 253, 251))
            addView(buildContent(step))
        })
    }

    private fun buildContent(step: SetupStep) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setPadding(dp(24), dp(48), dp(24), dp(32))

        addView(title(if (step == SetupStep.READY) "✓" else "⌨", 56f))
        addView(title(titleFor(step), 26f))
        addView(body(descriptionFor(step)))
        addView(section("Works offline", "Your typing stays on this device."))
        addView(section("No account", "Install, enable, choose, and start typing."))
        addView(primaryAction(step))

        if (step == SetupStep.READY) {
            addView(EditText(context).apply {
                hint = "Tap here to try MoodKeyra"
                textSize = 17f
                minHeight = dp(64)
                setPadding(dp(16), dp(12), dp(16), dp(12))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                ).apply { topMargin = dp(20) }
            })
        }

        addView(privacyAction())
        addView(body("MoodKeyra 1.0.0 • Offline • No Internet permission"))
    }

    private fun primaryAction(step: SetupStep) = Button(this).apply {
        text = buttonTextFor(step)
        isAllCaps = false
        textSize = 17f
        isEnabled = step != SetupStep.READY
        setOnClickListener {
            when (step) {
                SetupStep.ENABLE -> {
                    returningFromEnableSettings = true
                    keyboardSetup.openEnableSettings()
                }
                SetupStep.SELECT -> keyboardSetup.showKeyboardPicker()
                SetupStep.READY -> Unit
            }
        }
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(58),
        ).apply { topMargin = dp(20) }
    }

    private fun titleFor(step: SetupStep): String = when (step) {
        SetupStep.ENABLE -> "Set up MoodKeyra"
        SetupStep.SELECT -> "One last tap"
        SetupStep.READY -> "MoodKeyra is ready"
    }

    private fun descriptionFor(step: SetupStep): String = when (step) {
        SetupStep.ENABLE -> "Tap the button, turn on MoodKeyra, then come back. We will guide the next step."
        SetupStep.SELECT -> "Choose MoodKeyra from Android's keyboard list."
        SetupStep.READY -> "Open WhatsApp, Messages, Instagram, or any text field and type normally."
    }

    private fun buttonTextFor(step: SetupStep): String = when (step) {
        SetupStep.ENABLE -> "Set up MoodKeyra"
        SetupStep.SELECT -> "Choose MoodKeyra"
        SetupStep.READY -> "Ready to type"
    }

    private fun title(text: String, size: Float) = TextView(this).apply {
        this.text = text
        textSize = size
        setTextColor(Color.rgb(32, 33, 36))
        gravity = Gravity.CENTER
        setPadding(0, dp(6), 0, dp(6))
    }

    private fun body(text: String) = TextView(this).apply {
        this.text = text
        textSize = 15f
        setTextColor(Color.rgb(80, 84, 90))
        gravity = Gravity.CENTER
        setPadding(0, dp(8), 0, dp(20))
    }

    private fun section(heading: String, description: String) = TextView(this).apply {
        text = "✓  $heading\n     $description"
        textSize = 16f
        setTextColor(Color.rgb(32, 33, 36))
        setPadding(dp(12), dp(14), dp(12), dp(14))
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    private fun privacyAction() = Button(this).apply {
        text = "Privacy"
        isAllCaps = false
        setOnClickListener {
            android.app.AlertDialog.Builder(this@MainActivity)
                .setTitle("MoodKeyra privacy")
                .setMessage(
                    "MoodKeyra works offline. It does not collect, transmit, sell, share, or retain typed content or personal information. " +
                        "It has no Internet permission, account, advertising, analytics, tracking, or cloud service. " +
                        "Only your manually selected mood and reset preference are stored privately on this device.",
                )
                .setPositiveButton("Close", null)
                .show()
        }
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { topMargin = dp(12) }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
