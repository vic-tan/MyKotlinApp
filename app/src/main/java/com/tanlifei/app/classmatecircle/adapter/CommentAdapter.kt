package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.databinding.ItemManualBinding
import com.tanlifei.app.profile.bean.ManualBean
import org.w3c.dom.Comment
import java.util.*

/**
 * @desc:评论适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class CommentAdapter() :
    CommonRvAdapter<CommentBean, ItemManualBinding>() {

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
        bean: CommentBean
    ) {
        holder.binding.title.text = bean.nickname
    }

    override fun addChildClickViewIds(binding: ItemManualBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.item)
    }
}