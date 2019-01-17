package com.qihoo.security.permissionManager.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.Property
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.magic.module.permission.demo.BuildConfig
import com.magic.module.permission.demo.R
import com.magic.module.permission.demo.suggest.ColorUtils


/**
 * @author zhangyanjiao
 * @data on 2018/7/2 10:09
 * @desc RippleAnimView.kt
 */

class RippleAnimView : View {

    val DEBUG = BuildConfig.DEBUG
    val TAG = "RippleAnimView"
    private var backgroundPaint: Paint? = null
    private var paintX: Float = 0.toFloat()
    var paintY: Float = 0.toFloat()
    var radius: Float = 0.toFloat()
    var paintColor: Int = Color.GREEN
    var mBgColors: IntArray? = null
    //    val mWidth = UIUtils.dip2px(5f).toFloat()
    val mWidth = 50f
    private var mBorderColor: Int = Color.BLUE


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    private lateinit var rect1: Rect

    private lateinit var mBgRectStoken: Rect

    private lateinit var mBgRect: Rect

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init()
    }

    private var radiusAnimator: ObjectAnimator? = null

    private fun init() {
        backgroundPaint = Paint()
        backgroundPaint?.apply {
            color = paintColor
            isAntiAlias = true
            style = Paint.Style.FILL
            alpha = 50
        }
        radiusAnimator = ObjectAnimator.ofFloat(this, mRadiusProperty, 0f, 0f)?.apply {
            interpolator = DecelerateInterpolator()
        }
        alpnaAnim = ObjectAnimator.ofInt(this, mAlphaType, 0, 255).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
        }
        paintColor = context.resources.getColor(R.color.white)
        mBgColors = intArrayOf(
                context.resources.getColor(R.color.white),
                context.resources.getColor(R.color.blue))
        mBorderColor = context.resources.getColor(R.color.permission_guide_blue)
        mBgRectStoken = Rect()
        mBgRect = Rect()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mBgRectStoken.apply {
            this.left = 0
            this.right = measuredWidth
            this.top = 0
            this.bottom = measuredHeight
        }
        mBgRect.apply {
            this.left = mWidth.toInt() / 2
            this.right = (measuredWidth - mWidth).toInt()
            this.top = mWidth.toInt() / 2
            this.bottom = (measuredHeight - mWidth).toInt()
        }
        if (DEBUG) {
            Log.d(TAG, "left=$left  top=$top right=$right bottom=$bottom")
        }
        super.onLayout(changed, left, top, right, bottom)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.save()
        backgroundPaint?.color = paintColor
        backgroundPaint?.alpha = 125
        canvas.drawCircle(paintX, paintY, radius, backgroundPaint)

        backgroundPaint?.apply {
            color = mBorderColor
            style = Paint.Style.STROKE
            strokeWidth = mWidth
            alpha = currentAlpha
        }
        if (DEBUG) {
            Log.d(TAG, "currentAlpha=$currentAlpha")
        }
        canvas.drawRect(mBgRectStoken, backgroundPaint)
        backgroundPaint?.apply {
            color = Color.TRANSPARENT
            style = Paint.Style.FILL
        }
        canvas.drawRect(mBgRect, backgroundPaint)
        canvas.restore()
        super.onDraw(canvas)
    }

    private var currentAlpha: Int = 0

    private var alpnaAnim: ObjectAnimator? = null

    fun startAlpha(listener: Animator.AnimatorListener?) {
        if (alpnaAnim?.isRunning == true) {
            alpnaAnim?.cancel()
        }
        listener?.let {
            alpnaAnim?.addListener(listener)
        }
        alpnaAnim?.start()
    }


    fun clearAnim() {
        if (alpnaAnim?.isRunning == true) {
            alpnaAnim?.cancel()
        }
        alpnaAnim = null

        if (radiusAnimator?.isRunning == true) {
            radiusAnimator?.cancel()
        }
        radiusAnimator = null

    }

    fun revertOriginState() {
        clearAnim()
        paintColor = context.resources.getColor(R.color.white)
        radius = 0f
        currentAlpha = 0
        backgroundPaint = null
        init()
        postInvalidate()
    }


    private var startRadius: Float = 0f

    private var endRadius: Float = 0f
    fun startClickAnimator(x: Float, y: Float, listener: Animator.AnimatorListener?) {
        if (radiusAnimator?.isRunning == true) {
            radiusAnimator?.cancel()
        }
        paintX = x
        paintY = y
        //计算半径变化区域
        val end: Int = if (height < width) {
            width
        } else {
            height
        }

        startRadius = 0f
        endRadius = (if (end / 2 > paintX) end - paintX else paintX) * 0.4f

        if (DEBUG) {
            Log.d(TAG, "startRadius=$startRadius  endRadius=$endRadius")
        }
        radiusAnimator?.apply {
            setFloatValues(startRadius, endRadius)
            // 设置时间
            duration = 500
            if (listener != null) {
                addListener(listener)
            }
            //先快后慢
            start()
        }
        radiusAnimator?.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Float

            var currentPresent = 1 - (currentValue / endRadius)
            if (DEBUG) {
                Log.d(TAG, "currentValue=$currentValue  currentPersent=$currentPresent")
            }
            paintColor = ColorUtils.getCurrentColor(currentPresent, mBgColors!![0], mBgColors!![1])
            if (DEBUG) {
                Log.d(TAG, "paintColor=$paintColor")
            }
            postInvalidate()

        }
    }

    private val mRadiusProperty: Property<RippleAnimView, Float> = object : Property<RippleAnimView, Float>(Float::class.java, "radius") {
        override fun get(view: RippleAnimView?): Float {
            return view?.radius ?: 0f
        }

        override fun set(view: RippleAnimView?, value: Float?) {
            view?.radius = value ?: 0f
        }
    }
    private val mAlphaType: Property<RippleAnimView, Int> = object : Property<RippleAnimView, Int>(Int::class.java, "currentAlpha") {
        override fun get(view: RippleAnimView?): Int {
            return view?.currentAlpha ?: 0
        }

        override fun set(view: RippleAnimView?, value: Int?) {
            view?.currentAlpha = value ?: 0
            postInvalidate()
        }
    }

}
