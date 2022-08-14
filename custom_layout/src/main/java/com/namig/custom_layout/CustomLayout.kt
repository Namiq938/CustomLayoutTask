package com.namig.custom_layout

import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.RemoteViews.RemoteView
import androidx.core.view.setPadding

/**
 * This Layout is the custom layout to Linearlayout.
 * @author Namig Gadirov
 */

@RemoteView
class CustomLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private var mWeightSum = -1f
    private var mBorderColor: Int? = null
    private var mBorderWidth: Int? = null
    private var mCornerRadius: Float? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomLayout, defStyleAttr, 0).apply {
            mWeightSum = getFloat(R.styleable.CustomLayout_android_weightSum, mWeightSum)
            if (hasValue(R.styleable.CustomLayout_childBorderColor))
                mBorderColor = getColor(R.styleable.CustomLayout_childBorderColor, 0)
            if (hasValue(R.styleable.CustomLayout_childBorderWidth))
                mBorderWidth = getDimensionPixelSize(R.styleable.CustomLayout_childBorderWidth, 0)
            if (hasValue(R.styleable.CustomLayout_childCornerRadius))
                mCornerRadius = getDimensionPixelSize(R.styleable.CustomLayout_childCornerRadius, 0).toFloat()
            recycle()
        }
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    /**
     * This overridden method is responsible to lay out all child views to main layout
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        calculateWeightSum()
        renderLayout()
    }

    /**
     * Process All view renders
     */
    private fun renderLayout() {
        val count = childCount
        var curWidth: Int
        var curHeight: Int
        var curLeft: Int
        val curTop: Int
        //get the available size of child view
        val childLeft = this.paddingLeft
        val childTop = this.paddingTop
        val childRight = this.measuredWidth - this.paddingRight
        val childBottom = this.measuredHeight - this.paddingBottom
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop
        curLeft = childLeft
        curTop = childTop
        val maxHeight = getMaxHeight(childHeight)
        val calculatedWidth = calculatePredefinedWidthSum()
        for (i in 0 until count) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            if (child.visibility == GONE) return
            val viewHeight = getViewHeight(maxHeight, lp)
            //set Margins
            val marginStart = lp.marginStart
            val marginEnd = lp.marginEnd
            //measure view
            child.measure(MeasureSpec.makeMeasureSpec(childWidth + marginStart, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.AT_MOST))
            curWidth = if (lp.layoutWeight > 0 && (lp.width == WRAP_CONTENT || lp.width == MATCH_PARENT)) {
                (((childWidth - calculatedWidth) / mWeightSum) * lp.layoutWeight).toInt() + child.paddingRight + child.paddingLeft
            } else {
                getChildWidth(child)
            }
            //do the layout
            child.layout(curLeft + marginStart, curTop, curLeft + curWidth + marginStart + marginEnd, viewHeight)
            curLeft += curWidth + marginStart + marginEnd
            checkAndAddBorderToView(child, lp.mBackGroundColor)
            if (child is ImageView)
                setImageCorners(child)
        }
    }

    /**
     * Checks how much space this child should be taken
     */
    private fun getChildWidth(child: View): Int {
        return if (child.layoutParams.width == WRAP_CONTENT || child.layoutParams.width == MATCH_PARENT)
            child.measuredWidth
        else
            child.layoutParams.width
    }

    /**
     * Checks how much height this layout should be taken
     */
    private fun getViewHeight(maxHeight: Int, layoutParams: LayoutParams): Int {
        return if (layoutParams.height == MATCH_PARENT || layoutParams.height == WRAP_CONTENT)
            maxHeight
        else layoutParams.height
    }

    /**
     * Calculates all view weights that have set Wrap contents and has layoutWeight
     */
    private fun calculateWeightSum() {
        if (mWeightSum < 0) {
            var summedWeight = 0f
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val lp = child.layoutParams as LayoutParams
                if (lp.layoutWeight > 0 && (lp.width == WRAP_CONTENT || lp.width == MATCH_PARENT))
                    summedWeight += lp.layoutWeight
            }
            mWeightSum = summedWeight
        }
    }

    /**
     * Calculates total view counts that have exact width to determine
     * layout weight calculation
     */
    private fun calculatePredefinedWidthSum(): Float {
        var summedWidth = 0f
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            if (lp.layoutWeight < 0 || (lp.width != WRAP_CONTENT || lp.width != MATCH_PARENT))
                summedWidth += getChildWidth(child)
        }
        return summedWidth
    }

    /**
     * Calculates max view height to measure total view heights
     */
    private fun getMaxHeight(parentMaxHeight: Int): Int {
        var maxHeight = 0
        if (layoutParams.height == MATCH_PARENT)
            return parentMaxHeight
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            val height = lp.height
            if (lp.height == MATCH_PARENT)
                return parentMaxHeight
            else if (lp.height == WRAP_CONTENT && child.measuredHeight >= maxHeight)
                maxHeight = child.measuredHeight
            if (height >= maxHeight)
                maxHeight = height
        }
        return maxHeight
    }


    /**
     * If child view is ImageVew than set corners programmatically with outlines
     */
    private fun setImageCorners(child: ImageView) {
        mCornerRadius?.let { corner ->
            child.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, child.width, child.height, corner)
                }
            }
            child.clipToOutline = true
        }
    }

    /**
     * Adds view border and corner values to child view
     */
    private fun checkAndAddBorderToView(child: View?, backgroundColor: Int?) {
        val border = GradientDrawable()
        backgroundColor?.let { border.setColor(backgroundColor) }
        mBorderColor?.let { border.setStroke(mBorderWidth ?: 0, it) }
        mCornerRadius?.let { border.cornerRadius = it }
        child?.background = border
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    /**
     * This Layout Params is the param checks for all child views,
     * Extends from [MarginLayoutParams] that can get default widht and margin parameters
     */
    class LayoutParams : MarginLayoutParams {

        var layoutWeight = 1f
        var mBackGroundColor: Int? = null

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            val a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayout)
            layoutWeight = a.getFloat(R.styleable.CustomLayout_android_layout_weight, layoutWeight)
            if (a.hasValue(R.styleable.CustomLayout_android_background))
                mBackGroundColor = a.getColor(R.styleable.CustomLayout_android_background, 0)
            a.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: ViewGroup.LayoutParams?) : super(source) {}

    }
}