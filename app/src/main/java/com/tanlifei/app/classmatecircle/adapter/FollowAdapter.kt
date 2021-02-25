package com.tanlifei.app.classmatecircle.adapter

import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.ComApplication.Companion.context
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.common.widget.ExpandTextView
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ItemFollowBinding
import java.util.LinkedHashSet

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class FollowAdapter : CommonRvAdapter<ClassmateCircleBean, ItemFollowBinding>() {

    private var screenWidth = ScreenUtils.getScreenWidth()
    private var textViewWidth = screenWidth - ConvertUtils.dp2px(30f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemFollowBinding> {

        return CommonRvHolder(
            ItemFollowBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CommonRvHolder<ItemFollowBinding>,
        position: Int,
        binding: ItemFollowBinding,
        item: ClassmateCircleBean
    ) {
        holder.binding.banner.layoutParams.width = screenWidth
        holder.binding.banner.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, item.image)

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
        holder.binding.marginView.visibility =
            if (TextUtils.isEmpty(item.content)) View.GONE else View.VISIBLE

        holder.binding.praiseCount.text = NumberUtils.setPraiseCount(item.star)
        holder.binding.praiseIcon.setImageResource(if (item.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise)


        holder.binding.commentCount.text = NumberUtils.setCommentCount(item.comment)


    }

    override fun addChildClickViewIds(binding: ItemFollowBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.more, binding.shareLayout)
    }

}