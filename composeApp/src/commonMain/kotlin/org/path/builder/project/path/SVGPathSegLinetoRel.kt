package org.path.builder.project.path

import androidx.compose.ui.geometry.Offset
import org.path.builder.project.operations.LinePathOperation
import org.path.builder.project.operations.PathOperation

class SVGPathSegLinetoRel(
    val startPointX: Float,
    val startPointY: Float,
    var x: Float,
    var y: Float,
    override val pathSegTypeAsLetter: String,
    override val pathData: String,
) : SVGPathSeg {
    override val pathSegType: PathSegType = PathSegType.PathSegLineToRel

    override fun toOperation(): PathOperation = LinePathOperation(
        isAbstract = false,
        p0 = Offset(startPointX,startPointY),
        p1 = Offset(x,y)
    )
}
