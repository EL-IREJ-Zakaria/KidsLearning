package com.example.kidslearning.ui.lettertrace

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.kidslearning.R

/**
 * Custom view for letter tracing with finger drawing.
 * Features smooth anti-aliased strokes and path caching for performance.
 */
class TracingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val userPath = Path()
    private val userPaint = Paint().apply {
        color = context.getColor(R.color.user_path)
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private var currentX = 0f
    private var currentY = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(userPath, userPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                userPath.moveTo(x, y)
                currentX = x
                currentY = y
            }
            MotionEvent.ACTION_MOVE -> {
                userPath.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
                currentX = x
                currentY = y
            }
            MotionEvent.ACTION_UP -> {
                userPath.lineTo(currentX, currentY)
            }
        }
        invalidate()
        return true
    }

    /**
     * Resets the tracing view by clearing the user's drawn path.
     */
    fun reset() {
        userPath.reset()
        invalidate()
    }
}
