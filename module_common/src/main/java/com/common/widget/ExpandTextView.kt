package com.common.widget

import android.content.Context
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.ObjectUtils
import com.common.R

/**
 * @desc:TextView 收起/展开View
 * @author: tanlifei
 * @date: 2021/2/22 17:01
 */
class ExpandTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = android.R.attr.textStyle
) : AppCompatTextView(context, attrs, style) {

    companion object {
        const val STATE_SHRINK = 0
        const val STATE_EXPAND = 1
        const val CLASS_NAME_VIEW = "android.view.View"
        const val CLASS_NAME_LISTENER_INFO = "android.view.View\$ListenerInfo"
        const val ELLIPSIS_HINT = "..."
        const val GAP_TO_EXPAND_HINT = " "
        const val GAP_TO_SHRINK_HINT = " "
        const val MAX_LINES_ON_SHRINK = 3
        const val TO_EXPAND_HINT_COLOR = -0xcb6725
        const val TO_SHRINK_HINT_COLOR = -0x18b3c4
        const val TO_EXPAND_HINT_COLOR_BG_PRESSED = 0x55999999
        const val TO_SHRINK_HINT_COLOR_BG_PRESSED = 0x55999999
        const val TOGGLE_ENABLE = true
        const val SHOW_TO_EXPAND_HINT = true
        const val SHOW_TO_SHRINK_HINT = true
    }


    private var mEllipsisHint: String? = null
    private var mToExpandHint: String? = null
    private var mToShrinkHint: String? = null
    private var mGapToExpandHint = GAP_TO_EXPAND_HINT
    private var mGapToShrinkHint = GAP_TO_SHRINK_HINT
    private var mToggleEnable = TOGGLE_ENABLE
    private var mShowToExpandHint = SHOW_TO_EXPAND_HINT
    private var mShowToShrinkHint = SHOW_TO_SHRINK_HINT
    private var mMaxLinesOnShrink = MAX_LINES_ON_SHRINK
    private var mToExpandHintColor = TO_EXPAND_HINT_COLOR
    private var mToShrinkHintColor = TO_SHRINK_HINT_COLOR
    private var mToExpandHintColorBgPressed = TO_EXPAND_HINT_COLOR_BG_PRESSED
    private var mToShrinkHintColorBgPressed = TO_SHRINK_HINT_COLOR_BG_PRESSED
    var mCurrState = STATE_SHRINK

    //  used to add to the tail of modified text, the "shrink" and "expand" text
    private var mTouchableSpan: TouchableSpan? = null
    private var mBufferType = BufferType.NORMAL
    private lateinit var mTextPaint: TextPaint
    private var mLayout1: Layout? = null
    private var mTextLineCount = -1
    private var mLayoutWidth = 0
    private var mFutureTextViewWidth = 0

    //  the original text of this view
    private var mOrigText: CharSequence? = null

    //  used to judge if the listener of corresponding to the onclick event of ExpandableTextView
    //  is specifically for inner toggle
    private var mExpandableClickListener: ExpandableClickListener? = null
    lateinit var mOnExpandListener: OnExpandListener

    init {
        initView(attrs)
    }


    private fun initView(attrs: AttributeSet? = null) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView)
            if (a != null) {
                val n = a.indexCount
                for (i in 0 until n) {
                    val attr = a.getIndex(i)
                    when (attr) {
                        R.styleable.ExpandTextView_etv_MaxLinesOnShrink -> {
                            mMaxLinesOnShrink = a.getInteger(attr, MAX_LINES_ON_SHRINK)
                        }
                        R.styleable.ExpandTextView_etv_EllipsisHint -> {
                            mEllipsisHint = a.getString(attr)
                        }
                        R.styleable.ExpandTextView_etv_ToExpandHint -> {
                            mToExpandHint = a.getString(attr)
                        }
                        R.styleable.ExpandTextView_etv_ToShrinkHint -> {
                            mToShrinkHint = a.getString(attr)
                        }
                        R.styleable.ExpandTextView_etv_EnableToggle -> {
                            mToggleEnable = a.getBoolean(attr, TOGGLE_ENABLE)
                        }
                        R.styleable.ExpandTextView_etv_ToExpandHintShow -> {
                            mShowToExpandHint = a.getBoolean(attr, SHOW_TO_EXPAND_HINT)
                        }
                        R.styleable.ExpandTextView_etv_ToShrinkHintShow -> {
                            mShowToShrinkHint = a.getBoolean(attr, SHOW_TO_SHRINK_HINT)
                        }
                        R.styleable.ExpandTextView_etv_ToExpandHintColor -> {
                            mToExpandHintColor = a.getInteger(attr, TO_EXPAND_HINT_COLOR)
                        }
                        R.styleable.ExpandTextView_etv_ToShrinkHintColor -> {
                            mToShrinkHintColor = a.getInteger(attr, TO_SHRINK_HINT_COLOR)
                        }
                        R.styleable.ExpandTextView_etv_ToExpandHintColorBgPressed -> {
                            mToExpandHintColorBgPressed =
                                a.getInteger(attr, TO_EXPAND_HINT_COLOR_BG_PRESSED)
                        }
                        R.styleable.ExpandTextView_etv_ToShrinkHintColorBgPressed -> {
                            mToShrinkHintColorBgPressed =
                                a.getInteger(attr, TO_SHRINK_HINT_COLOR_BG_PRESSED)
                        }
                        R.styleable.ExpandTextView_etv_InitState -> {
                            mCurrState = a.getInteger(attr, STATE_SHRINK)
                        }
                        R.styleable.ExpandTextView_etv_GapToExpandHint -> {
                            mGapToExpandHint = a.getString(attr)!!
                        }
                        R.styleable.ExpandTextView_etv_GapToShrinkHint -> {
                            mGapToShrinkHint = a.getString(attr)!!
                        }
                    }
                }
                a.recycle()
            }
        }
        mTouchableSpan = TouchableSpan()
        movementMethod = LinkTouchMovementMethod()
        if (TextUtils.isEmpty(mEllipsisHint)) {
            mEllipsisHint = ELLIPSIS_HINT
        }
        if (TextUtils.isEmpty(mToExpandHint)) {
            mToExpandHint = resources.getString(R.string.to_expand_hint)
        }
        if (TextUtils.isEmpty(mToShrinkHint)) {
            mToShrinkHint = resources.getString(R.string.to_shrink_hint)
        }
        if (mToggleEnable) {
            mExpandableClickListener = ExpandableClickListener()
            setOnClickListener(mExpandableClickListener)
        }
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val obs = viewTreeObserver
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this)
                } else {
                    obs.removeGlobalOnLayoutListener(this)
                }
                setTextInternal(getNewTextByConfig(), mBufferType)
            }
        })
    }

    /**
     * used in ListView or RecyclerView to update ExpandTextView
     *
     * @param text                original text
     * @param futureTextViewWidth the width of ExpandTextView in px unit,
     * used to get max line number of original text by given the width
     * @param expandState         expand or shrink
     */
    fun updateForRecyclerView(
        text: CharSequence?,
        futureTextViewWidth: Int,
        expandState: Int
    ) {
        mFutureTextViewWidth = futureTextViewWidth
        mCurrState = expandState
        setText(text)
    }

    fun updateForRecyclerView(
        text: CharSequence,
        type: BufferType,
        futureTextViewWidth: Int
    ) {
        mFutureTextViewWidth = futureTextViewWidth
        setText(text, type)
    }

    fun updateForRecyclerView(
        text: CharSequence?,
        futureTextViewWidth: Int
    ) {
        mFutureTextViewWidth = futureTextViewWidth
        setText(text)
    }

    /**
     * get the current state of ExpandTextView
     *
     * @return STATE_SHRINK if in shrink state
     * STATE_EXPAND if in expand state
     */
    fun getExpandState(): Int {
        return mCurrState
    }

    /**
     * refresh and get a will-be-displayed text by current configuration
     *
     * @return get a will-be-displayed text
     */
    private fun getNewTextByConfig(): CharSequence {
        if (TextUtils.isEmpty(mOrigText)) {
            return mOrigText!!
        }
        mLayout1 = layout
        if (mLayout1 != null) {
            mLayoutWidth = mLayout1!!.width
        }
        if (mLayoutWidth <= 0) {
            mLayoutWidth = if (width == 0) {
                if (mFutureTextViewWidth == 0) {
                    return mOrigText!!
                } else {
                    mFutureTextViewWidth - paddingLeft - paddingRight
                }
            } else {
                width - paddingLeft - paddingRight
            }
        }
        mTextPaint = paint
        mTextLineCount = -1
        val line = getLineCount(mOrigText!!)
        when (mCurrState) {
            STATE_SHRINK -> {
                mLayout1 = DynamicLayout(
                    mOrigText!!,
                    mTextPaint,
                    mLayoutWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0f,
                    0.0f,
                    false
                )
                mTextLineCount = mLayout1!!.lineCount + line
                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return mOrigText!!
                }
                var start = 0
                var end: Int
                var showingText = ""
                var lineTotal = 0
                if (ObjectUtils.isNotEmpty(mOrigText)) {
                    var i = 0
                    while (i < mMaxLinesOnShrink) {
                        end = getValidLayout().getLineEnd(i)
                        showingText += mOrigText.toString().substring(start, end)
                        start = end
                        i++
                    }
                }
                if (ObjectUtils.isNotEmpty(showingText)) {
                    if (showingText.contains("\n\n") || showingText.contains("\n")) {
                        lineTotal = lineTotal + 1
                    }
                }
                val indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1)
                val indexStart = getValidLayout().getLineStart(mMaxLinesOnShrink - 1)
                var indexEndTrimmed = (indexEnd
                        - getLengthOfString(mEllipsisHint)
                        - if (mShowToExpandHint) getLengthOfString(mToExpandHint) + getLengthOfString(
                    mGapToExpandHint
                ) else 0)
                if (indexEndTrimmed <= indexStart) {
                    indexEndTrimmed = indexEnd
                }
                val remainWidth = getValidLayout().width -
                        (mTextPaint.measureText(
                            mOrigText!!.subSequence(indexStart, indexEndTrimmed).toString()
                        ) + 0.5).toInt()
                val widthTailReplaced = mTextPaint.measureText(
                    getContentOfString(mEllipsisHint)
                            + if (mShowToExpandHint) getContentOfString(mToExpandHint) + getContentOfString(
                        mGapToExpandHint
                    ) else ""
                )
                var indexEndTrimmedRevised = indexEndTrimmed
                if (remainWidth > widthTailReplaced) {
                    var extraOffset = 0
                    var extraWidth = 0
                    while (remainWidth > widthTailReplaced + extraWidth) {
                        extraOffset++
                        extraWidth = if (indexEndTrimmed + extraOffset <= mOrigText!!.length) {
                            (mTextPaint.measureText(
                                mOrigText!!.subSequence(
                                    indexEndTrimmed,
                                    indexEndTrimmed + extraOffset
                                ).toString()
                            ) + 0.5).toInt()
                        } else {
                            break
                        }
                    }
                    indexEndTrimmedRevised += extraOffset - 1
                } else {
                    var extraOffset = 0
                    var extraWidth = 0
                    while (remainWidth + extraWidth < widthTailReplaced) {
                        extraOffset--
                        extraWidth = if (indexEndTrimmed + extraOffset > indexStart) {
                            (mTextPaint.measureText(
                                mOrigText!!.subSequence(
                                    indexEndTrimmed + extraOffset,
                                    indexEndTrimmed
                                ).toString()
                            ) + 0.5).toInt()
                        } else {
                            break
                        }
                    }
                    indexEndTrimmedRevised += extraOffset
                }
                return if (lineTotal > 0) {
                    var fixText: CharSequence = ""
                    fixText =
                        if (ObjectUtils.isNotEmpty(showingText) && showingText.length > ELLIPSIS_HINT.length) {
                            showingText.substring(
                                0,
                                showingText.length - ELLIPSIS_HINT.length
                            )
                        } else {
                            showingText
                        }
                    val ssbShrink = SpannableStringBuilder(fixText)
                        .append(mEllipsisHint)
                    if (mShowToExpandHint) {
                        ssbShrink.append(
                            getContentOfString(mGapToExpandHint) + getContentOfString(
                                mToExpandHint
                            )
                        )
                        ssbShrink.setSpan(
                            mTouchableSpan,
                            ssbShrink.length - getLengthOfString(mToExpandHint),
                            ssbShrink.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    ssbShrink
                } else {
                    val fixText =
                        removeEndLineBreak(mOrigText!!.subSequence(0, indexEndTrimmedRevised))
                    val ssbShrink = SpannableStringBuilder(fixText)
                        .append(mEllipsisHint)
                    if (mShowToExpandHint) {
                        ssbShrink.append(
                            getContentOfString(mGapToExpandHint) + getContentOfString(
                                mToExpandHint
                            )
                        )
                        ssbShrink.setSpan(
                            mTouchableSpan,
                            ssbShrink.length - getLengthOfString(mToExpandHint),
                            ssbShrink.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    ssbShrink
                }
            }
            STATE_EXPAND -> {
                if (!mShowToShrinkHint) {
                    return mOrigText!!
                }
                mLayout1 = DynamicLayout(
                    mOrigText!!,
                    mTextPaint,
                    mLayoutWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0f,
                    0.0f,
                    false
                )
                mTextLineCount = mLayout1!!.lineCount + getLineCount(mOrigText!!)
                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return mOrigText!!
                }
                val ssbExpand = SpannableStringBuilder(mOrigText)
                    .append(mGapToShrinkHint).append(mToShrinkHint)
                ssbExpand.setSpan(
                    mTouchableSpan,
                    ssbExpand.length - getLengthOfString(mToShrinkHint),
                    ssbExpand.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return ssbExpand
            }
        }
        return mOrigText!!
    }

    private fun getLineCount(text: CharSequence): Int {
        val count = 0
        if (ObjectUtils.isNotEmpty(text.toString())) {
//            int indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
//            text = text.subSequence(0, indexEnd);
//            String[] s1 = text.toString().split("\n\n");
//            if (ObjectUtils.isNotEmpty(s1)) {
//                count = s1.length - 1;
//                for (int i = 0; i < s1.length; i++) {
//                    String[] s2 = s1[i].split("\n");
//                    if (ObjectUtils.isNotEmpty(s2)) {
//                        count = count + s2.length - 1;
//                    }
//                }
//            }
        }
        return count
    }

    private fun removeEndLineBreak(text: CharSequence): CharSequence {
        var text = text
        while (text.toString().endsWith("\n")) {
            text = text.subSequence(0, text.length - 1)
        }
        return text
    }

    fun setExpandListener(listener: OnExpandListener) {
        mOnExpandListener = listener
    }

    private fun getValidLayout(): Layout {
        return if (mLayout1 != null) mLayout1!! else layout
    }

    fun toggle() {
        when (mCurrState) {
            STATE_SHRINK -> {
                mCurrState = STATE_EXPAND
                if (mOnExpandListener != null) {
                    mOnExpandListener.onExpand(this)
                }
            }
            STATE_EXPAND -> {
                mCurrState = STATE_SHRINK
                if (mOnExpandListener != null) {
                    mOnExpandListener.onShrink(this)
                }
            }
        }
        setTextInternal(getNewTextByConfig(), mBufferType)
    }

    override fun setText(text: CharSequence, type: BufferType) {
        mOrigText = text
        mBufferType = type
        setTextInternal(getNewTextByConfig(), type)
    }

    private fun setTextInternal(text: CharSequence, type: BufferType) {
        super.setText(text, type)
    }

    private fun getLengthOfString(string: String?): Int {
        return string?.length ?: 0
    }

    private fun getContentOfString(string: String?): String {
        return string ?: ""
    }

    interface OnExpandListener {
        fun onExpand(view: ExpandTextView)
        fun onShrink(view: ExpandTextView)
    }


    fun getOnClickListener(view: View): OnClickListener? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getOnClickListenerV14(view)
        } else {
            getOnClickListenerV(view)
        }
    }

    private fun getOnClickListenerV(view: View): OnClickListener? {
        var retrievedListener: OnClickListener? = null
        try {
            val field =
                Class.forName(CLASS_NAME_VIEW)
                    .getDeclaredField("mOnClickListener")
            field.isAccessible = true
            retrievedListener = field[view] as OnClickListener
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return retrievedListener
    }

    private fun getOnClickListenerV14(view: View): OnClickListener? {
        var retrievedListener: OnClickListener? = null
        try {
            val listenerField =
                Class.forName(CLASS_NAME_VIEW)
                    .getDeclaredField("mListenerInfo")
            var listenerInfo: Any? = null
            if (listenerField != null) {
                listenerField.isAccessible = true
                listenerInfo = listenerField[view]
            }
            val clickListenerField =
                Class.forName(CLASS_NAME_LISTENER_INFO)
                    .getDeclaredField("mOnClickListener")
            if (clickListenerField != null && listenerInfo != null) {
                clickListenerField.isAccessible = true
                retrievedListener =
                    clickListenerField[listenerInfo] as OnClickListener
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return retrievedListener
    }


    inner class ExpandableClickListener : OnClickListener {
        override fun onClick(view: View) {
            toggle()
        }
    }

    /**
     * Copy from:
     * http://stackoverflow.com/questions
     * /20856105/change-the-text-color-of-a-single-clickablespan-when-pressed-without-affecting-o
     * By:
     * Steven Meliopoulos
     */
    inner class TouchableSpan : ClickableSpan() {
        private var mIsPressed = false
        fun setPressed(isSelected: Boolean) {
            mIsPressed = isSelected
        }

        override fun onClick(widget: View) {
            if (hasOnClickListeners()
                && getOnClickListener(this@ExpandTextView) is ExpandableClickListener
            ) {
            } else {
                toggle()
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            when (mCurrState) {
                STATE_SHRINK -> {
                    ds.color = mToExpandHintColor
                    ds.bgColor = if (mIsPressed) mToExpandHintColorBgPressed else 0
                }
                STATE_EXPAND -> {
                    ds.color = mToShrinkHintColor
                    ds.bgColor = if (mIsPressed) mToShrinkHintColorBgPressed else 0
                }
            }
            ds.isUnderlineText = false
        }
    }


    /**
     * Copy from:
     * http://stackoverflow.com/questions
     * /20856105/change-the-text-color-of-a-single-clickablespan-when-pressed-without-affecting-o
     * By:
     * Steven Meliopoulos
     */
    inner class LinkTouchMovementMethod : LinkMovementMethod() {
        private var mPressedSpan: TouchableSpan? = null

        override fun onTouchEvent(
            textView: TextView,
            spannable: Spannable,
            event: MotionEvent
        ): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event)
                if (mPressedSpan != null) {
                    mPressedSpan!!.setPressed(true)
                    Selection.setSelection(
                        spannable, spannable.getSpanStart(mPressedSpan),
                        spannable.getSpanEnd(mPressedSpan)
                    )
                }
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                val touchedSpan: TouchableSpan? = getPressedSpan(textView, spannable, event)
                if (mPressedSpan != null && touchedSpan !== mPressedSpan) {
                    mPressedSpan!!.setPressed(false)
                    mPressedSpan = null
                    Selection.removeSelection(spannable)
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan!!.setPressed(false)
                    super.onTouchEvent(textView, spannable, event)
                }
                mPressedSpan = null
                Selection.removeSelection(spannable)
            }
            return true
        }

        private fun getPressedSpan(
            textView: TextView,
            spannable: Spannable,
            event: MotionEvent
        ): TouchableSpan? {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= textView.totalPaddingLeft
            y -= textView.totalPaddingTop
            x += textView.scrollX
            y += textView.scrollY
            val layout = textView.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val link =
                spannable.getSpans(off, off, TouchableSpan::class.java)
            var touchedSpan: TouchableSpan? = null
            if (link.isNotEmpty()) {
                touchedSpan = link[0]
            }
            return touchedSpan
        }
    }
}