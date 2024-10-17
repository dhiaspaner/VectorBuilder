package org.path.builder.project.path

import androidx.compose.ui.geometry.Offset
import org.path.builder.project.operations.CubicBezierPathOperation
import org.path.builder.project.operations.PathOperation

class SVGPathSegCurvetoCubicAbs(
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

    private val starOffset = Offset(startPointX, startPointY)

    override val pathSegType: PathSegType = PathSegType.PathSegCurveToCubicAbs

    override fun toOperation(): PathOperation = CubicBezierPathOperation(
        isAbstract = true,
        p0 = starOffset,
        controlPoint1 = Offset(x, y),
        controlPoint2 = Offset(x1, y1),
        p3 = Offset(x2, y2),
    )


}
