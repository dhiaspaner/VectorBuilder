package org.path.builder.project.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.path.builder.project.InteractionEvent
import org.path.builder.project.drawControlPoint
import kotlin.math.pow
import kotlin.math.sqrt

class CubicBezierPathOperation(
    var p0: Offset = Offset.Unspecified,
    var controlPoint1: Offset = Offset.Unspecified,
    var controlPoint2: Offset = Offset.Unspecified,
    var p3: Offset = Offset.Unspecified,
    override val isAbstract: Boolean
) : PathOperation {

    override var isStored = false
    override fun applyToPath(path: Path) {
        path.moveTo(p0.x, p0.y)
        path.cubicTo(
            x1 = controlPoint1.x,
            y1 = controlPoint1.y,
            x2 = controlPoint2.x,
            y2 = controlPoint2.y,
            x3 = p3.x,
            y3 = p3.y
        )
    }

    fun applyToInternalPath() {
        path.moveTo(p0.x, p0.y)
        path.cubicTo(
            x1 = controlPoint1.x,
            y1 = controlPoint1.y,
            x2 = controlPoint2.x,
            y2 = controlPoint2.y,
            x3 = p3.x,
            y3 = p3.y,

            )
    }

    val path = Path()

    override fun onDraw(drawScope: DrawScope) {
        with(drawScope) {

            drawControlPoint(p0, color = Color.Red)
            drawControlPoint(controlPoint1, color = Color.Green)
            drawControlPoint(controlPoint2, color = Color.Yellow)
            drawControlPoint(p3)


            if (p0.isSpecified && controlPoint1.isSpecified) {
                drawLine(
                    color = Color.Black,
                    start = Offset(p0.x, p0.y),
                    end = Offset(controlPoint1.x, controlPoint1.y),
                )
                val inverseP1 = Offset(
                    x = p0.x * 2 - controlPoint1.x,
                    y = p0.y * 2 - controlPoint1.y
                )
                drawLine(
                    color = Color.Blue,
                    start = Offset(controlPoint1.x, controlPoint1.y),
                    end = inverseP1,
                )
                drawControlPoint(inverseP1)
            }

            if (p0.isSpecified && controlPoint2.isSpecified && p3.isSpecified && controlPoint1.isSpecified) {

                drawPath(
                    path = path,
                    style = Stroke(width = 5f),
                    color = Color.Gray
                )
            }
        }
    }

    override fun onEvent(event: InteractionEvent) {
        when (event) {
            is InteractionEvent.OnClick -> Unit
            else -> Unit
        }
    }

    fun bezierPoint(t: Float, p0: Offset, p1: Offset, p2: Offset): Offset {
        val x = (1 - t).pow(2) * p0.x + 2 * (1 - t) * t * p1.x + t.pow(2) * p2.x
        val y = (1 - t).pow(2) * p0.y + 2 * (1 - t) * t * p1.y + t.pow(2) * p2.y
        return Offset(x, y)
    }

    fun distance(p1: Offset, p2: Offset): Float {
        return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
    }

    fun isPointNearBezierCurve(
        point: Offset,
        p0: Offset,
        p1: Offset,
        p2: Offset,
        tolerance: Float = 1f,
        steps: Int = 1000
    ): Boolean {
        var minDistance = Float.MIN_VALUE

        // Iterate over 't' from 0 to 1 in small steps to approximate the curve
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            val curvePoint = bezierPoint(t, p0, p1, p2)
            val d = distance(point, curvePoint)

            // Track the minimum distance between the point and the curve
            if (d < minDistance) {
                minDistance = d
            }
        }

        // If the minimum distance is within the tolerance, the point is near the curve
        return minDistance <= tolerance
    }


    fun adjustQuadraticToCubic() {
        val newControlPoint2 = controlPoint2 + (controlPoint1 - controlPoint2) * (2f / 3f)
        p3 = controlPoint2
        controlPoint2 = newControlPoint2
    }

    override fun toPathData(): String =  "C"  +
            " ${controlPoint1.x} ${controlPoint1.y} " +
            "${controlPoint2.x} ${controlPoint2.y} " +
            "${p3.x} ${p3.y}"


}