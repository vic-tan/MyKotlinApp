package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.common.utils.GlideUtils
import com.tanlifei.app.databinding.ItemHomeBannerBinding
import com.tanlifei.app.home.bean.BannerBean
import com.youth.banner.adapter.BannerAdapter

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/16 17:15
 */
class HomeBannerAdapter(var context: Context?, datas: MutableList<BannerBean>) :
    BannerAdapter<BannerBean, HomeBannerAdapter.BannerViewHolder>(datas) {

    inner class BannerViewHolder(val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemHomeBannerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: BannerBean,
        position: Int,
        size: Int
    ) {
        GlideUtils.load(context, data.image, holder.binding.image)
    }
}