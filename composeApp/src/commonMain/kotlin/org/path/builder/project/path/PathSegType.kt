package org.path.builder.project.path

sealed interface PathSegType {

    companion object {
        fun fromData(data: String): PathSegType? = when {
            data.startsWith("M") -> PathSegMoveToAbs
            data.startsWith("m") -> PathSegMoveToRel
            data.startsWith("L") -> PathSegLineToAbs
            data.startsWith("l") -> PathSegLineToRel
            data.startsWith("C") -> PathSegCurveToCubicAbs
            data.startsWith("c") -> PathSegCurveToCubicRel
            data.startsWith("Q") -> PathSegCurveToQuadraticAbs
            data.startsWith("q") -> PathSegCurveToQuadraticRel
            data.startsWith("A") -> PathSegArcAbs
            data.startsWith("a") -> PathSegArcRel
            data.startsWith("H") -> PathSegLineToHorizontalAbs
            data.startsWith("h") -> PathSegLineToHorizontalRel
            data.startsWith("V") -> PathSegLineToVerticalAbs
            data.startsWith("v") -> PathSegLineToVerticalRel
            data.startsWith("S") -> PathSegCurveToCubicSmoothAbs
            data.startsWith("s") -> PathSegCurveToCubicSmoothRel
            data.startsWith("T") -> PathSegCurveToQuadraticSmoothAbs
            data.startsWith("t") -> PathSegCurveToQuadraticSmoothRel
            data.startsWith("Z") || data.startsWith("z") -> PathSegClosePath
            else -> null // Return null for unrecognized data
        }
    }


    data object PathSegUnknown : PathSegType
    data object PathSegClosePath : PathSegType
    data object PathSegMoveToAbs : PathSegType
    data object PathSegMoveToRel : PathSegType
    data object PathSegLineToAbs : PathSegType
    data object PathSegLineToRel : PathSegType
    data object PathSegCurveToCubicAbs : PathSegType
    data object PathSegCurveToCubicRel : PathSegType
    data object PathSegCurveToQuadraticAbs : PathSegType
    data object PathSegCurveToQuadraticRel : PathSegType
    data object PathSegArcAbs : PathSegType
    data object PathSegArcRel : PathSegType
    data object PathSegLineToHorizontalAbs : PathSegType
    data object PathSegLineToHorizontalRel : PathSegType
    data object PathSegLineToVerticalAbs : PathSegType
    data object PathSegLineToVerticalRel : PathSegType
    data object PathSegCurveToCubicSmoothAbs : PathSegType
    data object PathSegCurveToCubicSmoothRel : PathSegType
    data object PathSegCurveToQuadraticSmoothAbs : PathSegType
    data object PathSegCurveToQuadraticSmoothRel : PathSegType
}
