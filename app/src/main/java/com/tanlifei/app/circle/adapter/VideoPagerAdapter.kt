package com.tanlifei.app.circle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.constant.GlobalEnumConst
import com.common.widget.component.extension.drawableText
import com.tanlifei.app.R
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ItemVideoPagerBinding
import java.util.*

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class VideoPagerAdapter :
    BaseRvAdapter<CircleBean>() {


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
            player.setUp(jzDataSource, Jzvd.SCREEN_NORMAL)
            Glide.with(player.context).load(item.image?.url)
                .into(player.posterImageView)
            share.imageAssetsFolder = "anim/shareimages/"
            share.setAnimation("anim/share.json")
            share.repeatCount = Int.MAX_VALUE
            share.playAnimation()
            praise.drawableText(R.mipmap.ic_praise_video_normal,40f,40f,GlobalEnumConst.DrawableDirection.TOP)
            comment.drawableText(R.mipmap.ic_comment,40f,40f,GlobalEnumConst.DrawableDirection.TOP)
            praise.text = NumberUtils.setPraiseCount(item.star)
//            praise.setImageResource(if (item.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise_gray)
            comment.text = NumberUtils.setCommentCount(item.comment)
        }
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        val holder = holder as ItemVideoPagerBinding
        return linkedSetOf(holder.praise, holder.comment, holder.share,holder.typeLayout)
    }
}