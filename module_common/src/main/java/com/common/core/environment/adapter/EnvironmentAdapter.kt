package com.common.core.environment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.core.base.adapter.CommonRvMultiHolder
import com.common.core.base.adapter.CommonRvMultiItemAdapter
import com.common.core.environment.adapter.holder.EnvironmentContentHolder
import com.common.core.environment.adapter.holder.EnvironmentTitleHolder
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
    ): CommonRvMultiHolder {
        return when (viewType) {
            EnvironmentBean.TITLE ->
                EnvironmentTitleHolder(
                    ItemEnvironmentTitleBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            else -> EnvironmentContentHolder(
                ItemEnvironmentContentBinding.inflate(
                    inflater, parent, false
                )
            )

        }
    }

    override fun onBindViewHolder(
        holder: CommonRvMultiHolder,
        position: Int,
        bean: EnvironmentBean
    ) {
        when (holder) {
            is EnvironmentTitleHolder -> {
                holder.binding.title.text = bean.alias
            }
            is EnvironmentContentHolder -> {
                holder.binding.title.text = bean.alias
                holder.binding.url.text = bean.url
                holder.binding.radio.setImageResource(if (bean.defaultCheck) com.common.R.mipmap.ic_select_pre else com.common.R.mipmap.ic_select)
            }
        }
    }

    override fun setItemViewType(bean: EnvironmentBean): Int {
        return bean.itemType
    }

    override fun addChildClickViewIds(holder: CommonRvMultiHolder): LinkedHashSet<View> {
        return when (holder) {
            is EnvironmentTitleHolder -> {
                linkedSetOf()
            }
            is EnvironmentContentHolder -> {
                linkedSetOf(holder.binding.layout)
            }
            else -> {
                linkedSetOf()
            }
        }
    }


}