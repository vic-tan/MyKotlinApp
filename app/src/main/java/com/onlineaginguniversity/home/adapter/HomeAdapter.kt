package com.onlineaginguniversity.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.common.widget.component.extension.setVisible
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.databinding.ItemHomeBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeAdapter :
    BaseRvAdapter<CircleBean>() {


    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
            ItemHomeBinding.inflate(
                inflater, parent, false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: CircleBean
    ) {
        val holder = holder as ItemHomeBinding
        holder.apply {
           title.text = bean.nickName
           desc.text = bean.content
           time.text = "05月23日 15:20"
            GlideUtils.load(mContext, bean.image?.url,cover)
           headerTitleSplit.setVisible(position == 0)
           headerTitle.setVisible(position == 0)
           headerTitleLine.setVisible(position == 0)
        }

    }


    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemHomeBinding -> linkedSetOf(
                holder.item
            )
            else -> linkedSetOf()
        }
    }

}
