package org.path.builder.project

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke


fun DrawScope.drawControlPoint(offset: Offset, color: Color = Color(0xFF40B5F7)) {
    if (offset.isUnspecified) return
    drawCircle(
        color = Color.White,
        radius = 10f,
        center = offset,
    )
    drawCircle(
        color = color,
        radius = 10f,
        center = offset,
        style = Stroke(width = 2f),
    )

    drawCircle(
        color = color,
        radius = 5f,
        center = offset,
    )
}

