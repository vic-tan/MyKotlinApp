package com.common.widget.popup

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.core.base.adapter.BottomOptionsAdapter
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.listener.OnMultiItemListener
import com.common.databinding.ItemOptionBinding
import com.common.databinding.LayoutOptionBinding
import com.common.utils.RecyclerUtils
import com.common.utils.extension.clickListener
import com.common.utils.extension.setVisible
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener

/**
 * @desc:底部列表
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class BottomOptionsView(
    mContext: Context,
    var list: MutableList<Any>,
    var selectListener: OnSelectListener,
    var title: String = "",
    var cancel: String = "取消"
) :
    BottomPopupView(mContext) {

    lateinit var mBinding: LayoutOptionBinding
    lateinit var mAdapter: BottomOptionsAdapter
    override fun getImplLayoutId(): Int {
        return R.layout.layout_option
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = LayoutOptionBinding.bind(popupImplView)
        mAdapter = BottomOptionsAdapter(ObjectUtils.isNotEmpty(title))
        mAdapter.mData = list
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.layoutManager = RecyclerUtils.setLinearLayoutManager(context)
        mAdapter.setItemClickListener(object :
            OnMultiItemListener<String> {
            override fun click(
                holder: ViewBinding,
                str: String,
                v: View,
                position: Int
            ) {
                holder as ItemOptionBinding
                when (v) {
                    holder.title -> {
                        selectListener.onSelect(position, str)
                        dismiss()
                    }
                }
            }
        })
        mBinding.title.setVisible(ObjectUtils.isNotEmpty(title))
        mBinding.titleLine.setVisible(ObjectUtils.isNotEmpty(title))
        mBinding.cancel.setVisible(ObjectUtils.isNotEmpty(cancel))
        mBinding.divider.setVisible(ObjectUtils.isNotEmpty(cancel))
        mBinding.title.text = title
        mBinding.cancel.text = cancel
        clickListener(
            mBinding.cancel,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.cancel -> {
                        dismiss()
                    }
                }
            }
        )
    }


}