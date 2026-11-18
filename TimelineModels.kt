package com.empurra.editor.core

import androidx.compose.ui.graphics.Color

enum class LayerType { VIDEO, IMAGE, TEXT, AUDIO, EFFECT }

data class Keyframe(
    val timeSeconds: Double,
    val value: Double,
    val easing: Easing = Easing.BEZIER
)

enum class Easing { LINEAR, EASE_IN, EASE_OUT, EASE_IN_OUT, BEZIER }

data class BezierCurve(
    val x1: Double = 0.25,
    val y1: Double = 0.1,
    val x2: Double = 0.25,
    val y2: Double = 1.0
) {
    // Cubic Bezier y(t); x inversion can be added for exact time-parametric evaluation.
    fun y(t: Double): Double {
        val u = 1.0 - t
        return 3 * u * u * t * y1 + 3 * u * t * t * y2 + t * t * t
    }
}

data class AnimatedProperty(
    val name: String,
    val keyframes: List<Keyframe>,
    val curve: BezierCurve = BezierCurve()
)

data class TimeRemapPoint(val sourceTime: Double, val timelineTime: Double)

data class Layer(
    val id: String,
    val name: String,
    val type: LayerType,
    val startSeconds: Double = 0.0,
    val endSeconds: Double = 8.0,
    val keyframes: List<Keyframe> = emptyList(),
    val properties: List<AnimatedProperty> = emptyList(),
    val timeRemap: List<TimeRemapPoint> = emptyList(),
    val color: Color = Color(0xFF555D83)
) {
    companion object {
        fun video(name: String) = Layer("layer-${System.nanoTime()}", name, LayerType.VIDEO, color = Color(0xFF555D83))
        fun audio(name: String) = Layer("layer-${System.nanoTime()}", name, LayerType.AUDIO, color = Color(0xFF347C78))
    }
}

data class EditorProject(
    val name: String,
    val durationSeconds: Double,
    val frameRate: Int,
    val layers: List<Layer>
) {
    companion object {
        fun demo() = EditorProject(
            name = "Project 001",
            durationSeconds = 15.0,
            frameRate = 30,
            layers = listOf(
                Layer.video("Video 1").copy(keyframes = listOf(Keyframe(0.0, 0.0), Keyframe(2.4, 1.0))),
                Layer("text-1", "Text 1", LayerType.TEXT, color = Color(0xFF8054A8),
                    keyframes = listOf(Keyframe(0.0, 0.0), Keyframe(1.0, 100.0))),
                Layer.audio("Music 1")
            )
        )
    }
}
