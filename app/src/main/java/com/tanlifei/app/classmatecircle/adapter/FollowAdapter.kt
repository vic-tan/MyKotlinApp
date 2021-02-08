package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.common.core.base.holder.BaseBindingAdapter
import com.common.core.base.holder.BaseVBViewHolder
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.ItemFollowBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class FollowAdapter(data: MutableList<ClassmateCircleBean>) :
    BaseBindingAdapter<ClassmateCircleBean, ItemFollowBinding>(data) {
    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFollowBinding {
        return ItemFollowBinding.inflate(inflater, parent, false)
    }

    override fun convert(holder: BaseVBViewHolder<ItemFollowBinding>, item: ClassmateCircleBean) {
        holder.vb.name.text = item.nickName
        holder.vb.school.text =
            if (ObjectUtils.isNotEmpty(item.createtimeStr)) item.universityName else "${item.createtimeStr}  ${item.universityName}"
        Glide.with(context)
            .load(item.avatar).into(holder.vb.userHead)
        Glide.with(context)
            .load(item.image?.url).into(holder.vb.banner)

    }
}