package org.path.builder.project

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.path.builder.project.parser.Parser
import org.path.builder.project.penTool.PenTool
import java.io.File

class MainViewModel {

    val penTool = PenTool()

    var screenHeight: Float = 0f
    var screenWidth: Float = 0f

    fun generateSvgFromPath(): String {
        val pathData = penTool.pathOperationList.joinToString(" ") { it.toPathData() }
        return """
        <svg xmlns="http://www.w3.org/2000/svg" 
             width="$screenHeight" 
             height="$screenHeight" 
             viewBox="0 0 $screenHeight $screenHeight">
            <path d="$pathData" fill="none" stroke="black" stroke-width="2"/>
        </svg>
    """.trimIndent()
    }

    fun saveSvg() {
        val file = File("/Users/takiacademy/Downloads/MySvgTest.svg")
        val data = generateSvgFromPath()
        file.writeText(data)
    }

    val scope = CoroutineScope(Dispatchers.IO)

    fun load() {
        val file = File("/Users/takiacademy/Downloads/MySvgTest.svg")
        scope.launch {
            Parser.parseSvgFile(file)
        }
    }
}