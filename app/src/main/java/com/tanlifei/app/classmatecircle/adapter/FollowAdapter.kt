package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.common.core.base.holder.BaseBindingAdapter
import com.common.core.base.holder.BaseVBViewHolder
import com.common.utils.MyLogTools
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.ItemFollowBinding

/**
 * @desc:关注适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class FollowAdapter(data: MutableList<ClassmateCircleBean>) :
    BaseBindingAdapter<ClassmateCircleBean, ItemFollowBinding>(data) {
    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFollowBinding {
        return ItemFollowBinding.inflate(inflater, parent, false)
    }


    override fun convert(holder: BaseVBViewHolder<ItemFollowBinding>, item: ClassmateCircleBean) {
        holder.binding.name.text = item.nickName
        holder.binding.school.text =
            if (ObjectUtils.isEmpty(item.createtimeStr)) item.universityName else "${item.createtimeStr}  ${item.universityName}"
        Glide.with(context)
            .load(item.avatar).into(holder.binding.userHead)
        Glide.with(context)
            .load(item.image?.url).into(holder.binding.banner)
    }
}