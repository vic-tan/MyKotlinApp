package com.common.widget.popup

import android.content.Context
import android.view.View
import com.common.R
import com.common.databinding.LayoutShareBinding
import com.common.utils.extension.clickListener
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:底部列表
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class BottomOptionView<T>(context: Context) :
    BottomPopupView(context),
    View.OnClickListener {

    lateinit var binding: LayoutShareBinding
    override fun getImplLayoutId(): Int {
        return R.layout.layout_share
    }

    override fun onCreate() {
        super.onCreate()
        binding = LayoutShareBinding.bind(popupImplView)
        context.clickListener(
            this,
            binding.wx,
            binding.wxCircle,
            binding.report,
            binding.credit,
            binding.cancel
        )
    }


    override fun onClick(v: View) {

    }


}