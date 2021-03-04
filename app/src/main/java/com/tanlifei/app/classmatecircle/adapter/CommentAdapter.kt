package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.adapter.CommonRvMultiItemAdapter
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.databinding.ItemCommentBinding
import java.util.*

/**
 * @desc:评论适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class CommentAdapter :
    CommonRvMultiItemAdapter<CommentBean>() {

    override fun setItemViewType(int: Int): Int {
        return ItemViewType.CONTEN.ordinal
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(
            ItemCommentBinding.inflate(
                inflater, parent, false
            )
        )
    }


    override fun onBindViewHolder(
        holder: CommonRvHolder<ViewBinding>,
        position: Int,
        bean: CommentBean
    ) {
        val holder = holder.binding as ItemCommentBinding
        holder.title.text = bean.nickname
    }


    override fun addChildClickViewIds(holder: CommonRvHolder<ViewBinding>): LinkedHashSet<View> {
        val holder = holder.binding as ItemCommentBinding
        return linkedSetOf(holder.item)
    }


}