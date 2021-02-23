package com.tanlifei.app.classmatecircle.adapter

import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.core.base.holder.BaseBindingAdapter
import com.common.core.base.holder.BaseVBViewHolder
import com.common.utils.GlideUtils
import com.common.widget.ExpandTextView
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.ItemFollowBinding
import com.tanlifei.app.databinding.ItemRecommendBinding

/**
 * @desc:推荐适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class RecommendAdapter(data: MutableList<ClassmateCircleBean>) :
    BaseBindingAdapter<ClassmateCircleBean, ItemRecommendBinding>(data) {


    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemRecommendBinding {
        return ItemRecommendBinding.inflate(inflater, parent, false)
    }


    override fun convert(
        holder: BaseVBViewHolder<ItemRecommendBinding>,
        item: ClassmateCircleBean
    ) {
        holder.binding.name.text = item.nickName
        holder.binding.school.text =
            if (ObjectUtils.isEmpty(item.createtimeStr)) item.universityName else "${item.createtimeStr}  ${item.universityName}"
        GlideUtils.load(context, item.image?.url, holder.binding.banner)
        GlideUtils.loadAvatar(context, item.avatar, holder.binding.userHead)


    }


}