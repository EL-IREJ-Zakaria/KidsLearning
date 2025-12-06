package com.example.kidslearning.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Vue personnalisée pour tracer les lettres avec le doigt
 */
class TracingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val path = Path()
    private val paint = Paint().apply {
        color = Color.parseColor("#FF6B6B")
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    
    private var onDrawListener: (() -> Unit)? = null
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                onDrawListener?.invoke()
            }
            MotionEvent.ACTION_UP -> {
                // Optionnel: détecter la fin du tracé
            }
        }
        
        invalidate()
        return true
    }
    
    /**
     * Efface le tracé
     */
    fun clear() {
        path.reset()
        invalidate()
    }
    
    /**
     * Définit un listener appelé à chaque tracé
     */
    fun setOnDrawListener(listener: () -> Unit) {
        onDrawListener = listener
    }
}
