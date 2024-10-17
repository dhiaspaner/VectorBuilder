package org.path.builder.project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.path.builder.project.bezierCurve.QuadraticBezierCurveAnimation

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PathBuilder",
    ) {
        val mainViewModel = remember { MainViewModel() }
        LaunchedEffect(Unit) {
//            Parser.parseSvgFile(File("/Users/takiacademy/Downloads/Vector 6.svg"))
            mainViewModel.load()
        }

        val quadraticBezier = remember { QuadraticBezierCurveAnimation() }

        val scope = rememberCoroutineScope()

        Box(
            Modifier
                .fillMaxSize()
                .onSizeChanged {
                    mainViewModel.screenWidth = it.width.toFloat()
                    mainViewModel.screenHeight = it.height.toFloat()
                    println("mainViewModel size is ${mainViewModel.screenWidth} x ${mainViewModel.screenHeight}")
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .pointerInput(Unit) {
//                    detectTapGestures {
//                        scope.launch {
//                            quadraticBezier.onTap(it)
//                        }
//                    }
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    PointerEventType.Enter -> {}
                                    PointerEventType.Exit -> {
//                                    penTool.onDragStop()
                                    }

                                    PointerEventType.Press -> {
                                        event.changes.firstOrNull()?.position?.let {
                                            mainViewModel.penTool.onDragStart(it)
                                        }
                                    }

                                    PointerEventType.Release -> {
                                        mainViewModel.penTool.onDragStop()
                                    }

                                    PointerEventType.Move -> {
                                        event.changes.firstOrNull()?.position?.let {
                                            mainViewModel.penTool.onDrag(it)
                                            mainViewModel.penTool.onCursorMove(it)
                                        }
                                    }
                                }
                            }
                        }
                    }
            ) {
//            quadraticBezier.draw(this)
//            quadraticBezier.drawAdjustCubicBezierCurve(this)
                mainViewModel.penTool.draw(drawScope = this)
            }
        }

        Button(
            onClick = {
                mainViewModel.saveSvg()
            }
        ) {
            Text("Save")
        }
    }
}