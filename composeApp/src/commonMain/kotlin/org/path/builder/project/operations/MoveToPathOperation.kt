package org.path.builder.project.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.path.builder.project.InteractionEvent
import org.path.builder.project.drawControlPoint

class MoveToPathOperation(
    override val isAbstract: Boolean,
    val p: Offset
) : PathOperation {

    override var isStored = false

    override fun applyToPath(path: Path) {
        path.moveTo(p.x, p.y)
    }

    override fun onDraw(drawScope: DrawScope) {
        with(drawScope) {
            if (p.isSpecified) drawControlPoint(p)
        }
    }

    override fun onEvent(event: InteractionEvent) {

    }

    override fun toPathData(): String = "${if (isAbstract) "M" else "m" } ${p.x} ${p.y}"

}