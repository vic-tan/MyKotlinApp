package com.common.core.environment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.R
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
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
    BaseRvAdapter<EnvironmentBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return when (viewType) {
            EnvironmentBean.TITLE ->
                BaseRvHolder(
                    ItemEnvironmentTitleBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            else -> BaseRvHolder(
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
                holder.apply {
                    title.text = itemBean.alias
                }
            }
            is ItemEnvironmentContentBinding -> {
                holder.apply {
                    title.text = itemBean.alias
                    url.text = itemBean.url
                    radio.setImageResource(if (itemBean.defaultCheck) R.mipmap.ic_select_pre else R.mipmap.ic_select)
                }
            }
        }
    }

    override fun setItemViewType(itemBean: EnvironmentBean): Int {
        return itemBean.itemType
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
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