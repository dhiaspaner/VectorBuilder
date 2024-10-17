package org.path.builder.project

import androidx.compose.ui.geometry.Offset

sealed interface InteractionEvent {
    data class OnClick(val offset: Offset) : InteractionEvent
    data class OnDragStart(val offset: Offset) : InteractionEvent
    data class OnDrag(val offset: Offset,val dragAmount: Offset) : InteractionEvent
    data object OnDragStopped : InteractionEvent
}