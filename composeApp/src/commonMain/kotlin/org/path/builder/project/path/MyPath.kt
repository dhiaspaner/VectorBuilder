package org.path.builder.project.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import org.path.builder.project.operations.Operation

class MyPath {

    val path = Path()

    var currentPosition: Offset = Offset.Unspecified

    var pathData: String = ""

    val operations = mutableListOf<Operation>()




}