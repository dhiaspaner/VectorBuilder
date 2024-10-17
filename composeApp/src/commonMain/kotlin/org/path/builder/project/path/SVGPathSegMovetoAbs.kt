package org.path.builder.project.path

import androidx.compose.ui.geometry.Offset
import org.path.builder.project.operations.MoveToPathOperation
import org.path.builder.project.operations.PathOperation

class SVGPathSegMovetoAbs(
    override val pathData: String,
    var x: Float,
    var y: Float
): SVGPathSeg {

    companion object {
        fun from(segData: String): SVGPathSegMovetoAbs {
            val offsetList = segData.substring(2).split(" ").map() { it.toFloatOrNull()  }.mapNotNull {
                0f
            }
            if (offsetList.size == 2) {
                val x = offsetList[0]
                val y = offsetList[1]
                return SVGPathSegMovetoAbs(
                    x = x,
                    y = y,
                    pathData = segData
                )
            } else throw IllegalArgumentException("Invalid path data supplied: $segData")


        }
    }

    override val pathSegType: PathSegType = PathSegType.PathSegMoveToAbs
    override val pathSegTypeAsLetter: String = "M"



    override fun toOperation(): PathOperation = MoveToPathOperation(
        p = Offset(x = x, y = y),
        isAbstract = true
    )




}
