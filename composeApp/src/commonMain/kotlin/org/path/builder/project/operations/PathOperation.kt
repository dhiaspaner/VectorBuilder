package org.path.builder.project.operations

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.path.builder.project.InteractionEvent

interface PathOperation : Operation {

    val isAbstract: Boolean

    var isStored: Boolean


    fun applyToPath(path: Path)

    fun onDraw(drawScope: DrawScope)

    fun onEvent(event: InteractionEvent)


    fun toPathData(): String



}