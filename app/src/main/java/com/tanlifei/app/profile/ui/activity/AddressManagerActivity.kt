package com.tanlifei.app.profile.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.bean.UserBean
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.utils.extension.clickListener
import com.common.utils.extension.toast
import com.common.widget.TextInputHelper
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityAddressManngerBinding
import com.tanlifei.app.profile.bean.AreaBean
import com.tanlifei.app.profile.viewmodel.AddressViewModel


/**
 * @desc:收货地址
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class AddressManagerActivity : BaseFormActivity<ActivityAddressManngerBinding, AddressViewModel>() {


    private lateinit var mInputHelper: TextInputHelper
    var pvOptions: OptionsPickerView<AreaBean>? = null

    companion object {
        fun actionStart(user: UserBean) {
            var intent =
                Intent(ActivityUtils.getTopActivity(), AddressManagerActivity::class.java).apply {
                    putExtra(GlobalConst.Extras.BEAN, user)
                }
            ActivityUtils.startActivityForResult(ActivityUtils.getTopActivity(), intent, 2)
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
        mInputHelper = TextInputHelper(this, binding.enter)
        mInputHelper.addViews(
            binding.name,
            binding.phone,
            binding.areaDetails,
            binding.areaEdit
        )
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.loadingState.observe(this, this)
        viewModel.addressDataComplete.observe(this, Observer {
            binding.name.setText(viewModel.user.nickname)
            binding.name.setSelection(binding.name.text.length)
            binding.phone.setText(it.mobile)
            binding.email.setText(it.email)
            binding.area.text = it.addressPrefix
            binding.areaDetails.setText(it.address)
        })

        viewModel.areaDataComplete.observe(this, Observer {

            initAreaOptionPicker()
        })
        viewModel.editAddressComplete.observe(this, Observer {
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
        clickListener(binding.areaLayout, binding.enter,
            clickListener = View.OnClickListener {
                when (it) {
                    binding.areaLayout -> {
                        shopAreaOptionPicker()
                    }
                    binding.enter -> {
                        if (checkContent()) {
                            viewModel.requestEidtGoodsAddress()
                        }
                    }

                }
            })
    }

    private fun initData() {
        viewModel.requestGoodsAddress()
        viewModel.requestAreaJsonList()
    }


    /**
     * 地区条件选择器初始化，自定义布局
     *
     * @return
     */
    private fun initAreaOptionPicker(): OptionsPickerView<AreaBean>? {
        if (ObjectUtils.isNotEmpty(viewModel.options1Items) && ObjectUtils.isNotEmpty(viewModel.options2Items)
            && ObjectUtils.isNotEmpty(viewModel.options3Items)
        ) {
            pvOptions = OptionsPickerBuilder(
                this,
                OnOptionsSelectListener { options1: Int, options2: Int, options3: Int, _: View? ->
                    if (ObjectUtils.isNotEmpty(viewModel.addressBean)) {
                        viewModel.addressBean?.provinceId = viewModel.areaJsonList[options1].id
                        viewModel.addressBean?.cityId =
                            viewModel.areaJsonList[options1].areaListVOList[options2].id
                        viewModel.addressBean?.areaId =
                            viewModel.areaJsonList[options1].areaListVOList[options2]
                                .areaList[options3].id
                    }
                    viewModel.addressBean?.addressPrefix =
                        viewModel.areaJsonList[options1].name + " " +
                                viewModel.areaJsonList[options1].areaListVOList[options2]
                                    .name + " " +
                                viewModel.areaJsonList[options1].areaListVOList[options2]
                                    .areaList[options3].name
                    binding.area.text = viewModel.addressBean?.addressPrefix
                    binding.areaEdit.setText(binding.area.text)
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
                        pvOptions?.returnData()
                        pvOptions?.dismiss()
                    }
                    ivCancel.setOnClickListener { pvOptions?.dismiss() }
                }.setItemVisibleCount(4)
                .setLineSpacingMultiplier(2.2f)
                .setOutSideCancelable(true)
                .build<AreaBean>()

            pvOptions?.setPicker(
                viewModel.options1Items,
                viewModel.options2Items,
                viewModel.options3Items
            )
        }
        return pvOptions
    }


    /**
     * 信息检查
     *
     * @return
     */
    private fun checkContent(): Boolean {
        val userName: String = binding.name.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(userName)) {
            toast("收货人不能为空")
            return false
        }

        if (TextUtils.isEmpty(binding.phone.text.toString().trim { it <= ' ' })) {
            toast("联系方式不能为空")
            return false
        } else if (!RegexUtils.isMobileSimple(binding.phone.text)) {
            toast("请输入正确的手机号码")
            return false
        }

        if (ObjectUtils.isNotEmpty(
                binding.email.text.toString().trim { it <= ' ' }
            ) && !RegexUtils.isEmail(binding.email.text)
        ) {
            toast("请输入正确电子邮箱")
            return false
        }

        if (TextUtils.isEmpty(binding.area.text)) {
            toast("请选择所在地区")
            return false
        }
        if (TextUtils.isEmpty(binding.areaDetails.text.toString().trim { it <= ' ' })) {
            toast("请填写详细地址")
            return false
        }
        viewModel.addressBean?.username = userName
        viewModel.addressBean?.mobile = binding.phone.text.toString()
        viewModel.addressBean?.email = binding.email.text.toString()
        viewModel.addressBean?.address = binding.areaDetails.text.toString()
        return true
    }


    /**
     * 显示地区显示
     */
    private fun shopAreaOptionPicker() {
        if (ObjectUtils.isEmpty(pvOptions)) {
            initAreaOptionPicker()
        }
        pvOptions?.show()
    }

    override fun showSoftByEditViewIds(): IntArray {
        return return intArrayOf(R.id.name, R.id.phone, R.id.email, R.id.area_details)
    }


}