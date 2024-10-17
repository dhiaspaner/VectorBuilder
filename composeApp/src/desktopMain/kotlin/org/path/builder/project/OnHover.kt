package org.path.builder.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.onCursorMove(onHover: (Offset) -> Unit): Modifier =
    onPointerEvent(PointerEventType.Move) {
        it.changes.firstOrNull()?.position?.let {
            onHover(it)
        }
    }