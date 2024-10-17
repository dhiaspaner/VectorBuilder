package org.path.builder.project.operations

import androidx.compose.ui.geometry.Offset

interface Selectable {

    var isSelected: Boolean

    fun onSelect(offset: Offset)

}