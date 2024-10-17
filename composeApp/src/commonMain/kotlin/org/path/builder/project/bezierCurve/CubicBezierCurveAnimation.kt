package org.path.builder.project.bezierCurve

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class CubicBezierCurveAnimation {


    var p1 by mutableStateOf<Offset>(Offset.Unspecified)
    var p2 by mutableStateOf(Offset.Unspecified)

    var p3 by mutableStateOf(Offset.Unspecified)
    var p4 by mutableStateOf(Offset.Unspecified)


    private var drawIncrement by mutableIntStateOf(0)

    // Animatables to control the progress of L1 and L2 along the lines\

    private var animationL1Progress = Animatable(0f)
    private var animationL2Progress = Animatable(0f)
    private var animationL3Progress = Animatable(0f)
    private var animationQ1Progress = Animatable(0f)
    private var animationQ2Progress = Animatable(0f)
    private var animationQ3Progress = Animatable(0f)

    // Coroutine scope to launch animations
    private var animationScope: CoroutineScope? = null

    var bezierCurvePath = Path()

    val pointsList = mutableListOf<Offset>()

    suspend fun onTap(offset: Offset) {
        if (p1.isUnspecified) {
            p1 = offset
        } else if (p2.isUnspecified) {
            p2 = offset
        } else if (p3.isUnspecified) {
            p3 = offset
        } else if (p4.isUnspecified) {
            p4 = offset
            startAnimations()
        } else {
            if (p1.isSpecified && p2.isSpecified && p3.isSpecified && p4.isSpecified) {
                bezierCurvePath.reset()
                p1 = Offset.Unspecified
                p2 = Offset.Unspecified
                p3 = Offset.Unspecified
                p4 = Offset.Unspecified
                animationL1Progress.snapTo(0f)
                animationL2Progress.snapTo(0f)
                animationL3Progress.snapTo(0f)
                animationQ1Progress.snapTo(0f)
                animationQ2Progress.snapTo(0f)
                animationQ3Progress.snapTo(0f)
            }
            drawIncrement++

        }
        drawIncrement++
    }


    // Start animations for L1 and L2 once both lines are defined
    private suspend fun startAnimations() {
        coroutineScope {
            val jobs = mutableListOf<Deferred<AnimationResult<Float, AnimationVector1D>>>()

            listOf(
                animationL1Progress,
                animationL2Progress,
                animationL3Progress,
                animationQ1Progress,
                animationQ2Progress,
                animationQ3Progress
            ).forEach {
                jobs.add(
                    async(Dispatchers.IO) {
                        it.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 10000)
                        )
                    }
                )
            }

            jobs.awaitAll()

        }

    }


    fun DrawScope.drawBezierPath() {
        if (p1.isSpecified && p2.isSpecified && p3.isSpecified && p4.isSpecified) {
            val pointL1 = lerp(p1, p2, animationL1Progress.value)
            val pointL2 = lerp(p2, p3, animationL2Progress.value)
            val pointL3 = lerp(p3, p4, animationL3Progress.value)


            val pointQ1 = lerp(pointL1, pointL2, animationQ1Progress.value)
            val pointQ2 = lerp(pointL2, pointL3, animationQ2Progress.value)

            val pointQ3 = lerp(pointQ1, pointQ2, animationQ3Progress.value)

            drawCircle(color = Color.Yellow, radius = 10f, center = pointQ3)


            drawLine(
                color = Color.Green,
                start = pointL1,
                end = pointL2,
                strokeWidth = 5f
            )

            drawLine(
                color = Color.Red,
                start = pointL2,
                end = pointL3,
                strokeWidth = 5f
            )

            drawLine(
                color = Color.Blue,
                start = pointQ1,
                end = pointQ2,
            )


            bezierCurvePath.apply {
                reset()
                moveTo(p1.x, p1.y)

                cubicTo(pointL1.x, pointL1.y, pointL2.x, pointL2.y, pointQ3.x, pointQ3.y)
            }
//            }
            drawPath(
                path = bezierCurvePath,
                style = Stroke(width = 5f),
                color = Color.Black
            )
        }
    }

    fun DrawScope.drawCircleOfSegments() {
        if (p1.isSpecified && p2.isSpecified) {
            val pointL1 = lerp(p1, p2, animationL1Progress.value)
            drawCircle(color = Color.Red, radius = 10f, center = pointL1)
        }

        if (p2.isSpecified && p3.isSpecified) {
            val pointL2 = lerp(p2, p3, animationL2Progress.value)
            drawCircle(color = Color.Blue, radius = 10f, center = pointL2)
        }
        if (p1.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 10f,
                center = p1
            )
        if (p2.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 10f,
                center = p2
            )

        if (p3.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 10f,
                center = p3
            )

        if (p4.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 10f,
                center = p4
            )
    }

    // Draw the circles L1 and L2 on the lines
    fun draw(drawScope: DrawScope) {
        with(drawScope) {
            drawIncrement

            drawBezierPath()

            if (p1.isSpecified && p2.isSpecified)
                drawLine(
                    start = p1,
                    end = p2,
                    color = Color.Black
                )

            if (p3.isSpecified && p2.isSpecified)
                drawLine(
                    start = p2,
                    end = p3,
                    color = Color.Black
                )

            if (p3.isSpecified && p4.isSpecified)
                drawLine(
                    start = p3,
                    end = p4,
                    color = Color.Black
                )

            drawCircleOfSegments()

        }

    }

}