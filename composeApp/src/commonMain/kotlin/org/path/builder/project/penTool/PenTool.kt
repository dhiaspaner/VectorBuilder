package org.path.builder.project.penTool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.path.builder.project.operations.CubicBezierPathOperation
import org.path.builder.project.operations.LinePathOperation
import org.path.builder.project.operations.MoveToPathOperation
import org.path.builder.project.operations.PathOperation

class PenTool {

    var lastPathOperation: PathOperation? = null

    var dragAmount: Offset = Offset.Zero

    var dragStartOffset = Offset.Unspecified

    var lastRelativePoint = Offset.Unspecified
    val path = Path()


    val pathOperationList = mutableListOf<PathOperation>()

    fun addOperation(pathOperation: PathOperation) {
        pathOperation.applyToPath(path)
        pathOperationList.add(pathOperation)
        pathOperation.isStored = true
        lastPathOperation = pathOperation
    }


    var drawIncrement by mutableIntStateOf(0)
    val controlPointList = mutableListOf<Offset>()


    var isStartToDrag by mutableStateOf(false)
    var isClick by mutableStateOf(false)
    var isDragging by mutableStateOf(false)
    var cursorOffset: Offset = Offset.Unspecified


    fun onClick(offset: Offset) {
        lastPathOperation.let { operation ->
            if (operation == null) {
                val moveOperation = MoveToPathOperation(
                    p = offset,
                    isAbstract = true
                )
                lastRelativePoint = moveOperation.p.copy()
                addOperation(moveOperation)
            } else {
                when (operation) {
                    is MoveToPathOperation -> {
                        val lineOperation = LinePathOperation(
                            p0 = lastRelativePoint.copy(),
                            p1 = offset,
                            isAbstract = false
                        )
                        lastRelativePoint = lineOperation.p1
                        addOperation(lineOperation)
                    }

                    is LinePathOperation -> {
                        val newLine = LinePathOperation(
                            p0 = lastRelativePoint.copy(),
                            p1 = offset,
                            isAbstract = false
                        )
                        addOperation(newLine)
                        lastRelativePoint = newLine.p1
                    }

                    is CubicBezierPathOperation -> {
                        if (operation.p3.isUnspecified) {
                            operation.controlPoint2 = offset
                            operation.adjustQuadraticToCubic()
                            addOperation(operation)
                        } else {
                            val newLineOperation = LinePathOperation(
                                p0 = lastRelativePoint.copy(),
                                p1 = offset,
                                isAbstract = false
                            )
                            addOperation(newLineOperation)
                        }
                        lastRelativePoint = offset

                    }
                }
            }
        }

        drawIncrement++
    }

    fun onDragStart(offset: Offset) {
        println("onDragStart")
        isStartToDrag = true
        dragStartOffset = offset
        drawIncrement++
    }


    fun onCursorMove(offset: Offset) {
        cursorOffset = offset
        println("is Dragging $isDragging")
        if (isDragging) return
        println("last operation $lastPathOperation")
        lastPathOperation?.let { operation ->
            when (operation) {
                is LinePathOperation -> {
//                    operation.p1 = offset.toPoint()
//                    lastOperation = operation
                }

                is CubicBezierPathOperation -> {

                }
            }
        }

        drawIncrement++

    }


    fun onDrag(offset: Offset) {
        if (isStartToDrag) {
            dragAmount = offset - dragStartOffset
            isClick = dragAmount.x < 1 && dragAmount.y < 1
            isDragging = isStartToDrag && !isClick
            if (isDragging) {
                println("onDrag")
                lastPathOperation.let { operation ->
                    if (operation == null) {
                        val moveOperation = MoveToPathOperation(isAbstract = true, p = dragStartOffset)
                        lastRelativePoint = moveOperation.p
                        addOperation(moveOperation)
                        val cubicOperation = CubicBezierPathOperation(isAbstract = false, p0 = lastRelativePoint.copy())
                        lastPathOperation = cubicOperation
                    }
                    if (operation is LinePathOperation && operation.isStored) {

                        val newCubicOperation = CubicBezierPathOperation(
                            p0 = dragStartOffset,
                            controlPoint1 = (dragStartOffset + dragAmount),
                            isAbstract = false
                        )
                        lastPathOperation = newCubicOperation
                    } else if (operation is CubicBezierPathOperation) {
                        if (operation.isStored)
                            lastPathOperation = CubicBezierPathOperation(
                                p0 = dragStartOffset,
                                controlPoint1 = (dragStartOffset + dragAmount),
                                isAbstract = false
                            )
                        else {
                            operation.controlPoint1 = (dragStartOffset + dragAmount)
                            lastPathOperation = operation
                        }

                    }
                }

                drawIncrement++
            }
        }
    }

    fun onDragStop() {
        isStartToDrag = false
        isDragging = false
        if (dragStartOffset.isUnspecified) return

//        println("drag amount ${dragAmount.x} ${dragAmount.y}")
        if (dragAmount.x < 5 && dragAmount.y < 5) {
            onClick(dragStartOffset)
        } else {
            lastPathOperation.let { operation ->
                val lastSavedOperation = pathOperationList.lastOrNull()
                if (operation is CubicBezierPathOperation && lastSavedOperation is LinePathOperation) {
                    operation.controlPoint2 = lastSavedOperation.p1
                    operation.adjustQuadraticToCubic()
                    operation.applyToInternalPath()
                    lastPathOperation = operation
                    addOperation(operation)
                    lastRelativePoint = operation.p0
                } else if (operation is CubicBezierPathOperation && lastSavedOperation is CubicBezierPathOperation) {
                    if (pathOperationList.size >= 2 && (
                                        pathOperationList[pathOperationList.size - 2] is MoveToPathOperation
                                )
                    ) {
                        operation.controlPoint2 = lastSavedOperation.p3
                        operation.adjustQuadraticToCubic()
                        operation.applyToInternalPath()
                        addOperation(operation)
                        lastRelativePoint = dragStartOffset
                    } else {
                        println("second cubic")
                        operation.controlPoint2 = lastSavedOperation.p0
                        operation.adjustQuadraticToCubic()
                        operation.applyToInternalPath()
                        addOperation(operation)
                        lastRelativePoint = dragStartOffset
                    }

                }
            }
        }
        dragAmount = Offset.Zero
        dragStartOffset = Offset.Unspecified
        drawIncrement++

    }




    fun draw(drawScope: DrawScope) {
        with(drawScope) {
            drawIncrement
            pathOperationList.forEach { operation -> operation.onDraw(this) }
            lastPathOperation?.onDraw(this)
            drawPath(
                path = path,
                style = Stroke(width = 5f),
                color = Color.Black,
            )
        }
    }
}