package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
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
class MenuAdapter :
    CommonRvAdapter<MenuBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(ItemHomeMenuBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: MenuBean
    ) {
        holder as ItemHomeMenuBinding
        holder.title.text = bean.name
        GlideUtils.load(mContext, bean.image, holder.icon)
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemHomeMenuBinding
        return linkedSetOf(holder.item)
    }


}