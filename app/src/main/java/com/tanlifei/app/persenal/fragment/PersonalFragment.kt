package com.tanlifei.app.persenal.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.databinding.FragmentPersonalBinding
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.tanlifei.app.persenal.activity.SettingActivity

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
            Glide.with(this)
                .load(it.avatar).into(binding.userCover)

        })
    }

}