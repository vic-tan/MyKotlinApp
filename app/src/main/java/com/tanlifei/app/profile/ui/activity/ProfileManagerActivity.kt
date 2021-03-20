package com.tanlifei.app.profile.ui.activity

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
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.utils.GlideUtils
import com.common.utils.PermissionUtils
import com.common.utils.PictureSelectorUtils
import com.common.utils.extension.click
import com.common.utils.extension.clickListener
import com.common.utils.extension.toast
import com.common.widget.popup.BottomOptionsView
import com.hjq.bar.OnTitleBarListener
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.tanlifei.app.R
import com.tanlifei.app.common.event.UserEvent
import com.tanlifei.app.databinding.ActivityProfileManagerBinding
import com.tanlifei.app.profile.bean.AreaBean
import com.tanlifei.app.profile.bean.UniversityBean
import com.tanlifei.app.profile.viewmodel.ProfileViewModel
import org.greenrobot.eventbus.EventBus
import com.common.cofing.constant.GlobalConst.ActivityResult


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ProfileManagerActivity : BaseFormActivity<ActivityProfileManagerBinding, ProfileViewModel>() {

    var saveUrl = ""
    var areaOptions: OptionsPickerView<AreaBean>? = null
    var universityOptions: OptionsPickerView<UniversityBean>? = null

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
        mTitleBar.rightTitle = "保存"
    }

    override fun setTitleBarListener() {
        mTitleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View?) {
                ActivityUtils.finishActivity(mActivity)
            }

            override fun onRightClick(v: View?) {
                if (binding.nickname.text.trim().isEmpty()) {
                    toast("昵称不能为空")
                    return
                }
                mViewModel.mUserBean?.nickname = binding.nickname.text.toString()
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
        mViewModel.mLoadingState.observe(this, this)
        mViewModel.mRefreshUserInfo.observe(this, Observer { it ->
            GlideUtils.loadAvatar(mActivity, it.avatar, binding.userHead)
            binding.nickname.setText(it.nickname)
            if (it.isLecturer == 1) {
                binding.introduction.hint = "补充讲师介绍，让学员更了解你"
            }
            binding.nickname.setSelection(binding.nickname.text.length)
            binding.introduction.text = it.bio
            binding.area.text = it.address
            binding.school.text = it.universityName
            binding.sex.text = it.gender
            binding.age.text = it.age
            binding.address.text = if (it.goodsAddress == 0L) "" else "修改"
        })
        mViewModel.mAreaDataComplete.observe(this, Observer {
            initAreaOptionPicker()
        })
        mViewModel.mRefreshUniversityList.observe(this, Observer {
            initUniversityOptionPicker()
            if (ObjectUtils.isNotEmpty(it)) {
                binding.school.text = it[0].name
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
            binding.userHead,
            binding.introductionLayout,
            binding.areaLayout,
            binding.schoolLayout,
            binding.sexLayout,
            binding.ageLayout,
            binding.addressLayout,
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
                                                saveUrl = result[0]?.cutPath.toString()
                                                GlideUtils.load(
                                                    mActivity,
                                                    result[0]?.cutPath,
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
                    binding.introductionLayout -> {
                        IntroductionActivity.actionStart(mViewModel.mUserBean?.bio)
                    }
                    binding.areaLayout -> {
                        shopAreaOptionPicker()
                    }
                    binding.schoolLayout -> {
                        showUniversityDialog()
                    }
                    binding.sexLayout -> mViewModel.mUserBean?.let {
                        var optionView = BottomOptionsView(
                            mActivity,
                            mutableListOf("男", "女"),
                            OnSelectListener { _, text ->
                                binding.sex.text = text
                                mViewModel.mUserBean?.gender = text
                            }
                        )
                        XPopup.Builder(mActivity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .asCustom(optionView)
                            .show()
                    }
                    binding.ageLayout -> {
                        var optionView = BottomOptionsView(
                            mActivity,
                            mutableListOf("50以下", "50-60", "60-70", "70以上"),
                            OnSelectListener { _, text ->
                                binding.age.text = text
                                mViewModel.mUserBean?.age = text
                            }
                        )
                        XPopup.Builder(mActivity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .asCustom(optionView)
                            .show()
                    }
                    binding.addressLayout -> mViewModel.mUserBean?.let {
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
                        binding.area.text = mViewModel.mUserBean?.address
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
                mViewModel.mUserBean?.universityName = mViewModel.mUniversityOptionsItems[options1].name
                binding.school.text = mViewModel.mUserBean?.universityName
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
                        mViewModel.mUserBean?.bio = introduction;
                        binding.introduction.text = introduction
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
        return mutableListOf(binding.nickname, binding.introduction)
    }

}


