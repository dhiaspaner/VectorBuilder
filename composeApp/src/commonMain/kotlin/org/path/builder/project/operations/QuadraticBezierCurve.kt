package org.path.builder.project.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.path.builder.project.InteractionEvent
import org.path.builder.project.drawControlPoint

class QuadraticBezierCurve(
    var p0: Offset = Offset.Unspecified,
    var controlPoint1: Offset = Offset.Unspecified,
    var p2: Offset = Offset.Unspecified,
    override val isAbstract: Boolean
) : PathOperation {
    override var isStored = false

    override fun applyToPath(path: Path) {
        path.quadraticBezierTo(
            controlPoint1.x, controlPoint1.x,
            p2.x, p2.y,
        )
    }

    fun applyToInternalPath() {
        path.moveTo(p0.x, p0.y)
        path.quadraticBezierTo(
            controlPoint1.x, controlPoint1.x,
            p2.x, p2.y,
        )
    }

    val path = Path()

    override fun onDraw(drawScope: DrawScope) {
        with(drawScope) {

            if (p0.isSpecified) drawControlPoint(p0)

            if (p2.isSpecified) drawControlPoint(p2)

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

            if (p0.isSpecified && p2.isSpecified && controlPoint1.isSpecified) {
                drawPath(
                    path = path,
                    style = Stroke(width = 5f),
                    color = Color.Gray
                )
            }
        }
    }

    override fun onEvent(event: InteractionEvent) = Unit

    override fun toPathData(): String = "${if (isAbstract) "Q" else "q"} $controlPoint1.$controlPoint1 "
}