package com.example.kidslearning.ui.tracing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TracingCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var letterPath: Path = Path()
    private var userPath: Path = Path()
    private var currentPaint: Paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 16f
        isAntiAlias = true
    }
    private var letterPaint: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
        isAntiAlias = true
        alpha = 100 // Semi-transparent
    }

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    fun setLetterTracingPath(pathData: String?) {
        letterPath.reset()
        pathData?.let {
            // Basic parsing for SVG path data (M, L, Q commands)
            // This is a simplified parser and might not handle all complex SVG paths
            val commands = it.split(' ')
            var currentX = 0f
            var currentY = 0f
            for (i in commands.indices) {
                val command = commands[i]
                when (command[0]) {
                    'M' -> {
                        val coords = command.substring(1).split(',').map { c -> c.toFloat() }
                        if (coords.size == 2) {
                            letterPath.moveTo(coords[0], coords[1])
                            currentX = coords[0]
                            currentY = coords[1]
                        }
                    }
                    'L' -> {
                        val coords = command.substring(1).split(',').map { c -> c.toFloat() }
                        if (coords.size == 2) {
                            letterPath.lineTo(coords[0], coords[1])
                            currentX = coords[0]
                            currentY = coords[1]
                        }
                    }
                    'Q' -> {
                        if (i + 1 < commands.size) {
                            val controlCoords = command.substring(1).split(',').map { c -> c.toFloat() }
                            val endCoords = commands[i+1].split(',').map { c -> c.toFloat() }
                            if (controlCoords.size == 2 && endCoords.size == 2) {
                                letterPath.quadTo(controlCoords[0], controlCoords[1], endCoords[0], endCoords[1])
                                currentX = endCoords[0]
                                currentY = endCoords[1]
                            }
                        }
                    }
                }
            }
            invalidate()
        }
    }

    fun clearDrawing() {
        userPath.reset()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(letterPath, letterPaint)
        canvas.drawPath(userPath, currentPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {
        userPath.moveTo(motionTouchEventX, motionTouchEventY)
    }

    private fun touchMove() {
        userPath.lineTo(motionTouchEventX, motionTouchEventY)
        invalidate()
    }

    private fun touchUp() {
        // You can add logic here to check if tracing is complete/correct
    }
}
