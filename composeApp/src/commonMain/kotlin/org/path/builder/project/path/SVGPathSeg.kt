package org.path.builder.project.path

import org.path.builder.project.operations.CubicBezierPathOperation
import org.path.builder.project.operations.PathOperation

interface SVGPathSeg {
    // Path Segment Types
    companion object {

        fun fromPathDataToOperation(segData: String): SVGPathSeg? {
            val type = PathSegType.fromData(segData)
            return when (type) {
//                is SVGPathSegMovetoAbs -> SVGPathSegMovetoAbs.from(segData = segData)
//                is PathSegType.PathSegLineToAbs -> SVGPathSegLinetoAbs.from(segData = segData)
//                is CubicBezierPathOperation -> SVGPathSegCurvetoCubicAbs.from(segData = segData)
                else -> null
            }

        }

    }


    val pathSegType: PathSegType
    val pathSegTypeAsLetter: String

    val pathData: String


    fun toOperation() : PathOperation

}
