package com.onlineaginguniversity.profile.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.constant.GlobalConst
import com.common.constant.GlobalConst.ActivityResult
import com.common.base.bean.UserBean
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.widget.component.extension.click
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.startActivityForResult
import com.common.widget.component.extension.toast
import com.common.widget.TextInputHelper
import com.onlineaginguniversity.R
import com.onlineaginguniversity.databinding.ActivityAddressManngerBinding
import com.onlineaginguniversity.profile.bean.AreaBean
import com.onlineaginguniversity.profile.viewmodel.AddressViewModel


/**
 * @desc:收货地址
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class AddressManagerActivity : BaseToolBarActivity<ActivityAddressManngerBinding, AddressViewModel>() {


    private lateinit var mInputHelper: TextInputHelper
    var mPvOptions: OptionsPickerView<AreaBean>? = null

    companion object {
        fun actionStart(user: UserBean) {
            startActivityForResult<AddressManagerActivity>(ActivityResult.REQUEST_CODE_2) {
                putExtra(GlobalConst.Extras.BEAN, user)
            }
        }
    }

    override fun createViewModel(): AddressViewModel {
        var user: UserBean = intent.getSerializableExtra(GlobalConst.Extras.BEAN) as UserBean
        return AddressViewModel(user)
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
        initTextInputHelper()
    }


    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(this, mBinding.enter)
        mInputHelper.addViews(
            mBinding.name,
            mBinding.phone,
            mBinding.areaDetails,
            mBinding.areaEdit
        )
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        uiChangeObserve()
        mViewModel.mAddressDataComplete.observe(this, Observer {
            mBinding.name.setText(mViewModel.mUser.nickname)
            mBinding.name.setSelection(mBinding.name.text.length)
            mBinding.phone.setText(it.mobile)
            mBinding.email.setText(it.email)
            mBinding.area.text = it.addressPrefix
            mBinding.areaDetails.setText(it.address)
        })

        mViewModel.mAreaDataComplete.observe(this, Observer {

            initAreaOptionPicker()
        })
        mViewModel.mEditAddressComplete.observe(this, Observer {
            toast("保存成功")
            val i = Intent()
            i.putExtra(GlobalConst.Extras.ID, it)
            setResult(RESULT_OK, i)
            ActivityUtils.finishActivity(this)
        })

    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(mBinding.areaLayout, mBinding.enter,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.areaLayout -> {
                        shopAreaOptionPicker()
                    }
                    mBinding.enter -> {
                        if (checkContent()) {
                            mViewModel.requestEidtGoodsAddress()
                        }
                    }

                }
            })
    }

    private fun initData() {
        mViewModel.requestGoodsAddress()
        mViewModel.requestAreaJsonList()
    }


    /**
     * 地区条件选择器初始化，自定义布局
     *
     * @return
     */
    private fun initAreaOptionPicker(): OptionsPickerView<AreaBean>? {
        if (ObjectUtils.isNotEmpty(mViewModel.mOptions1Items) && ObjectUtils.isNotEmpty(mViewModel.mOptions2Items)
            && ObjectUtils.isNotEmpty(mViewModel.mOptions3Items)
        ) {
            mPvOptions = OptionsPickerBuilder(
                this,
                OnOptionsSelectListener { options1: Int, options2: Int, options3: Int, _: View? ->
                    if (ObjectUtils.isNotEmpty(mViewModel.mAddressBean)) {
                        mViewModel.mAddressBean?.provinceId = mViewModel.mAreaJsonList[options1].id
                        mViewModel.mAddressBean?.cityId =
                            mViewModel.mAreaJsonList[options1].areaListVOList[options2].id
                        mViewModel.mAddressBean?.areaId =
                            mViewModel.mAreaJsonList[options1].areaListVOList[options2]
                                .areaList[options3].id
                    }
                    mViewModel.mAddressBean?.addressPrefix =
                        mViewModel.mAreaJsonList[options1].name + " " +
                                mViewModel.mAreaJsonList[options1].areaListVOList[options2]
                                    .name + " " +
                                mViewModel.mAreaJsonList[options1].areaListVOList[options2]
                                    .areaList[options3].name
                    mBinding.area.text = mViewModel.mAddressBean?.addressPrefix
                    mBinding.areaEdit.setText(mBinding.area.text)
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
                        mPvOptions?.returnData()
                        mPvOptions?.dismiss()
                    }
                    ivCancel.click { mPvOptions?.dismiss() }
                    optLayout.click { }
                }.setItemVisibleCount(4)
                .setLineSpacingMultiplier(2.2f)
                .setOutSideCancelable(true)
                .build<AreaBean>()

            mPvOptions?.setPicker(
                mViewModel.mOptions1Items,
                mViewModel.mOptions2Items,
                mViewModel.mOptions3Items
            )
        }
        return mPvOptions
    }


    /**
     * 信息检查
     *
     * @return
     */
    private fun checkContent(): Boolean {
        val userName: String = mBinding.name.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(userName)) {
            toast("收货人不能为空")
            return false
        }

        if (TextUtils.isEmpty(mBinding.phone.text.toString().trim { it <= ' ' })) {
            toast("联系方式不能为空")
            return false
        } else if (!RegexUtils.isMobileSimple(mBinding.phone.text)) {
            toast("请输入正确的手机号码")
            return false
        }

        if (ObjectUtils.isNotEmpty(
                mBinding.email.text.toString().trim { it <= ' ' }
            ) && !RegexUtils.isEmail(mBinding.email.text)
        ) {
            toast("请输入正确电子邮箱")
            return false
        }

        if (TextUtils.isEmpty(mBinding.area.text)) {
            toast("请选择所在地区")
            return false
        }
        if (TextUtils.isEmpty(mBinding.areaDetails.text.toString().trim { it <= ' ' })) {
            toast("请填写详细地址")
            return false
        }
        mViewModel.mAddressBean?.username = userName
        mViewModel.mAddressBean?.mobile = mBinding.phone.text.toString()
        mViewModel.mAddressBean?.email = mBinding.email.text.toString()
        mViewModel.mAddressBean?.address = mBinding.areaDetails.text.toString()
        return true
    }


    /**
     * 显示地区显示
     */
    private fun shopAreaOptionPicker() {
        if (ObjectUtils.isEmpty(mPvOptions)) {
            initAreaOptionPicker()
        }
        mPvOptions?.show()
    }


    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.name, mBinding.phone, mBinding.email, mBinding.areaDetails)
    }


}