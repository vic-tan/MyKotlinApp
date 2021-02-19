package com.tanlifei.app.persenal.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.GlideUtils
import com.tanlifei.app.R
import com.tanlifei.app.databinding.FragmentPersonalBinding
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.tanlifei.app.persenal.activity.SettingActivity
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * @desc:我的
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class PersonalFragment : BaseLazyFragment<FragmentPersonalBinding>() {

    private lateinit var homeViewModel: HomeViewModel

    companion object {
        fun newInstance(): PersonalFragment {
            val args = Bundle()
            val fragment = PersonalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onFirstVisibleToUser() {
        binding.setting.setOnClickListener {
            SettingActivity.actionStart()
        }
        initViewModel()
        initViewModelObserve()
    }


    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            BaseViewModel.createViewModelFactory(HomeViewModel())
        ).get(
            HomeViewModel::class.java
        )
        homeViewModel.getUser()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        homeViewModel.refreshUserInfo.observe(this, Observer {
            binding.name.text = it.nickname
            binding.university.text = it.universityName
            GlideUtils.loadBlur(
                this.context,
                it.avatar,
                binding.persenalBg,
                R.mipmap.default_persenal_bg
            )
            GlideUtils.loadAvatar(this.context, it.avatar, binding.userCover)
        })
    }

}