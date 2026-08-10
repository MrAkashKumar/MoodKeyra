package com.moodkeyra.keyboard.ime

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.moodkeyra.keyboard.model.Mood
import kotlin.math.floor

class MoodKeyboardView(context: Context) : View(context) {
    var onKey: ((KeyboardKey) -> Unit)? = null
    var onMood: ((Mood) -> Unit)? = null
    var mood: Mood = Mood.NORMAL
        set(value) {
            field = value
            invalidate()
        }
    var shifted = false
        set(value) {
            field = value
            invalidate()
        }
    var symbols = false
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }
    private val keyBounds = mutableListOf<Pair<RectF, KeyboardKey>>()
    private val moodBounds = mutableListOf<Pair<RectF, Mood>>()
    private var pressedKey: KeyboardKey? = null
    private var pressedMood: Mood? = null
    private val cornerRadius get() = 8f * resources.displayMetrics.density

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), dp(290))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(mood.surfaceColor)
        keyBounds.clear()
        moodBounds.clear()
        drawMoodStrip(canvas)
        val rows = if (symbols) SYMBOL_ROWS else LETTER_ROWS
        rows.forEachIndexed { index, row -> drawRow(canvas, row, dp(58 + index * 55)) }
    }

    private fun drawMoodStrip(canvas: Canvas) {
        val cellWidth = width / 7f
        Mood.entries.filterNot { it == Mood.NORMAL }.forEachIndexed { index, item ->
            val rect = RectF(index * cellWidth, 0f, (index + 1) * cellWidth, dp(55).toFloat())
            val isHighlighted = item == mood || item == pressedMood
            if (isHighlighted) drawRounded(canvas, rect, item.accentColor)
            paint.color = if (isHighlighted) Color.WHITE else item.textColor
            paint.textSize = sp(22f)
            canvas.drawText(item.symbol, rect.centerX(), dp(27).toFloat(), paint)
            paint.textSize = sp(9f)
            canvas.drawText(item.displayName, rect.centerX(), dp(47).toFloat(), paint)
            moodBounds += rect to item
        }
    }

    private fun drawRow(canvas: Canvas, row: List<Pair<String, KeyboardKey>>, top: Int) {
        val gap = dp(4).toFloat()
        val horizontalInset = if (row.size == 9) dp(16).toFloat() else dp(4).toFloat()
        val cellWidth = (width - horizontalInset * 2 - gap * (row.size - 1)) / row.size
        row.forEachIndexed { index, (label, key) ->
            val left = horizontalInset + index * (cellWidth + gap)
            val rect = RectF(left, top.toFloat(), left + cellWidth, top + dp(50).toFloat())
            val color = when {
                key == pressedKey -> blend(Color.WHITE, mood.accentColor)
                key == KeyboardKey.MoodPicker -> mood.accentColor
                else -> Color.WHITE
            }
            drawRounded(canvas, rect, color)
            paint.color = if (key == KeyboardKey.MoodPicker) Color.WHITE else mood.textColor
            paint.textSize = sp(if (label.length > 2) 13f else 20f)
            val shown = if (key is KeyboardKey.Character && shifted) label.uppercase() else label
            canvas.drawText(shown, rect.centerX(), rect.centerY() - (paint.ascent() + paint.descent()) / 2, paint)
            keyBounds += rect to key
        }
    }

    private fun drawRounded(canvas: Canvas, rect: RectF, color: Int) {
        paint.color = color
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pressedMood = moodAt(event.x, event.y)
                pressedKey = keyAt(event.x, event.y)
                if (pressedMood != null || pressedKey != null) {
                    performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                val releasedMood = moodAt(event.x, event.y)
                val releasedKey = keyAt(event.x, event.y)
                when {
                    pressedMood != null && pressedMood == releasedMood -> onMood?.invoke(releasedMood!!)
                    pressedKey != null && pressedKey == releasedKey -> onKey?.invoke(releasedKey!!)
                }
                pressedMood = null
                pressedKey = null
                performClick()
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                pressedMood = null
                pressedKey = null
                invalidate()
            }
        }
        return true
    }

    override fun performClick(): Boolean = super.performClick()

    private fun dp(value: Int): Int = floor(value * resources.displayMetrics.density).toInt()
    private fun sp(value: Float): Float = value * resources.displayMetrics.scaledDensity
    private fun keyAt(x: Float, y: Float): KeyboardKey? = keyBounds.firstOrNull { it.first.contains(x, y) }?.second
    private fun moodAt(x: Float, y: Float): Mood? = moodBounds.firstOrNull { it.first.contains(x, y) }?.second

    private fun blend(first: Int, second: Int): Int = Color.rgb(
        (Color.red(first) + Color.red(second)) / 2,
        (Color.green(first) + Color.green(second)) / 2,
        (Color.blue(first) + Color.blue(second)) / 2,
    )

    private companion object {
        val LETTER_ROWS = listOf(
            "qwertyuiop".map { it.toString() to KeyboardKey.Character(it) },
            "asdfghjkl".map { it.toString() to KeyboardKey.Character(it) },
            listOf("⇧" to KeyboardKey.Shift) + "zxcvbnm".map { it.toString() to KeyboardKey.Character(it) } + ("⌫" to KeyboardKey.Backspace),
            listOf("?123" to KeyboardKey.Symbols, "🎭" to KeyboardKey.MoodPicker, "space" to KeyboardKey.Space, "◎" to KeyboardKey.SwitchKeyboard, "↵" to KeyboardKey.Enter),
        )
        val SYMBOL_ROWS = listOf(
            "1234567890".map { it.toString() to KeyboardKey.Character(it) },
            listOf("@", "#", "$", "%", "&", "-", "+", "(", ")").map { it to KeyboardKey.Character(it.single()) },
            listOf("!", "\"", "'", ":", ";", "/", "?", ".", ",").map { it to KeyboardKey.Character(it.single()) },
            listOf("ABC" to KeyboardKey.Symbols, "🎭" to KeyboardKey.MoodPicker, "space" to KeyboardKey.Space, "◎" to KeyboardKey.SwitchKeyboard, "↵" to KeyboardKey.Enter),
        )
    }
}
