package com.common.widget.popup

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View.OnClickListener
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.R
import com.common.databinding.LayoutBottomEditViewBinding
import com.common.utils.extension.clickListener
import com.common.widget.TextInputHelper
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:底部弹出输入框
 * @author: tanlifei
 * @date: 2021/3/9 11:17
 */
class BottomInputEditView(
    context: Context,
    var hintText: String = "请输入内容",
    var btnText: String = "确定",
    var maxNum: Int = 200,
    var callBack: CallBack
) :
    BottomPopupView(context),
    TextWatcher {
    lateinit var binding: LayoutBottomEditViewBinding
    private lateinit var mInputHelper: TextInputHelper

    override fun getImplLayoutId(): Int {
        return R.layout.layout_bottom_edit_view
    }

    override fun onCreate() {
        super.onCreate()
        binding = LayoutBottomEditViewBinding.bind(popupImplView)
        binding.etInput.hint = hintText
        binding.etInput.filters = arrayOf(InputFilter.LengthFilter(maxNum))
        binding.enter.text = btnText
        binding.num.text = "${0}/${maxNum}"
        initTextInputHelper()
        clickListener(
            binding.enter,
            clickListener = OnClickListener {
                when (it) {
                    binding.enter -> {
                        dismiss()
                        callBack.callback(binding.etInput.text.toString())
                    }
                }
            }
        )
    }


    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        if (ObjectUtils.isNotEmpty(binding)) {
            mInputHelper = TextInputHelper(context, binding.enter)
            mInputHelper.addViews(binding.etInput)
            binding.etInput.addTextChangedListener(this)
        }
    }

    override fun getMaxHeight(): Int {
        return (ScreenUtils.getScreenHeight() * 0.5).toInt()
    }

    override fun afterTextChanged(s: Editable?) {
        if (ObjectUtils.isNotEmpty(binding)) {
            binding.num.text = "${s?.length}/${maxNum}"
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    interface CallBack {
        fun callback(inputText: String)
    }
}