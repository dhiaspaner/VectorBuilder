package org.path.builder.project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.path.builder.project.bezierCurve.QuadraticBezierCurveAnimation
import org.path.builder.project.penTool.PenTool


@Composable
@Preview
fun App() {
    MaterialTheme {


        val penTool = remember { PenTool() }

        val quadraticBezier = remember { QuadraticBezierCurveAnimation() }


        val scope = rememberCoroutineScope()


        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Enter -> {}
                                PointerEventType.Exit -> {}
                                PointerEventType.Press -> {
                                    event.changes.firstOrNull()?.position?.let {
                                        penTool.onDragStart(it)
                                    }
                                }
                                PointerEventType.Release -> {
                                    penTool.onDragStop()
                                }
                                PointerEventType.Move -> {
                                    event.changes.firstOrNull()?.position?.let {
                                        penTool.onDrag(it)
                                        penTool.onCursorMove(it)
                                    }
                                }
                            }
                        }
                    }
//                    detectTapGestures {
//                        scope.launch {
//                            quadraticBezier.onTap(it)
//                        }
//                    }
//                    if (penTool.isOnTapGesture)
//                        detectTapGestures() {
//                            penTool.onClick(offset = it)
//                        }
//                    else
//                        detectDragGestures(
//                            onDragStart = { offset ->
//                                penTool.onDragStart(offset = offset)
//                            },
//                            onDrag = { _, offset ->
//                                penTool.onDrag(offset = offset)
//                            },
//                            onDragEnd = {
//                                penTool.dragEnd()
//                            },
//                            onDragCancel = {
//                                penTool.lastOperation = null
//                            }
//                        )
                }
//                .onCursorMove { offset ->
//                    penTool.onCursorMove(offset)
//                }


        ) {
            penTool.draw(drawScope = this)
//            quadraticBezier.draw(this)
        }
    }
}