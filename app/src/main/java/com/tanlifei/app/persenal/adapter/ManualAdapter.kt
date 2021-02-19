package com.tanlifei.app.persenal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.core.base.holder.BaseBindingAdapter
import com.common.core.base.holder.BaseVBViewHolder
import com.tanlifei.app.persenal.bean.ManualBean
import com.tanlifei.app.databinding.ItemManualBinding

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