package com.tanlifei.app.profile.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.utils.GlideUtils
import com.common.utils.ViewUtils
import com.tanlifei.app.R
import com.tanlifei.app.common.config.api.ApiConst
import com.tanlifei.app.databinding.FragmentProfileBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.tanlifei.app.profile.activity.ManualActivity
import com.tanlifei.app.profile.activity.SettingActivity


/**
 * @desc:我的
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ProfileFragment : BaseLazyFragment<FragmentProfileBinding>(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel

    companion object {
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onFirstVisibleToUser() {
        initViewModel()
        initViewModelObserve()
        initListener()
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        ViewUtils.setOnClickListener(
            this,
            binding.setting,
            binding.recruitingLecturers,
            binding.score,
            binding.optManual
        )
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        homeViewModel = (activity as HomeActivity).viewModel
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
                R.mipmap.bg_profile_default
            )
            GlideUtils.loadAvatar(this.context, it.avatar, binding.userCover)
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.setting -> SettingActivity.actionStart()
            binding.recruitingLecturers -> gotoWeb("讲师入驻入口", ApiConst.URL_LECTURER_ASKFOR)
            binding.score -> launchAppDetail()
            binding.optManual -> ManualActivity.actionStart()
        }
    }

    /**
     * 启动到应用商店app详情界面
     * 某些应用商店可能会失败
     */
    private fun launchAppDetail() {
        try {
            val uri: Uri = Uri.parse("market://details?id=com.onlineaginguniversity")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * webView查看协议
     */
    private fun gotoWeb(title: String?, url: String) {
        BaseWebViewActivity.actionStart(title, url)
    }

}