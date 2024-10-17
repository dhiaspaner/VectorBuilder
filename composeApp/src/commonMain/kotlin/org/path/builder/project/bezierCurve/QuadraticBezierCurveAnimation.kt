package org.path.builder.project.bezierCurve

import AnimatedLine
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class QuadraticBezierCurveAnimation {

    val line1 = AnimatedLine()
    val line2 = AnimatedLine()


    private var drawIncrement by mutableIntStateOf(0)

    // Animatables to control the progress of L1 and L2 along the lines\

    private var animationL1Progress = Animatable(0f)
    private var animationL2Progress = Animatable(0f)
    private var animationQ1Progress = Animatable(0f)

    // Coroutine scope to launch animations
    private var animationScope: CoroutineScope? = null

    var bezierCurvePath = Path()
    val pointsList = mutableListOf<Offset>()

    suspend fun onTap(offset: Offset) {
        if (line1.p1.isUnspecified) {
            line1.p1 = offset
        } else if (line1.p2.isUnspecified) {
            line1.p2 = offset
            line1.startAnimation()
            line2.p1 = offset
        } else if (line2.p2.isUnspecified) {
            line2.p2 = offset
            line2.startAnimation()
            startAnimations()
        } else {
            if (line1.isLineDefined && line2.isLineDefined) {
                bezierCurvePath.reset()
                line1.resetPoints()
                line2.resetPoints()
                animationL1Progress.snapTo(0f)
                animationL2Progress.snapTo(0f)
                animationQ1Progress.snapTo(0f)
            }
        }
        drawIncrement++
    }


    // Start animations for L1 and L2 once both lines are defined
    private suspend fun startAnimations() {
        coroutineScope {
            val animationL1Progress = async(Dispatchers.IO) {
                animationL1Progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 10000)
                )
            }
            val animationL2Progress = async(Dispatchers.IO) {
                animationL2Progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 10000)
                )
            }

            val animationQ1Progress = async(Dispatchers.IO) {
                animationQ1Progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 10000)
                )
            }

            awaitAll(animationL1Progress, animationL2Progress, animationQ1Progress)

        }

    }


    fun DrawScope.drawBezierPath() {
        if (line1.isLineDefined && line2.isLineDefined) {
            val pointL1 = lerp(line1.p1, line1.p2, animationL1Progress.value)
            val pointL2 = lerp(line2.p1, line2.p2, animationL2Progress.value)
            val pointQ3 = lerp(pointL1, pointL2, animationQ1Progress.value)

            drawCircle(color = Color.Yellow, radius = 10f, center = pointQ3)

            if (animationQ1Progress.isRunning) {
                drawLine(
                    color = Color.Green,
                    start = pointL1,
                    end = pointL2,
                    strokeWidth = 5f
                )
            }


            if (animationQ1Progress.isRunning) {
                bezierCurvePath.apply {
                    reset()
                    moveTo(line1.p1.x, line1.p1.y)
                    quadraticBezierTo(pointL1.x, pointL1.y, pointQ3.x, pointQ3.y)
                }
            }
            drawPath(
                path = bezierCurvePath,
                style = Stroke(width = 5f),
                color = Color.Black
            )


        }
    }

    fun DrawScope.drawCircleOfSegments() {
        if (line1.isLineDefined) {
            val pointL1 = lerp(line1.p1, line1.p2, animationL1Progress.value)
            drawCircle(color = Color.Red, radius = 10f, center = pointL1)
        }

        if (line2.isLineDefined) {
            val pointL2 = lerp(line2.p1, line2.p2, animationL2Progress.value)
            drawCircle(color = Color.Blue, radius = 10f, center = pointL2)
        }
        if (line1.p1.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 5f,
                center = line1.p1
            )
        if (line1.p2.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 5f,
                center = line1.p2
            )

        if (line2.p1.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 5f,
                center = line2.p1
            )

        if (line2.p2.isSpecified)
            drawCircle(
                color = Color.Gray,
                radius = 5f,
                center = line2.p2
            )
    }

    // Draw the circles L1 and L2 on the lines
    fun draw(drawScope: DrawScope) {
        with(drawScope) {
            drawIncrement
            drawBezierPath()
            if (animationQ1Progress.value != 1f) {
                line1.drawLine(this)
                line2.drawLine(this)
            }
            drawCircleOfSegments()

        }

    }

    fun drawAdjustCubicBezierCurve(drawScope: DrawScope) {
//        p2 + (controlPoint1 - p2) * (2f / 3f)

        if (line1.isLineDefined && line2.isLineDefined) {
            val p2 = line2.p2 + (line2.p1 - line2.p2) * (2f / 3f)

//        if (animationQ1Progress.is) {
//            bezierCurvePath.apply {
//                reset()
//                moveTo(line1.p1.x, line1.p1.y)
//                quadraticBezierTo(pointL1.x, pointL1.y, pointQ3.x, pointQ3.y)
//            }
//        }
            val path = Path()
            path.moveTo(line1.p1.x, line1.p1.y)
            path.cubicTo(
                line1.p2.x, line1.p2.y,
                p2.x, p2.y,
                line2.p2.x, line2.p2.y
            )
            drawScope.drawPath(
                path = path,
                style = Stroke(width = 5f),
                color = Color.Green
            )

        }
    }

}
