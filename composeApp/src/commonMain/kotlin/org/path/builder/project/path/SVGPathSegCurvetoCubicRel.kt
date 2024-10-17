package org.path.builder.project.path

import androidx.compose.ui.geometry.Offset
import org.path.builder.project.operations.CubicBezierPathOperation
import org.path.builder.project.operations.PathOperation

class SVGPathSegCurvetoCubicRel(
    val startPointX: Float,
    val startPointY: Float,
    var x: Float,
    var y: Float,
    var x1: Float,
    var y1: Float,
    var x2: Float,
    var y2: Float,
    override val pathSegTypeAsLetter: String,
    override val pathData: String,
) : SVGPathSeg {

    override val pathSegType = PathSegType.PathSegCurveToCubicRel

    private val starOffset = Offset(startPointX, startPointY)

    override fun toOperation(): PathOperation {
        return CubicBezierPathOperation(
            isAbstract = false,
            p0 = starOffset,
            controlPoint1 = (starOffset + Offset(x, y)),
            controlPoint2 = (starOffset + Offset(x1, y1)),
            p3 = (starOffset + Offset(x2, y2)),
        )
    }
}
