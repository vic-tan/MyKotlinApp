package com.tanlifei.app.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.CommonRvAdapter
import com.common.base.adapter.CommonRvHolder
import com.tanlifei.app.databinding.ItemManualBinding
import com.tanlifei.app.profile.bean.ManualBean
import java.util.*

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class ManualAdapter :
    CommonRvAdapter<ManualBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(ItemManualBinding.inflate(inflater, parent, false))
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
        val holder = holder as ItemManualBinding
        return linkedSetOf(holder.item)
    }
}