package com.common.widget.popup

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.core.base.adapter.BottomOptionsAdapter
import com.common.core.base.listener.OnItemClickListener
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
    context: Context,
    var list: MutableList<String>,
    var selectListener: OnSelectListener,
    var title: String = "",
    var cancel: String = "取消"
) :
    BottomPopupView(context) {

    lateinit var binding: LayoutOptionBinding
    lateinit var adapter: BottomOptionsAdapter
    override fun getImplLayoutId(): Int {
        return R.layout.layout_option
    }

    override fun onCreate() {
        super.onCreate()
        binding = LayoutOptionBinding.bind(popupImplView)
        adapter = BottomOptionsAdapter(ObjectUtils.isNotEmpty(title))
        adapter.mData = list
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = RecyclerUtils.setLinearLayoutManager(context)
        adapter.setItemClickListener(object :
            OnItemClickListener<ItemOptionBinding, String> {
            override fun click(
                binding: ItemOptionBinding,
                str: String,
                v: View,
                position: Int
            ) {
                when (v) {
                    binding.title -> {
                        selectListener.onSelect(position, str)
                        dismiss()
                    }
                }
            }
        })
        binding.title.setVisible(ObjectUtils.isNotEmpty(title))
        binding.titleLine.setVisible(ObjectUtils.isNotEmpty(title))
        binding.cancel.setVisible(ObjectUtils.isNotEmpty(cancel))
        binding.divider.setVisible(ObjectUtils.isNotEmpty(cancel))
        binding.title.text = title
        binding.cancel.text = cancel
        clickListener(
            binding.cancel,
            clickListener = View.OnClickListener {
                when (it) {
                    binding.cancel -> {
                        dismiss()
                    }
                }
            }
        )
    }


}