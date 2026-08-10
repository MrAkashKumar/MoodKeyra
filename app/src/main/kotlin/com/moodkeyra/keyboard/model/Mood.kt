package com.moodkeyra.keyboard.model

import android.graphics.Color

enum class Mood(
    val symbol: String,
    val displayName: String,
    val surfaceColor: Int,
    val accentColor: Int,
    val textColor: Int,
) {
    NORMAL("—", "Normal", Color.rgb(242, 244, 247), Color.rgb(72, 78, 86), Color.rgb(32, 33, 36)),
    HAPPY("😊", "Happy", Color.rgb(255, 244, 204), Color.rgb(231, 169, 0), Color.rgb(84, 47, 0)),
    EXCITED("🤩", "Excited", Color.rgb(255, 224, 222), Color.rgb(224, 82, 82), Color.rgb(106, 21, 32)),
    CALM("😌", "Calm", Color.rgb(230, 242, 246), Color.rgb(79, 144, 166), Color.rgb(29, 60, 71)),
    SAD("😔", "Sad", Color.rgb(233, 237, 242), Color.rgb(94, 114, 143), Color.rgb(36, 55, 78)),
    ANGRY("😠", "Angry", Color.rgb(253, 232, 231), Color.rgb(179, 58, 58), Color.rgb(68, 22, 22)),
    THOUGHTFUL("💭", "Thoughtful", Color.rgb(238, 239, 246), Color.rgb(106, 112, 152), Color.rgb(50, 54, 66)),
    ROMANTIC("💗", "Romantic", Color.rgb(252, 232, 238), Color.rgb(200, 93, 122), Color.rgb(95, 45, 66)),
}
