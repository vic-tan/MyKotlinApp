package com.onlineaginguniversity.login.ui.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.utils.ComDialogUtils
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.*
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.ActivitySetPasswordBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:找回密码\修改密码界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class SetPasswordAtivity :
    BaseToolBarActivity<ActivitySetPasswordBinding, LoginViewModel>(), TextWatcher {

    private lateinit var mInputHelper: TextInputHelper
    private lateinit var type: EnumConst.SMSType
    private var phoneNumber: String = ""

    companion object {
        fun actionStart(phoneNumber: String, type: EnumConst.SMSType) {
            startActivity<SetPasswordAtivity> {
                putExtra(GlobalConst.Extras.NAME, phoneNumber)
                putExtra(GlobalConst.Extras.TYPE, type)
            }
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        phoneNumber = intent.getStringExtra(GlobalConst.Extras.NAME).toString()
        type = intent.getSerializableExtra(GlobalConst.Extras.TYPE) as EnumConst.SMSType
        mBinding.hint.text = "短信验证码已发送至 ${phoneNumber}，新密码需要8-20位，至少含字母/数字"
        mTitleBar.lineView.gone()
        mBinding.pwd.addTextChangedListener(this)
        initViewModelObserve()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        LoginUtils.synchEnvironmentLogo(mBinding.logo)
    }


    private fun initData() {
        initTextInputHelper()
        mViewModel.requestSmsCode(LoginUtils.getPhoneNumber(phoneNumber), type)
    }

    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(this, mBinding.enter)
        mInputHelper.addViews(mBinding.code, mBinding.pwd)
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mCodeTimer.observe(this, Observer {
            when (it) {
                EnumConst.TimerConst.START.value -> LoginUtils.onTimerStart(mBinding.codeBtn)
                EnumConst.TimerConst.COMPLETE.value -> LoginUtils.onTimerComplete(mBinding.codeBtn)
                else -> LoginUtils.onTimerChanged(mBinding.codeBtn, it)
            }
        })

        mViewModel.mPwdLoginResult.observe(this, Observer {
            if (ObjectUtils.isNotEmpty(it)) {
                if (it.status == 0) {
                    it.token?.let { token -> LoginUtils.loginSuccess(token) }
                } else if (it.status == -1) {
                    when (it.count) {
                        1 -> {
                            mBinding.errorText.text = "账号或密码错误"
                            mBinding.errorText.visible()
                        }
                        in 2..5 -> {
                            mBinding.errorText.text =
                                "账号或密码错误，还有${6 - it.count}次机会"
                            mBinding.errorText.visible()
                        }
                        -1 -> {
                            mBinding.errorText.gone()
                            var content = "账号或密码错误已超过5次， \n请在${it.expireTime}分钟后重试？"
                            ComDialogUtils.showMultDialogByNoTitle(this@SetPasswordAtivity,
                                content,
                                "忘记密码",
                                OnConfirmListener {

                                })
                        }
                    }
                }
            } else {
                toast("登录失败")
            }
        })

        mViewModel.mIsToken.observe(this, Observer {
            mViewModel.mToken?.let { it -> LoginUtils.loginSuccess(it) }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.enter,
            mBinding.notReceivedCode,
            mBinding.codeBtn,
            mBinding.voiceCode,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.enter -> {
                        login()
                    }
                    mBinding.notReceivedCode -> {
                        ComDialogUtils.showComPrompt(
                            this@SetPasswordAtivity,
                            "收不到验证码?",
                            "1.手机号码是否输入正确\n2.检查手机是否停机\n3.请使用其他账号登录"
                        )
                    }
                    mBinding.codeBtn -> {
                        mViewModel.requestSmsCode(
                            LoginUtils.getPhoneNumber(phoneNumber),
                            type
                        )
                    }
                    mBinding.voiceCode -> {
                        mViewModel.requestVoiceCode(
                            LoginUtils.getPhoneNumber(phoneNumber),
                            type
                        )
                    }
                }
            }
        )
    }

    fun login() {
        if (ObjectUtils.isEmpty(mBinding.code.text.toString())) {
            toast("请输入验证码")
            return
        }
        if (ObjectUtils.isEmpty(mBinding.pwd.text.toString())) {
            toast("请输入密码")
            return
        }

        if (mBinding.pwd.text.toString().length < 8) {
            mBinding.errorText.text = "新密码需要8-20位"
            mBinding.errorText.visible()
            return
        }
        if (!LoginUtils.regexPwd(
                mBinding.pwd.text.toString()
            )
        ) {
            mBinding.errorText.text = "新密码需要8-20位，且含字母/数字"
            mBinding.errorText.visible()
            return
        }
        mBinding.errorText.gone()
        mViewModel.requestSetPwd(
            LoginUtils.getPhoneNumber(phoneNumber),
            mBinding.code.text.toString(),
            mBinding.pwd.text.toString(),
            type
        )
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.code, mBinding.pwd)
    }

    override fun afterTextChanged(s: Editable?) {
        if (s.toString().isEmpty()) {
            mBinding.errorText.gone()
            return
        }
        if (s.toString().length < 8) {
            mBinding.errorText.visible()
            return
        } else {
            if (!LoginUtils.regexPwd(mBinding.pwd.text.toString())) {
                mBinding.errorText.visible()
                return
            } else {
                mBinding.errorText.gone()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}


