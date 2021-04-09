package com.onlineaginguniversity.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.onlineaginguniversity.databinding.ItemManualBinding
import com.onlineaginguniversity.profile.bean.ManualBean
import java.util.*

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class ManualAdapter :
    BaseRvAdapter<ManualBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(ItemManualBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: ManualBean
    ) {
        val holder = holder as ItemManualBinding
        holder.apply {
            title.text = bean.title
        }
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemManualBinding -> linkedSetOf(
                holder.item
            )
            else -> linkedSetOf()
        }
    }
}