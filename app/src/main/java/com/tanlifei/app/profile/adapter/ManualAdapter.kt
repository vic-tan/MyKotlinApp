package com.tanlifei.app.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.core.base.holder.BaseBindingAdapter
import com.common.core.base.holder.BaseVBViewHolder
import com.tanlifei.app.databinding.ItemManualBinding
import com.tanlifei.app.profile.bean.ManualBean

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class ManualAdapter(data: MutableList<ManualBean>) :
    BaseBindingAdapter<ManualBean, ItemManualBinding>(data) {
    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemManualBinding {
        return ItemManualBinding.inflate(inflater, parent, false)
    }

    override fun convert(holder: BaseVBViewHolder<ItemManualBinding>, item: ManualBean) {
        holder.binding.title.text = item.title
    }
}