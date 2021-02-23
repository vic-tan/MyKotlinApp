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

/**
 * @desc:关注适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class FollowAdapter(data: MutableList<ClassmateCircleBean>) :
    BaseBindingAdapter<ClassmateCircleBean, ItemFollowBinding>(data) {

    private var screenWidth = ScreenUtils.getScreenWidth()
    private var textViewWidth = screenWidth - ConvertUtils.dp2px(30f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFollowBinding {
        return ItemFollowBinding.inflate(inflater, parent, false)
    }


    override fun convert(holder: BaseVBViewHolder<ItemFollowBinding>, item: ClassmateCircleBean) {
        holder.binding.name.text = item.nickName
        holder.binding.school.text =
            if (ObjectUtils.isEmpty(item.createtimeStr)) item.universityName else "${item.createtimeStr}  ${item.universityName}"
        GlideUtils.load(context, item.image?.url, holder.binding.banner)
        GlideUtils.loadAvatar(context, item.avatar, holder.binding.userHead)
        holder.binding.expandTextView.setExpandListener(object : ExpandTextView.OnExpandListener {
            override fun onExpand(view: ExpandTextView) {
                mPositionsAndStates.put(holder.adapterPosition, view.getExpandState())
            }

            override fun onShrink(view: ExpandTextView) {
                mPositionsAndStates.put(holder.adapterPosition, view.getExpandState())
            }

        })
        val state: Int? = mPositionsAndStates.get(holder.adapterPosition)
        holder.binding.expandTextView.updateForRecyclerView(
            item.content,
            textViewWidth,
            state ?: 0
        )
        holder.binding.expandTextView.visibility =
            if (TextUtils.isEmpty(item.content)) View.GONE else View.VISIBLE

    }
}