package com.onlineaginguniversity.profile.ui.activity

import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.constant.GlobalConst.ActivityResult
import com.common.utils.GlideUtils
import com.common.utils.PermissionUtils
import com.common.utils.PictureSelectorUtils
import com.common.widget.component.extension.click
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.startActivity
import com.common.widget.component.extension.toast
import com.common.widget.component.popup.BottomOptionsView
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.event.UserEvent
import com.onlineaginguniversity.common.utils.HuaweiUploadManager
import com.onlineaginguniversity.databinding.ActivityProfileManagerBinding
import com.onlineaginguniversity.databinding.ActivityProfileSettingBinding
import com.onlineaginguniversity.profile.bean.AreaBean
import com.onlineaginguniversity.profile.bean.UniversityBean
import com.onlineaginguniversity.profile.viewmodel.ProfileViewModel
import org.greenrobot.eventbus.EventBus


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ProfileSettingActivity :
    BaseToolBarActivity<ActivityProfileSettingBinding, ProfileViewModel>() {

    var saveUrl = ""
    var areaOptions: OptionsPickerView<AreaBean>? = null
    var universityOptions: OptionsPickerView<UniversityBean>? = null

    companion object {
        fun actionStart() {
            startActivity<ProfileSettingActivity> { }
        }
    }

    override fun createViewModel(): ProfileViewModel {
        return ProfileViewModel()
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
        mTitleBar.rightTitle = "保存"
    }

    override fun setTitleBarListener() {
        mTitleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View?) {
                ActivityUtils.finishActivity(mActivity)
            }

            override fun onRightClick(v: View?) {
                if (mBinding.nickname.text.trim().isEmpty()) {
                    toast("昵称不能为空")
                    return
                }
                mViewModel.mUserBean?.nickname = mBinding.nickname.text.toString()
                if (saveUrl.isEmpty()) {
                    mViewModel.requestUpdateUser()
                } else {
                    mViewModel.saveUser(saveUrl)
                }
            }

            override fun onTitleClick(v: View?) {
            }

        })
    }

    /**
     * 显示地区显示
     */
    private fun shopAreaOptionPicker() {
        if (ObjectUtils.isEmpty(areaOptions)) {
            initAreaOptionPicker()
        }
        areaOptions?.show()
    }

    /**
     * 显示地区学校显示
     */
    private fun showUniversityDialog() {
        if (ObjectUtils.isEmpty(universityOptions)) {
            initUniversityOptionPicker()
        }
        if (ObjectUtils.isNotEmpty(universityOptions)) {
            universityOptions?.show()
        }
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        uiChangeObserve()
        mViewModel.mRefreshUserInfo.observe(this, Observer { it ->
            GlideUtils.loadAvatar(mActivity, it.avatar, mBinding.userHead)
            mBinding.nickname.setText(it.nickname)
            if (it.isLecturer == 1) {
                mBinding.introduction.hint = "补充讲师介绍，让学员更了解你"
            }
            mBinding.nickname.setSelection(mBinding.nickname.text.length)
            mBinding.introduction.text = it.bio
            mBinding.area.text = it.address
            mBinding.school.text = it.universityName
            mBinding.sex.text = it.gender
            mBinding.age.text = it.age
            mBinding.address.text = if (it.goodsAddress == 0L) "" else "修改"
        })
        mViewModel.mAreaDataComplete.observe(this, Observer {
            initAreaOptionPicker()
        })
        mViewModel.mRefreshUniversityList.observe(this, Observer {
            initUniversityOptionPicker()
            if (ObjectUtils.isNotEmpty(it)) {
                mBinding.school.text = it[0].name
            }
        })
        mViewModel.mDataChanged.observe(this, Observer {
            EventBus.getDefault().post(
                UserEvent(it)
            )
            ActivityUtils.finishActivity(this)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.userHead,
            mBinding.introductionLayout,
            mBinding.areaLayout,
            mBinding.schoolLayout,
            mBinding.sexLayout,
            mBinding.ageLayout,
            mBinding.addressLayout,
            clickListener = View.OnClickListener { v ->
                when (v) {
                    mBinding.userHead -> {
                        PermissionUtils.requestCameraPermission(
                            this,
                            callback = object : PermissionUtils.PermissionCallback {
                                override fun allGranted() {
                                    PictureSelectorUtils.createAvatarCrop(mActivity)
                                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                            override fun onResult(result: List<LocalMedia?>) {
                                                saveUrl = result[0]?.cutPath.toString()
                                                GlideUtils.load(
                                                    mActivity,
                                                    result[0]?.cutPath,
                                                    mBinding.userHead
                                                )
                                            }

                                            override fun onCancel() {
                                            }
                                        })
                                }
                            }
                        )

                    }
                    mBinding.introductionLayout -> {
                        IntroductionActivity.actionStart(mViewModel.mUserBean?.bio)
                    }
                    mBinding.areaLayout -> {
                        shopAreaOptionPicker()
                    }
                    mBinding.schoolLayout -> {
                        showUniversityDialog()
                    }
                    mBinding.sexLayout -> mViewModel.mUserBean?.let {
                        var optionView = BottomOptionsView(
                            mActivity,
                            mutableListOf("男", "女"),
                            OnSelectListener { _, text ->
                                mBinding.sex.text = text
                                mViewModel.mUserBean?.gender = text
                            }
                        )
                        XPopup.Builder(mActivity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .asCustom(optionView)
                            .show()
                    }
                    mBinding.ageLayout -> {
                        var optionView = BottomOptionsView(
                            mActivity,
                            mutableListOf("50以下", "50-60", "60-70", "70以上"),
                            OnSelectListener { _, text ->
                                mBinding.age.text = text
                                mViewModel.mUserBean?.age = text
                            }
                        )
                        XPopup.Builder(mActivity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .asCustom(optionView)
                            .show()
                    }
                    mBinding.addressLayout -> mViewModel.mUserBean?.let {
                        AddressManagerActivity.actionStart(it)
                    }

                }
            })
    }


    /**
     * 地区条件选择器初始化，自定义布局
     *
     * @return
     */
    private fun initAreaOptionPicker(): OptionsPickerView<AreaBean>? {
        if (ObjectUtils.isNotEmpty(mViewModel.mOptions1Items) && ObjectUtils.isNotEmpty(mViewModel.mOptions2Items)) {
            areaOptions = OptionsPickerBuilder(
                this,
                OnOptionsSelectListener { options1: Int, options2: Int, _: Int, _: View? ->
                    val newAreaId =
                        mViewModel.mAreaJsonList[options1].areaListVOList[options2].id
                    if (mViewModel.mUserBean?.areaId != newAreaId) {
                        mViewModel.requestUniversity(newAreaId)
                        mViewModel.mUserBean?.address =
                            "${mViewModel.mAreaJsonList[options1].name},${mViewModel.mAreaJsonList[options1].areaListVOList[options2].name}"
                        mViewModel.mUserBean?.areaId = newAreaId
                        mBinding.area.text = mViewModel.mUserBean?.address
                    }
                }
            )
                .setLayoutRes(
                    R.layout.pickerview_custom_options
                ) { v: View ->
                    val tvSubmit =
                        v.findViewById<View>(R.id.tv_finish) as TextView
                    val ivCancel =
                        v.findViewById<View>(R.id.iv_cancel) as TextView
                    val optLayout =
                        v.findViewById<View>(R.id.opt_layout) as RelativeLayout
                    tvSubmit.click {
                        areaOptions?.returnData()
                        areaOptions?.dismiss()
                    }
                    ivCancel.click { areaOptions?.dismiss() }
                    optLayout.click { }
                }.setItemVisibleCount(4)
                .setLineSpacingMultiplier(2.2f)
                .setOutSideCancelable(true)
                .build<AreaBean>()

            areaOptions?.setPicker(
                mViewModel.mOptions1Items,
                mViewModel.mOptions2Items
            )
        }
        return areaOptions
    }


    /**
     * 学校条件选择器初始化，自定义布局
     *
     * @return
     */
    private fun initUniversityOptionPicker(): OptionsPickerView<UniversityBean>? {
        universityOptions = OptionsPickerBuilder(
            this,
            OnOptionsSelectListener { options1: Int, _: Int, _: Int, _: View? ->
                mViewModel.mUserBean?.university = mViewModel.mUniversityOptionsItems[options1].id
                mViewModel.mUserBean?.universityName =
                    mViewModel.mUniversityOptionsItems[options1].name
                mBinding.school.text = mViewModel.mUserBean?.universityName
            }
        )
            .setLayoutRes(
                R.layout.pickerview_custom_options
            ) { v: View ->
                val tvSubmit =
                    v.findViewById<View>(R.id.tv_finish) as TextView
                val ivCancel =
                    v.findViewById<View>(R.id.iv_cancel) as TextView
                tvSubmit.setOnClickListener {
                    universityOptions?.returnData()
                    universityOptions?.dismiss()
                }
                ivCancel.setOnClickListener { _ -> universityOptions?.dismiss() }
            }
            .setItemVisibleCount(4)
            .setLineSpacingMultiplier(2.2f)
            .setOutSideCancelable(true)
            .build<UniversityBean>()
        universityOptions?.setPicker(mViewModel.mUniversityOptionsItems)
        return universityOptions
    }

    private fun initData() {
        mViewModel.requestUser()
        mViewModel.requestAreaJsonList()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ActivityResult.REQUEST_CODE_1 ->//简介
                    if (ObjectUtils.isNotEmpty(data)) {
                        var introduction = data!!.getStringExtra(GlobalConst.Extras.CONTENT)
                        if (ObjectUtils.isNotEmpty(introduction)) {
                            mViewModel.mUserBean?.bio = introduction!!
                            mBinding.introduction.text = introduction
                        }
                    }
                ActivityResult.REQUEST_CODE_2 -> {//收货地址
                    if (ObjectUtils.isNotEmpty(data)) {
                        mViewModel.mUserBean?.goodsAddress =
                            data!!.getIntExtra(GlobalConst.Extras.ID, 0).toLong()
                    }
                }
            }
        }
    }


    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.nickname, mBinding.introduction)
    }

    override fun onDestroy() {
        super.onDestroy()
        HuaweiUploadManager().cancleJob()
    }

}


