package com.tanlifei.app.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.common.utils.extension.setVisible
import com.tanlifei.app.common.config.Const
import com.tanlifei.app.databinding.ItemGuideBinding
import com.tanlifei.app.main.ui.activity.LoginAtivity
import com.youth.banner.adapter.BannerAdapter


/**
 * @desc: 引导界面适配器
 * @author: tanlifei
 * @date: 2021/1/23 10:21
 */
open class GuideAdapter(private val mActivity: Activity, mList: MutableList<Int>) :
    BannerAdapter<Int, GuideAdapter.BannerViewHolder>(mList) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder? {
        val binding = ItemGuideBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: Int,
        position: Int,
        size: Int
    ) {
        holder.binding.image.setImageResource(data)
        holder.binding.startBtn.setVisible(position == mDatas.size - 1)
        holder.binding.startBtn.setOnClickListener {
            SPUtils.getInstance().put(Const.SPKey.GUIDE, false)
            ActivityUtils.startActivity(LoginAtivity::class.java)
            ActivityUtils.finishActivity(mActivity)
        }
    }

    inner class BannerViewHolder(val binding: ItemGuideBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}


