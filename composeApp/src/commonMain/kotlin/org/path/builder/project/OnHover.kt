package org.path.builder.project

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

expect fun Modifier.onCursorMove(onHover: (Offset) -> Unit): Modifier