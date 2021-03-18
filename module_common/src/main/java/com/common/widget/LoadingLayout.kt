package com.common.widget

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.common.R
import com.common.utils.extension.gone
import com.common.utils.extension.setVisible
import com.common.utils.extension.visible
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/8 18:09
 */
class LoadingLayout : FrameLayout {
    private lateinit var mContext: Context
    var btn: TextView? = null
    var txt: TextView? = null
    var descTxt: TextView? = null

    constructor(context: Context) : super(context) {
        init(context, null, R.attr.styleLoadingLayout)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, R.attr.styleLoadingLayout)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    /**
     * 初始化数据
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        mContext = context
        mInflater = LayoutInflater.from(context)
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingLayout,
            defStyleAttr,
            R.style.LoadingLayout_Style
        )
        mEmptyImage =
            a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, View.NO_ID)
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText)
        mEmptyDescText = a.getString(R.styleable.LoadingLayout_llEmptyDescText)
        mErrorImage =
            a.getResourceId(R.styleable.LoadingLayout_llErrorImage, View.NO_ID)
        mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText)
        mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText)
        mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, 0x333333)
        mTextDescColor = a.getColor(R.styleable.LoadingLayout_llTextDescColor, 0x999999)
        mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, dp2px(16f))
        mTextDescSize =
            a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextDescSize, dp2px(13f))
        mButtonTextColor = a.getColor(R.styleable.LoadingLayout_llButtonTextColor, -0xcccccd)
        mButtonTextSize =
            a.getDimensionPixelSize(R.styleable.LoadingLayout_llButtonTextSize, dp2px(16f))
        mButtonBackground = a.getDrawable(R.styleable.LoadingLayout_llButtonBackground)
        mEmptyResId =
            a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout.layout_loading_empty)
        mLoadingResId =
            a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout.layout_loading)
        mErrorResId =
            a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout.layout_loading_error)
        a.recycle()
    }


    interface OnInflateListener {
        fun onInflate(inflated: View?)
    }

    fun wrap(activity: Activity): LoadingLayout? {
        return wrap(
            (activity.findViewById<View>(R.id.content) as ViewGroup).getChildAt(
                0
            )
        )
    }

    fun wrap(fragment: Fragment): LoadingLayout? {
        return wrap(fragment.view)
    }

    fun wrap(view: View?): LoadingLayout? {
        if (view == null) {
            throw RuntimeException("content view can not be null")
        }
        val parent = view.parent as ViewGroup
        if (view == null) {
            throw RuntimeException("parent view can not be null")
        }
        val lp = view.layoutParams
        val index = parent.indexOfChild(view)
        parent.removeView(view)
        val layout = LoadingLayout(view.context)
        parent.addView(layout, index, lp)
        layout.addView(view)
        layout.setContentView(view)
        return layout
    }

    var mEmptyImage = 0
    var mEmptyText: CharSequence? = null
    var mEmptyDescText: CharSequence? = null

    var mErrorImage = 0
    var mErrorText: CharSequence? = null
    var mRetryText: CharSequence? = null
    var mRetryButtonClickListener =
        OnClickListener { v ->
            if (mRetryListener != null) {
                mRetryListener!!.onClick(v)
            }
        }
    var mRetryListener: OnClickListener? = null

    var mOnEmptyInflateListener: OnInflateListener? = null
    var mOnErrorInflateListener: OnInflateListener? = null

    var mTextColor = 0
    var mTextSize: Int = 0
    var mTextDescColor: Int = 0
    var mTextDescSize: Int = 0
    var mButtonTextColor = 0
    var mButtonTextSize: Int = 0
    var mButtonBackground: Drawable? = null
    var mEmptyResId = View.NO_ID
    var mLoadingResId: Int = View.NO_ID
    var mErrorResId: Int = View.NO_ID
    var mContentId = View.NO_ID

    var mLayouts: MutableMap<Int, View?> =
        HashMap()


    fun dp2px(dp: Float): Int {
        return (resources.displayMetrics.density * dp).toInt()
    }


    var mInflater: LayoutInflater? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 0) {
            return
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        val view = getChildAt(0)
        setContentView(view)
        showLoading()
    }

    private fun setContentView(view: View) {
        mContentId = view.id
        mLayouts[mContentId] = view
    }

    fun setLoading(@LayoutRes id: Int): LoadingLayout? {
        if (mLoadingResId != id) {
            remove(mLoadingResId)
            mLoadingResId = id
        }
        return this
    }

    fun setEmpty(@LayoutRes id: Int): LoadingLayout? {
        if (mEmptyResId != id) {
            remove(mEmptyResId)
            mEmptyResId = id
        }
        return this
    }

    fun setOnEmptyInflateListener(listener: OnInflateListener): LoadingLayout? {
        mOnEmptyInflateListener = listener
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts[mEmptyResId])
        }
        return this
    }

    fun setOnErrorInflateListener(listener: OnInflateListener): LoadingLayout? {
        mOnErrorInflateListener = listener
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts[mErrorResId])
        }
        return this
    }

    fun setEmptyImage(@DrawableRes resId: Int): LoadingLayout? {
        mEmptyImage = resId
        image(mEmptyResId, R.id.empty_image, mEmptyImage)
        return this
    }

    fun setEmptyText(value: String?): LoadingLayout? {
        mEmptyText = value
        text(mEmptyResId, R.id.empty_text, mEmptyText)
        return this
    }

    fun setEmptyDescText(value: String?): LoadingLayout? {
        mEmptyDescText = value
        text(mEmptyResId, R.id.empty_desc_text, mEmptyDescText)
        return this
    }

    fun setErrorImage(@DrawableRes resId: Int): LoadingLayout? {
        mErrorImage = resId
        image(mErrorResId, R.id.error_image, mErrorImage)
        return this
    }

    fun setErrorText(value: String?): LoadingLayout? {
        mErrorText = value
        text(mErrorResId, R.id.error_text, mErrorText)
        return this
    }

    fun setRetryText(text: String): LoadingLayout? {
        mRetryText = text
        text(mErrorResId, R.id.retry_button, mRetryText)
        return this
    }

    fun setRetryListener(listener: OnClickListener?): LoadingLayout? {
        mRetryListener = listener
        return this
    }


    fun showLoading() {
        show(mLoadingResId)
    }

    fun showEmpty() {
        show(mEmptyResId)
    }

    fun showError() {
        show(mErrorResId)
    }

    fun showContent() {
        show(mContentId)
    }

    private fun show(layoutId: Int) {
        for (view in mLayouts.values) {
            view!!.gone()
        }
        layout(layoutId)!!.visible()
    }


    private fun remove(layoutId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val vg = mLayouts.remove(layoutId)
            removeView(vg)
        }
    }

    private fun layout(layoutId: Int): View? {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts[layoutId]
        }
        val layout = mInflater!!.inflate(layoutId, this, false)
        layout.gone()
        addView(layout)
        mLayouts[layoutId] = layout
        if (layoutId == mEmptyResId) {
            val img =
                layout.findViewById<View>(R.id.empty_image) as ImageView
            if (img != null) {
                img.setVisible(mEmptyImage != -1)
                if (mEmptyImage != -1) {
                    img.setImageResource(mEmptyImage)
                }
            }
            val view =
                layout.findViewById<View>(R.id.empty_text) as TextView
            if (view != null) {
                view.text = mEmptyText
                view.setTextColor(mTextColor)
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }
            val view2 =
                layout.findViewById<View>(R.id.empty_desc_text) as TextView
            if (view2 != null) {
                view2.text = mEmptyDescText
                view2.setTextColor(mTextColor)
                view2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextDescSize.toFloat())
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener!!.onInflate(layout)
            }
        } else if (layoutId == mErrorResId) {
            val img =
                layout.findViewById<View>(R.id.error_image) as ImageView
            img?.setImageResource(mErrorImage)
            txt = layout.findViewById<View>(R.id.error_text) as TextView
            if (txt != null) {
                txt!!.text = mErrorText
                txt!!.setTextColor(mTextColor)
                txt!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }
            btn = layout.findViewById<View>(R.id.retry_button) as TextView
            if (btn != null) {
                btn!!.text = mRetryText
                btn!!.setTextColor(mButtonTextColor)
                btn!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize.toFloat())
                btn!!.background = mButtonBackground
                btn!!.setOnClickListener(mRetryButtonClickListener)
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener!!.onInflate(layout)
            }
        }
        return layout
    }

    private fun text(layoutId: Int, ctrlId: Int, value: CharSequence?) {
        if (mLayouts.containsKey(layoutId)) {
            val view =
                mLayouts[layoutId]!!.findViewById<View>(ctrlId) as TextView
            if (view != null) {
                view.text = value
                view.visible()
            }
        }
    }

    private fun image(layoutId: Int, ctrlId: Int, resId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val view = mLayouts[layoutId]
                ?.findViewById<View>(ctrlId) as ImageView
            if (view != null) {
                view.setVisible(resId != -1)
                if (resId != -1) {
                    view.setImageResource(resId)
                }
            }
        }
    }
}