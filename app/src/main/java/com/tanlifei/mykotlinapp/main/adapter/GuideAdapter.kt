package com.tanlifei.mykotlinapp.main.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.ruffian.library.widget.RTextView
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.common.config.Const
import com.tanlifei.mykotlinapp.main.ui.MainActivity
import com.youth.banner.adapter.BannerAdapter


/**
 * @desc: 引导界面适配器
 * @author: tanlifei
 * @date: 2021/1/23 10:21
 */
open class GuideAdapter(activity: Activity, datas: MutableList<Int>) :
    BannerAdapter<Int, GuideAdapter.BannerViewHolder>(datas) {
    var mActivity = activity

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder? {
        var view =
            LayoutInflater.from(mActivity).inflate(
                R.layout.item_guide
                , parent, false
            )
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: Int,
        position: Int,
        size: Int
    ) {
        holder.image.setImageResource(data)
        holder.startBtn.visibility = if (position == mDatas.size - 1) View.VISIBLE else View.GONE
        holder.startBtn.setOnClickListener {
            SPUtils.getInstance().put(Const.SPKey.GUIDE, false)
            ActivityUtils.startActivity(MainActivity::class.java)
            ActivityUtils.finishActivity(mActivity)
        }
    }

    class BannerViewHolder(@NonNull view: View) :
        RecyclerView.ViewHolder(view) {
        var startBtn: RTextView = view.findViewById(R.id.startBtn) as RTextView
        var image: ImageView = view.findViewById(R.id.image) as ImageView

    }

}


