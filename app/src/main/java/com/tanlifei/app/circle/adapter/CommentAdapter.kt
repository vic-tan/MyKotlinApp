package com.tanlifei.app.circle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.common.widget.component.extension.setVisible
import com.tanlifei.app.circle.bean.CommentBean
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ItemCommentBinding
import com.tanlifei.app.databinding.ItemFollowBinding
import java.util.*

/**
 * @desc:评论适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class CommentAdapter :
    BaseRvAdapter<CommentBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
            ItemCommentBinding.inflate(
                inflater, parent, false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: CommentBean
    ) {
        val holder = holder as ItemCommentBinding
        holder.apply {
            name.text = bean.nickname
            content.text = bean.content
            time.text = bean.createTimeStr
            school.text = bean.universityName
            school.setVisible(ObjectUtils.isNotEmpty(bean.universityName))
            GlideUtils.loadAvatar(ComFun.mContext, bean.avatar, userHead)
            delete.setVisible(UserInfoUtils.getUid() == bean.uid)
        }

    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemCommentBinding -> linkedSetOf(
                holder.delete,
            )
            else -> linkedSetOf()
        }
    }

}