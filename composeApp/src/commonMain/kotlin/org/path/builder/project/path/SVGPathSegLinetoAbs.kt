package org.path.builder.project.path

import androidx.compose.ui.geometry.Offset
import org.path.builder.project.operations.LinePathOperation
import org.path.builder.project.operations.PathOperation

class SVGPathSegLinetoAbs(
    val startPointX: Float,
    val startPointY: Float,
    var x: Float,
    var y: Float,
    override val pathSegTypeAsLetter: String,
    override val pathData: String
 ) : SVGPathSeg {

     companion object {
//         fun fromData(pathSeg: String): SVGPathSegLinetoAbs {
//             val floatList = pathSeg.split(" ")
//                 .drop(1) // Skip the first command letter
//                 .map { it.toFloat() }
//             SVGPathSegLinetoAbs(
////                 pathData = pathSeg,
//
//             )
//         }
     }
    override val pathSegType: PathSegType = PathSegType.PathSegLineToAbs

    override fun toOperation(): PathOperation = LinePathOperation(
        p0 = Offset(startPointX,startPointY),
        p1 = Offset(x,y),
        isAbstract = true
    )

}
