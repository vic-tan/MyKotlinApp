package com.tanlifei.app.classmatecircle.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.common.utils.extension.screenWidth
import com.common.utils.extension.setVisible
import com.common.widget.ExpandTextView
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ItemFollowBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class FollowAdapter:
    CommonRvAdapter<ClassmateCircleBean, ItemFollowBinding>() {
    private lateinit var mContext: Context
    private var mTextViewWidth = screenWidth - ConvertUtils.dp2px(30f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemFollowBinding> {
        mContext = parent.context
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
        bean: ClassmateCircleBean
    ) {
        holder.binding.banner.layoutParams.width = screenWidth
        holder.binding.banner.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, bean.image)

        holder.binding.name.text = bean.nickName
        holder.binding.school.text =
            if (ObjectUtils.isEmpty(bean.createtimeStr)) bean.universityName else "${bean.createtimeStr}  ${bean.universityName}"
        GlideUtils.load(mContext, bean.image?.url, holder.binding.banner)
        GlideUtils.loadAvatar(mContext, bean.avatar, holder.binding.userHead)
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
            bean.content,
            mTextViewWidth,
            state ?: 0
        )
        holder.binding.expandTextView.setVisible(ObjectUtils.isNotEmpty(bean.content))
        holder.binding.marginView.setVisible(ObjectUtils.isNotEmpty(bean.content))
        holder.binding.praiseCount.text = NumberUtils.setPraiseCount(bean.star)
        holder.binding.praiseIcon.setImageResource(if (bean.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise_gray)
        holder.binding.commentCount.text = NumberUtils.setCommentCount(bean.comment)
    }

    override fun addChildClickViewIds(binding: ItemFollowBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.more, binding.shareLayout, binding.banner)
    }

}
