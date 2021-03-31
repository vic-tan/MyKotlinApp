package com.tanlifei.app.circle.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.viewbinding.ViewBinding
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.base.listener.ComListener
import com.common.constant.GlobalEnumConst
import com.common.utils.GlideUtils
import com.common.widget.ExpandTextView
import com.common.widget.component.extension.*
import com.tanlifei.app.R
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.common.constant.EnumConst
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ItemVideoPagerBinding
import java.util.*

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class VideoPagerAdapter(var backCall: ComListener.BackCall?) :
    BaseRvAdapter<CircleBean>() {
    private var mTextViewWidth = screenWidth - dp2px(120f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(ItemVideoPagerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        item: CircleBean
    ) {
        val holder = holder as ItemVideoPagerBinding


        val jzDataSource = JZDataSource(item.videoUrl)
        jzDataSource.looping = true
        holder.apply {
            GlideUtils.load(mContext, item.avatar, avatar)
            name.text = item.nickName
            school.text = item.universityName
            school.setVisible(ObjectUtils.isNotEmpty(item.universityName))

            player.setUp(jzDataSource, Jzvd.SCREEN_NORMAL)
            player.seekBar = seekBar
            Glide.with(player.context).load(item.image?.url)
                .into(player.posterImageView)
            share.imageAssetsFolder = "anim/shareimages/"
            share.setAnimation("anim/share.json")
            share.repeatCount = Int.MAX_VALUE
            share.playAnimation()
            praise.drawableText(
                if (item.isStar) R.mipmap.ic_praise_video_pre else R.mipmap.ic_praise_video_normal,
                if (item.isStar) 36f else 40f,
                40f,
                GlobalEnumConst.DrawableDirection.TOP
            )
            comment.drawableText(
                R.mipmap.ic_comment,
                40f,
                40f,
                GlobalEnumConst.DrawableDirection.TOP
            )
            praise.text = NumberUtils.setPraiseCount(item.star)
            comment.text = NumberUtils.setCommentCount(item.comment)

            //商品分类
            topicLayout.setVisible(ObjectUtils.isNotEmpty(item.entertainmentTopicName))
            topicTxt.text = item.entertainmentTopicName

            //商品链接
            typeLayout.setVisible(ObjectUtils.isNotEmpty(item.jumpCode))
            typeTxt.gone()
            typeIcon.gone()
            if (StringUtils.equals(
                    item.jumpCode,
                    EnumConst.LinkJumpType.TYPE_SHOP_JIUJIU.value
                )
            ) { //0,表示没有，1，表示来源于商品，2，表示来源于课程3.表示来源来旅游
                typeIcon.visible()
            } else if (StringUtils.equals(
                    item.jumpCode,
                    EnumConst.LinkJumpType.TYPE_LIVE_COLUMN_FREE.value
                ) //免费课程
                || StringUtils.equals(
                    item.jumpCode,
                    EnumConst.LinkJumpType.TYPE_LIVE_COLUMN_CHARGE.value
                )
            ) { //精品课程
                typeTxt.visible()
                typeTxt.text = "课程"
            } else if (StringUtils.equals(
                    item.jumpCode,
                    EnumConst.LinkJumpType.TYPE_SHOP_TRAVEL.value
                )
            ) {
                typeTxt.visible()
                typeTxt.text = "旅游"
            }


            attention.helper.backgroundColorNormal =
                color(if (item.isFollowing == 1) R.color.color_33FFFFFF else R.color.theme)
            attention.helper.borderColorNormal =
                color(if (item.isFollowing == 1) R.color.color_99FFFFFF else R.color.theme)

            if (item.isFollowing == 1) {
                if (item.isFollowing == 1) {
                    attention.text = "相互关注"
                } else {
                    attention.text = "已关注"
                }
            } else {
                attention.text = "关注"
            }
            expandTextView.setVisible(ObjectUtils.isNotEmpty(item.content))
            expandTextView.setIsExpand(false)
            expandTextView.setExpandListener(object : ExpandTextView.OnExpandListener {
                override fun onExpand(view: ExpandTextView) {
                    mPositionsAndStates.put(position, view.getExpandState())
                    backCall?.call(holder, item)
                }

                override fun onShrink(view: ExpandTextView) {
                    mPositionsAndStates.put(position, view.getExpandState())
                    backCall?.call(holder, item)
                }

            })
            val state: Int? = mPositionsAndStates.get(position)
            expandTextView.updateForRecyclerView(
                item.content,
                mTextViewWidth,
                state ?: 0
            )

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    //总时间长度
                    val duration: Long =
                        player.duration / 100L * seekBar.progress * 1L
                    player.mediaInterface.seekTo(duration)
                }

            })
        }
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        val holder = holder as ItemVideoPagerBinding
        return linkedSetOf(
            holder.praise,
            holder.comment,
            holder.share,
            holder.typeLayout
        )
    }
}