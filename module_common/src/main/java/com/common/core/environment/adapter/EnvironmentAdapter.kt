package com.common.core.environment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.adapter.CommonRvMultiItemAdapter
import com.common.core.environment.bean.EnvironmentBean
import com.common.databinding.ItemEnvironmentContentBinding
import com.common.databinding.ItemEnvironmentTitleBinding
import java.util.*


/**
 * @desc:环境切换器适配器
 * @author: tanlifei
 * @date: 2021/2/2 17:58
 */
class EnvironmentAdapter :
    CommonRvMultiItemAdapter<EnvironmentBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return when (viewType) {
            EnvironmentBean.TITLE ->
                CommonRvHolder(
                    ItemEnvironmentTitleBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            else -> CommonRvHolder(
                ItemEnvironmentContentBinding.inflate(
                    inflater, parent, false
                )
            )

        }
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        itemBean: EnvironmentBean
    ) {
        when (holder) {
            is ItemEnvironmentTitleBinding -> {
                holder.title.text = itemBean.alias
            }
            is ItemEnvironmentContentBinding -> {
                holder.title.text = itemBean.alias
                holder.url.text = itemBean.url
                holder.radio.setImageResource(if (itemBean.defaultCheck) com.common.R.mipmap.ic_select_pre else com.common.R.mipmap.ic_select)
            }
        }
    }

    override fun setItemViewType(itemBean: EnvironmentBean): Int {
        return itemBean.itemType
    }

    override fun addChildClickViewIds(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemEnvironmentTitleBinding -> {
                linkedSetOf()
            }
            is ItemEnvironmentContentBinding -> {
                linkedSetOf(holder.layout)
            }
            else -> {
                linkedSetOf()
            }
        }
    }


}