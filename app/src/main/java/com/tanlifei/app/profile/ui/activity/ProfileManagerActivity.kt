package com.tanlifei.app.profile.ui.activity

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.utils.GlideUtils
import com.common.utils.PermissionUtils
import com.common.utils.PictureSelectorUtils
import com.common.utils.extension.clickListener
import com.common.widget.popup.BottomOptionView
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityProfileManagerBinding
import com.tanlifei.app.profile.viewmodel.ProfileViewModel


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ProfileManagerActivity : BaseFormActivity<ActivityProfileManagerBinding, ProfileViewModel>() {


    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(ProfileManagerActivity::class.java)
        }
    }

    override fun createViewModel(): ProfileViewModel {
        return ProfileViewModel()
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
        titleBar.rightTitle = "保存"
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.refreshUserInfo.observe(this, Observer { it ->
            GlideUtils.loadAvatar(mActivity, it.avatar, binding.userHead)
            binding.nickname.setText(it.nickname)
            binding.introduction.setText(it.bio)
            binding.area.text = it.address
            binding.school.text = it.universityName
            binding.sex.text = it.gender
            binding.age.text = it.age
            binding.address.text = if (it.goodsAddress == 0L) "" else "修改"
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            binding.userHead,
            binding.addressLayout,
            binding.sexLayout,
            binding.ageLayout,
            clickListener = View.OnClickListener { v ->
                when (v) {
                    binding.userHead -> {
                        PermissionUtils.requestCameraPermission(
                            this,
                            callback = object : PermissionUtils.PermissionCallback {
                                override fun allGranted() {
                                    PictureSelectorUtils.createAvatarCrop(mActivity)
                                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                            override fun onResult(result: List<LocalMedia?>) {
                                                GlideUtils.load(
                                                    mActivity,
                                                    result[0]?.compressPath,
                                                    binding.userHead
                                                )
                                            }

                                            override fun onCancel() {
                                            }
                                        })
                                }
                            }
                        )

                    }
                    binding.addressLayout -> viewModel.userBean?.let {
                        AddressManagerActivity.actionStart(it)
                    }
                    binding.sexLayout -> viewModel.userBean?.let {
                        var optionView = BottomOptionView(
                            mActivity,
                            mutableListOf("男", "女"),
                            OnSelectListener { _, text -> binding.sex.text = text }
                        )
                        XPopup.Builder(mActivity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .asCustom(optionView)
                            .show()
                    }
                    binding.ageLayout -> {
                        var optionView = BottomOptionView(
                            mActivity,
                            mutableListOf("50以下", "50-60", "60-70", "70以上"),
                            OnSelectListener { _, text -> binding.age.text = text }
                        )
                        XPopup.Builder(mActivity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .asCustom(optionView)
                            .show()
                    }
                }
            })
    }

    private fun initData() {
        viewModel.requestUser()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                2 -> {//收货地址
                    if (ObjectUtils.isNotEmpty(data)) {
                        viewModel.userBean?.goodsAddress =
                            data!!.getIntExtra(GlobalConst.Extras.ID, 0).toLong()
                    }
                }
            }
        }
    }


    override fun showSoftByEditViewIds(): IntArray {
        return return intArrayOf(R.id.nickname, R.id.introduction)
    }


}


