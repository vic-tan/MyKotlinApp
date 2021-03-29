package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.common.widget.extension.setVisible
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ItemCommentBinding
import java.util.*

/**
 * @desc:评论适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class CommentAdapter :
    CommonRvAdapter<CommentBean>() {

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
        val holder = holder as ItemCommentBinding
        return linkedSetOf(holder.delete)
    }


}