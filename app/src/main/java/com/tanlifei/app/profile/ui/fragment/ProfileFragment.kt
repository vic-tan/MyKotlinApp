package com.tanlifei.app.profile.ui.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.AppUtils
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.base.ui.fragment.BaseLazyFragment
import com.common.base.viewmodel.EmptyViewModel
import com.common.utils.GlideUtils
import com.common.widget.component.extension.clickListener
import com.tanlifei.app.R
import com.tanlifei.app.common.constant.ApiUrlConst
import com.tanlifei.app.databinding.FragmentProfileBinding
import com.tanlifei.app.main.ui.activity.MainActivity
import com.tanlifei.app.main.viewmodel.MainViewModel
import com.tanlifei.app.profile.ui.activity.ManualActivity
import com.tanlifei.app.profile.ui.activity.ProfileManagerActivity
import com.tanlifei.app.profile.ui.activity.SettingActivity


/**
 * @desc:我的
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ProfileFragment : BaseLazyFragment<FragmentProfileBinding, EmptyViewModel>() {

    private lateinit var mHomeViewModel: MainViewModel


    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
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
        clickListener(
            mBinding.arrow,
            mBinding.setting,
            mBinding.recruitingLecturers,
            mBinding.score,
            mBinding.optManual,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.arrow -> ProfileManagerActivity.actionStart()
                    mBinding.setting -> SettingActivity.actionStart()
                    mBinding.recruitingLecturers -> gotoWeb(
                        "讲师入驻入口",
                        ApiUrlConst.URL_LECTURER_ASKFOR
                    )
                    mBinding.score -> launchAppDetail()
                    mBinding.optManual -> ManualActivity.actionStart()
                }
            }
        )
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        mHomeViewModel = (activity as MainActivity).mViewModel
        mHomeViewModel.getUser()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mHomeViewModel.mRefreshUserInfo.observe(this, Observer {
            mBinding.name.text = it.nickname
            mBinding.university.text = it.universityName
            GlideUtils.loadBlur(
                this.context,
                it.avatar,
                mBinding.persenalBg,
                R.mipmap.bg_profile_default
            )
            GlideUtils.loadAvatar(this.context, it.avatar, mBinding.userCover)
        })
    }


    /**
     * 启动到应用商店app详情界面
     * 某些应用商店可能会失败
     */
    private fun launchAppDetail() {
        try {
            val uri: Uri = Uri.parse("market://details?id=${AppUtils.getAppPackageName()}")
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