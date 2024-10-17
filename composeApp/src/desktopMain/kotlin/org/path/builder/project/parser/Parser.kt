package org.path.builder.project.parser

import org.path.builder.project.path.SVGPathSeg
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object Parser {

    private val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    private val docBuilder = factory.newDocumentBuilder()

//    val attributesData = listOf<Pair<String, String>>()

    var isPath = false
    var pathData = ""
    val pathSegment = listOf<SVGPathSeg>()
    private fun parseElement(node: Node, indent: String = "") {
        // Ensure we're dealing with an element node
        if (node.nodeType == Node.ELEMENT_NODE) {
            val element = node as Element
            println("${indent}Element: ${element.tagName}")
            isPath = element.tagName == "path"


            // Print all attributes for this element
            val attributes = element.attributes
            for (i in 0 until attributes.length) {
                val attr = attributes.item(i)
                println("$indent  Attribute: ${attr.nodeName} = ${attr.nodeValue}")


                if (attr.nodeName == "d") {
                    pathData = attr.nodeValue
                    val operationsString = splitPathData(pathData)

                }
            }

            // Recursively handle child nodes
            val childNodes = element.childNodes
            for (i in 0 until childNodes.length) {
                val child = childNodes.item(i)
                if (child.nodeType == Node.ELEMENT_NODE) {
                    parseElement(child, "$indent  ") // Increase indent for child elements
                } else if (child.nodeType == Node.TEXT_NODE && child.textContent.trim().isNotEmpty()) {
                    // Handle and print any text content inside elements
                    println("$indent  Text content: ${child.textContent.trim()}")
                }
            }
        }
    }

    fun splitPathData(pathData: String): List<String> {
        val operationsString = mutableListOf<String>()
        pathData.forEachIndexed { index, s ->
            if (s.isLetter()) {
                when (s) {
                    'C' -> with(pathData.substring(index + 1)) {
                        val nextOperator = firstOrNull {
                            it.isLetter()
                        }
                        val splitIndex = if (nextOperator != null) indexOf(nextOperator)
                        else length - 1
                        substring(0, splitIndex + 1)
                    }

                    'L' -> with(pathData.substring(index + 1)) {
                        val nextOperator = firstOrNull {
                            it.isLetter()
                        }
                        val splitIndex = if (nextOperator != null) indexOf(nextOperator)
                        else length - 1
                        substring(0, splitIndex + 1)
                    }

                    'M' -> with(pathData.substring(index + 1)) {
                        val nextOperator = firstOrNull {
                            it.isLetter()
                        }
                        val splitIndex = if (nextOperator != null) indexOf(nextOperator)
                        else length - 1
                        substring(0, splitIndex + 1)
                    }

                    else -> ""
                }.apply {
                    if (isNotEmpty()) operationsString.add(this)
                }
            }
        }
        return operationsString
    }


    suspend fun parseSvgFile(file: File) {
        val xmlDoc: Document = docBuilder.parse(file)

        // Normalize the XML structure
        xmlDoc.documentElement.normalize()

        // Get the root <svg> element
        val svgRoot = xmlDoc.documentElement
        println("Root element: ${svgRoot.nodeName}") // should print "svg"\
        parseElement(svgRoot)
    }


}