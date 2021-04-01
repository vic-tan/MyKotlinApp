package com.tanlifei.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.tanlifei.app.databinding.ItemFollowBinding
import com.tanlifei.app.databinding.ItemHomeMenuBinding
import com.tanlifei.app.home.bean.MenuBean
import java.util.*

/**
 * @desc:菜单适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class MenuAdapter :
    BaseRvAdapter<MenuBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(ItemHomeMenuBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: MenuBean
    ) {
        holder as ItemHomeMenuBinding
        holder.apply {
            title.text = bean.name
            GlideUtils.load(mContext, bean.image, icon)
        }

    }

    override fun <V : ViewBinding> addChildClickView(holder: V): LinkedHashSet<View> {
        return when (holder) {
            is ItemHomeMenuBinding -> linkedSetOf(
                holder.item
            )
            else -> linkedSetOf()
        }
    }

}