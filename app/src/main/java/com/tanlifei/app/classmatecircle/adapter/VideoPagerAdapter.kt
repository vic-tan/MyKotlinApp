package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.common.base.adapter.CommonRvAdapter
import com.common.base.adapter.CommonRvHolder
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.databinding.ItemVideoPagerBinding
import java.util.*

/**
 * @desc:操作手册适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class VideoPagerAdapter :
    CommonRvAdapter<CircleBean>() {


    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(ItemVideoPagerBinding.inflate(inflater, parent, false))
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
        }
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        val holder = holder as ItemVideoPagerBinding
        return linkedSetOf()
    }
}