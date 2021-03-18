package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.tanlifei.app.databinding.ItemHomeMenuBinding
import com.tanlifei.app.home.bean.MenuBean
import java.util.*

/**
 * @desc:菜单适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class MenuAdapter(val context: Context?) :
    CommonRvAdapter<MenuBean, ItemHomeMenuBinding>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemHomeMenuBinding> {
        return CommonRvHolder(ItemHomeMenuBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: CommonRvHolder<ItemHomeMenuBinding>,
        position: Int,
        binding: ItemHomeMenuBinding,
        bean: MenuBean
    ) {
        holder.binding.title.text = bean.name
        GlideUtils.load(context, bean.image, binding.icon)
    }

    override fun addChildClickViewIds(binding: ItemHomeMenuBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.item)
    }


}