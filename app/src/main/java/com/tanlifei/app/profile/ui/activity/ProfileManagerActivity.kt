package com.tanlifei.app.profile.ui.activity

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.utils.GlideEngine
import com.common.utils.GlideUtils
import com.common.utils.PictureSelectorManager
import com.common.utils.ResUtils
import com.common.utils.extension.clickListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityProfileManagerBinding
import com.tanlifei.app.profile.viewmodel.ProfileViewModel


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ProfileManagerActivity : BaseFormActivity<ActivityProfileManagerBinding, ProfileViewModel>(),
    View.OnClickListener {


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
        viewModel.refreshUserInfo.observe(this, Observer {

        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(this, binding.userHead, binding.addressLayout, binding.sexLayout)
    }

    private fun initData() {
        viewModel.requestUser()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.userHead -> {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage()).imageEngine(GlideEngine)
                    .setPictureUIStyle(PictureSelectorManager.getPictureUIStyle())
                    .isCompress(true)
                    .isEnableCrop(true)
                    .maxSelectNum(1)
                    .circleDimmedLayer(true)
                    .setCropDimmedColor(ResUtils.getColor(R.color.com_blur_shadow))//设置圆形裁剪背景色值
                    .setCircleDimmedBorderColor(ResUtils.getColor(R.color.white))//设置圆形裁剪边框色值
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: List<LocalMedia?>) {
                            GlideUtils.load(mActivity, result[0]?.compressPath, binding.userHead)
                        }

                        override fun onCancel() {
                            // onCancel Callback
                        }
                    })
            }
            binding.addressLayout -> viewModel.userBean?.let { AddressManagerActivity.actionStart(it) }
            binding.sexLayout -> viewModel.userBean?.let { AddressManagerActivity.actionStart(it) }
        }
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


