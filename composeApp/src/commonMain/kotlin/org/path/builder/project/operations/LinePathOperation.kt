package org.path.builder.project.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.path.builder.project.InteractionEvent
import org.path.builder.project.drawControlPoint

data class LinePathOperation(
    var p0: Offset = Offset.Unspecified,
    var p1: Offset = Offset.Unspecified,
    override val isAbstract: Boolean
) : PathOperation {

    override var isStored = false

    override fun onDraw(drawScope: DrawScope) {
        if (p0.isUnspecified || p1.isUnspecified ) return
        with(drawScope) {
            if (p0.isSpecified) drawControlPoint(p0)
            if (p1.isSpecified) drawControlPoint(p1)
            if (p1.isSpecified && p0.isSpecified) {
                drawLine(
                    start = p0,
                    end = p1,
                    color = Color.Gray,
                    strokeWidth = 5f
                )
            }
        }
    }

    override fun applyToPath(path: Path) {
        path.moveTo(p0.x, p0.y)
        path.lineTo(p1.x, p1.y)
    }

    override fun onEvent(event: InteractionEvent) {
        when (event) {
//            is InteractionEvent.OnClick -> {
//                if (p0.isUnspecified) {
//                    p0 = event.offset
//                    return
//                }
//
//                if (p1.isUnspecified) {
//                    p1 = event.offset
//                }
//            }
            else -> Unit
        }
    }


    override fun toPathData(): String = "L ${p0.x} ${p0.y} ${p1.x} ${p1.y}"

}
