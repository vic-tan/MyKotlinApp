package com.tanlifei.app.main.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.common.core.base.ui.activity.BaseActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.tanlifei.app.databinding.ActivityAdsBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.main.viewmodel.AdsViewModel
import com.tanlifei.app.main.viewmodel.factory.AdsModelFactory

/**
 * @desc:广告页
 * @author: tanlifei
 * @date: 2021/2/3 17:58
 */
class AdsActivity : BaseActivity<ActivityAdsBinding>() {

    private lateinit var adsViewModel: AdsViewModel

    companion object {
        private const val EXTRAS_DATA = "extras_data"
        fun actionStart(context: Context, adsBean: AdsBean) {
            var intent = Intent(context, AdsActivity::class.java).apply {
                putExtra(EXTRAS_DATA, adsBean)
            }
            ActivityUtils.startActivity(intent)
        }
    }


    override fun init() {
        initViewModel()
        initViewModelObserve()
        initListener()
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        var adsBean: AdsBean = intent.getParcelableExtra(EXTRAS_DATA) as AdsBean
        adsViewModel = ViewModelProvider(
            this,
            AdsModelFactory(adsBean)
        ).get(
            AdsViewModel::class.java
        )
        adsViewModel.startInterval()
        if (ObjectUtils.isNotEmpty(adsBean)) {
            Glide.with(this)
                .load(adsBean.poster).into(binding.adsImg)
        }
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        adsViewModel.interval.observe(this, Observer {
            when (it) {
                -1L -> adsViewModel.doJump()
                else -> onIntervalChanged(it)
            }
        })

        adsViewModel.jump.observe(this, Observer {
            when (it) {
                HomeActivity::class.java -> HomeActivity.actionStart()
                LoginAtivity::class.java -> LoginAtivity.actionStart()
            }
            ActivityUtils.finishActivity(this)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        binding.adsImg.setOnClickListener {
            adsViewModel.bean.url?.let { it1 ->
                BaseWebViewActivity.actionStart(
                    this, adsViewModel.bean.name,
                    it1
                )
            }
        }
        binding.into.setOnClickListener {
            adsViewModel.doJump()
        }
    }

    private fun onIntervalChanged(second: Long) {
        binding.into.text = "跳过 ${second}s"
    }
}