package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.common.utils.GlideUtils
import com.common.utils.PhotoUtils
import com.common.utils.extension.click
import com.tanlifei.app.databinding.ItemHomeBannerBinding
import com.tanlifei.app.home.bean.BannerBean
import com.youth.banner.adapter.BannerAdapter

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/16 17:15
 */
class HomeBannerAdapter(
    var mViewPager2: ViewPager2,
    mBannerList: MutableList<BannerBean>
) :
    BannerAdapter<BannerBean, HomeBannerAdapter.BannerViewHolder>(mBannerList) {
    private lateinit var mContext: Context

    inner class BannerViewHolder(val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        mContext = parent.context
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
        GlideUtils.load(mContext, data.image, holder.binding.image)
        holder.binding.image.click {
            val photoList: MutableList<String> = mutableListOf()
            for (banerBean in mDatas) {
                banerBean.image?.let { it1 -> photoList.add(it1) }
            }
            PhotoUtils.showBannerPhoto(
                mContext,
                holder.binding.image,
                position,
                mViewPager2,
                photoList
            )
        }
    }
}