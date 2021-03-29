package com.tanlifei.app.classmatecircle.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.common.widget.component.extension.screenWidth
import com.common.widget.component.extension.setVisible
import com.common.widget.ExpandTextView
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ItemFollowBinding
import java.util.*

/**
 * @desc:关注
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class FollowAdapter :
    BaseRvAdapter<CircleBean>() {
    private var mTextViewWidth = screenWidth - ConvertUtils.dp2px(30f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
            ItemFollowBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: CircleBean
    ) {
        holder as ItemFollowBinding
        holder.apply {
            banner.layoutParams.width = screenWidth
            banner.layoutParams.height =
                AutoHeightUtils.getHeightParams(screenWidth, bean.image)

            name.text = bean.nickName
            school.text =
                if (ObjectUtils.isEmpty(bean.createtimeStr)) bean.universityName else "${bean.createtimeStr}  ${bean.universityName}"
            GlideUtils.load(mContext, bean.image?.url, banner)
            GlideUtils.loadAvatar(mContext, bean.avatar, userHead)
            expandTextView.setExpandListener(object : ExpandTextView.OnExpandListener {
                override fun onExpand(view: ExpandTextView) {
                    mPositionsAndStates.put(position, view.getExpandState())
                }

                override fun onShrink(view: ExpandTextView) {
                    mPositionsAndStates.put(position, view.getExpandState())
                }

            })
            val state: Int? = mPositionsAndStates.get(position)
            expandTextView.updateForRecyclerView(
                bean.content,
                mTextViewWidth,
                state ?: 0
            )
            expandTextView.setVisible(ObjectUtils.isNotEmpty(bean.content))
            marginView.setVisible(ObjectUtils.isNotEmpty(bean.content))
            praiseCount.text = NumberUtils.setPraiseCount(bean.star)
            praiseIcon.setImageResource(if (bean.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise_gray)
            commentCount.text = NumberUtils.setCommentCount(bean.comment)
        }
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemFollowBinding
        return linkedSetOf(holder.more, holder.shareLayout, holder.banner)
    }

}
