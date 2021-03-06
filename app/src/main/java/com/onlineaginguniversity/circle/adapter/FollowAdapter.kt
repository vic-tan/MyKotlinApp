package com.onlineaginguniversity.circle.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.common.widget.ExpandTextView
import com.common.widget.component.extension.*
import com.onlineaginguniversity.R
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.common.utils.AutoHeightUtils
import com.onlineaginguniversity.common.utils.NumberUtils
import com.onlineaginguniversity.databinding.ItemFollowBinding
import com.youth.banner.indicator.CircleIndicator
import java.util.*

/**
 * @desc:关注
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class FollowAdapter :
    BaseRvAdapter<CircleBean>() {
    private var mTextViewWidth = screenWidth - dp2px(30f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun setItemViewType(position: Int,itemBean: CircleBean): Int {
        return itemBean.mediaType
    }

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
        holder: BaseRvHolder<ViewBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (ObjectUtils.isEmpty(payloads)) {
            onBindViewHolder(holder, position)
        } else {
            val item = mData[position] as CircleBean
            val holder = holder.binding as ItemFollowBinding
            partChange(holder, item)
        }
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: CircleBean
    ) {
        holder as ItemFollowBinding
        holder.apply {
            banner.gone()
            playerLayout.gone()
            name.text = bean.nickName
            school.text =
                if (ObjectUtils.isEmpty(bean.createtimeStr)) bean.universityName else "${bean.createtimeStr}  ${bean.universityName}"
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

            when (bean.mediaType) {
                1 -> {//视频
                    playerLayout.layoutParams.width = screenWidth
                    playerLayout.layoutParams.height =
                        AutoHeightUtils.getHeightParams(screenWidth, bean.image)
                    val jzDataSource = JZDataSource(bean.videoUrl)
                    jzDataSource.looping = true
                    player.setUp(jzDataSource, Jzvd.SCREEN_NORMAL)
                    Glide.with(player.context).load(bean.image?.url)
                        .into(player.posterImageView)
                    playerLayout.visible()
                }
                else -> {
                    banner.layoutParams.width = screenWidth
                    banner.layoutParams.height =
                        AutoHeightUtils.getHeightParams(screenWidth, bean.image)
                    //banner
                    banner.adapter =
                        CircleBannerAdapter(
                            banner.viewPager2,
                            bean.imagesUrlList
                        )
                    banner.indicator = CircleIndicator(mContext)
                    banner.currentItem = 1
                    banner.isAutoLoop(false)
                    banner.adapter.notifyDataSetChanged()
                    banner.visible()
                }
            }
        }
        partChange(holder, bean)
    }

    private fun partChange(holder: ItemFollowBinding, bean: CircleBean) {
        holder.apply {
            praiseCount.text = NumberUtils.setPraiseCount(bean.star)
            praiseIcon.setImageDrawable(drawable(if (bean.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise_gray))
            commentCount.text = NumberUtils.setCommentCount(bean.comment)
        }
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemFollowBinding -> linkedSetOf(
                holder.more,
                holder.shareLayout,
                holder.playerControl,
                holder.praiseLayout
            )
            else -> linkedSetOf()
        }
    }
}
