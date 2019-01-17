package com.qihoo.security.permissionManager.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
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
 * @data on 2018/7/3 10:30
 * @desc 模拟滑动开关动画
 */

class SwitchAnimView : View {
    val DEBUG = BuildConfig.DEBUG
    val TAG = "SwitchAnimView"

    private var mPaint: Paint? = null

    private var paintBgColor: Int = Color.GRAY
    private var paintSwitchColor: Int = Color.GRAY

    /**
     * 开关背景条
     */
    private lateinit var mBgRect: RectF
    private var mDefaultBgWidth = 0f
    private var mDefaultBgHeight = 0f
    private var mDefaultBgRadius = 0f
    private var mDefaultSwitchRadius = 0f
    private var mPointX = 0f
    private var mPointY = 0f
    private var mTransX = 0f
    private var mBgColors: IntArray? = null
    private var mSwitchColors: IntArray? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        paintBgColor = Color.GRAY
        paintSwitchColor = resources.getColor(R.color.gray_eb)
        mBgColors = intArrayOf(paintBgColor, resources.getColor(R.color.permission_guide_switch_trans_50))
        mSwitchColors = intArrayOf(paintSwitchColor, resources.getColor(R.color.permission_guide_switch))
        mPaint?.apply {
            color = paintBgColor
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mBgRect = RectF()
        mTranXAnim = ObjectAnimator.ofFloat(this, mTransProperty, 0f, 0f)?.apply {
            duration = 500
            interpolator = DecelerateInterpolator()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mDefaultSwitchRadius = (measuredHeight - paddingTop - paddingBottom) / 2f
        mDefaultBgHeight = (measuredHeight - paddingTop - paddingBottom) / 1.5f
        mDefaultBgWidth = measuredWidth - paddingLeft - paddingRight - mDefaultSwitchRadius * 2
        mDefaultBgRadius = mDefaultBgHeight / 2
        if (DEBUG) {
            Log.d(TAG, "mDefaultSwitchRadius=$mDefaultSwitchRadius mDefaultBgHeight=$mDefaultBgHeight mDefaultBgWidth=$mDefaultBgWidth mDefaultBgRadius=$mDefaultBgRadius");
        }
        mBgRect.apply {
            this.left = mDefaultSwitchRadius + paddingLeft
            this.top = (mDefaultSwitchRadius - mDefaultBgHeight / 2) + paddingTop
            this.right = mDefaultBgWidth + this.left
            this.bottom = mDefaultBgHeight + this.top

        }
        mTranXAnim?.apply {
            setFloatValues(0f, mDefaultBgWidth)
            addUpdateListener { animation ->
                val currentValue = animation.animatedValue as Float

                var currentPresent = currentValue / mDefaultBgWidth
                if (DEBUG) {
                    Log.d(TAG, "currentValue=$currentValue  currentPersent=$currentPresent")
                }
                paintBgColor = ColorUtils.getCurrentColor(currentPresent, mBgColors!![0], mBgColors!![1])
                paintSwitchColor = ColorUtils.getCurrentColor(currentPresent, mSwitchColors!![0], mSwitchColors!![1])
                postInvalidate()

            }
        }
        mPointX = mDefaultSwitchRadius + paddingLeft
        mPointY = mDefaultSwitchRadius + paddingTop
        super.onLayout(changed, left, top, right, bottom)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.let {
            mPaint?.color = paintBgColor
            mPaint?.alpha = 125
            it.drawRoundRect(mBgRect, mDefaultBgRadius, mDefaultBgRadius, mPaint)
            if (DEBUG) {
                Log.d(TAG, "mBgRect :top=${mBgRect.top} left=${mBgRect.left} right=${mBgRect.right} bottom=${mBgRect.bottom} mTransX=$mTransX")
            }
            mPaint?.color = paintSwitchColor
            it.drawCircle(mPointX + mTransX, mPointY, mDefaultSwitchRadius, mPaint)
        }
        canvas?.restore()
        super.onDraw(canvas)

    }


    private var mTranXAnim: ObjectAnimator? = null

    fun startSlideAnim(listener: Animator.AnimatorListener) {
        if (mTranXAnim == null) {
            mTranXAnim = ObjectAnimator.ofFloat(this, mTransProperty, 0f, mDefaultBgWidth)?.apply {
                duration = 500
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    val currentValue = animation.animatedValue as Float

                    var currentPresent = currentValue / mDefaultBgWidth
                    if (DEBUG) {
                        Log.d(TAG, "currentValue=$currentValue  currentPersent=$currentPresent")
                    }
                    paintBgColor = ColorUtils.getCurrentColor(currentPresent, mBgColors!![0], mBgColors!![1])
                    paintSwitchColor = ColorUtils.getCurrentColor(currentPresent, mSwitchColors!![0], mSwitchColors!![1])
                    postInvalidate()

                }
            }
        }
        if (mTranXAnim?.isRunning == true) {
            mTranXAnim?.cancel()
        }
        mTranXAnim?.addListener(listener)
        mTranXAnim?.start()

    }

    fun clearAnim() {
        mTranXAnim?.cancel()
        mTranXAnim = null
    }

    fun revertOriginState() {
        init()
        mTransX = 0f
        mTranXAnim = null
        postInvalidate()
    }

    private val mTransProperty: Property<SwitchAnimView, Float> = object : Property<SwitchAnimView, Float>(Float::class.java, "mTransX") {
        override fun get(view: SwitchAnimView?): Float {
            return view?.mTransX ?: 0f
        }

        override fun set(view: SwitchAnimView?, value: Float?) {
            view?.mTransX = value ?: 0f
            postInvalidate()
        }
    }
}
