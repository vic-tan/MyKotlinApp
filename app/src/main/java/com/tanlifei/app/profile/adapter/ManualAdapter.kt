package com.tanlifei.app.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.tanlifei.app.databinding.ItemManualBinding
import com.tanlifei.app.profile.bean.ManualBean
import java.util.*

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class ManualAdapter :
    CommonRvAdapter<ManualBean, ItemManualBinding>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemManualBinding> {
        return CommonRvHolder(ItemManualBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: CommonRvHolder<ItemManualBinding>,
        position: Int,
        binding: ItemManualBinding,
        bean: ManualBean
    ) {
        holder.binding.title.text = bean.title
    }

    override fun addChildClickViewIds(binding: ItemManualBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.item)
    }
}