import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class AnimatedLine {

    var p1: Offset = Offset.Unspecified
    var p2: Offset = Offset.Unspecified

    var drawIncrement by mutableIntStateOf(0)


    val isLineDefined: Boolean get() = p1.isSpecified && p2.isSpecified

    // Animatable to control the animation progress from 0f to 1f
    private var animationProgress = Animatable(0f)

    // Coroutine scope to launch animations

    fun onTap(offset: Offset) {
        if (p1.isUnspecified) {
            p1 = offset
        } else if (p2.isUnspecified) {
            p2 = offset
        }
    }

    suspend fun resetPoints() {
        p1 = Offset.Unspecified
        p2 = Offset.Unspecified
        animationProgress.snapTo(0f) // Reset animation progress
        drawIncrement++

    }

    // Start the line animation once p1 and p2 are defined
    suspend fun startAnimation() {
        println("start line animation")
        drawIncrement++
        if (isLineDefined) {
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            )
        }
    }


    // Drawing the animated line based on the animation progress
    fun drawLine(drawScope: DrawScope) {
        drawIncrement
        if (isLineDefined) {
            val currentPoint = lerp(p1, p2, animationProgress.value)
            // Draw the animated line
            drawScope.drawLine(
                color = Color.Blue,
                start = p1,
                end = currentPoint,
                strokeWidth = 5f
            )
        }
    }
}
