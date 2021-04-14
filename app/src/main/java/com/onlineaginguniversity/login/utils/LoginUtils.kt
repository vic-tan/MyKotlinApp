package com.onlineaginguniversity.login.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import cn.iwgang.simplifyspan.SimplifySpanBuild
import cn.iwgang.simplifyspan.other.OnClickableSpanListener
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit
import cn.iwgang.simplifyspan.unit.SpecialTextUnit
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SPUtils
import com.common.ComFun
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.ComDialogUtils
import com.common.widget.component.extension.*
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.common.constant.ComConst
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.utils.UserInfoUtils
import com.onlineaginguniversity.login.bean.WxLoginResultBean
import com.onlineaginguniversity.login.listener.OnKeyLoginListener
import com.onlineaginguniversity.login.ui.activity.BindInputPhoneAtivity
import com.onlineaginguniversity.login.ui.activity.LoginEntranceAtivity
import com.onlineaginguniversity.login.ui.activity.BindSIMPhoneAtivity
import com.onlineaginguniversity.login.ui.activity.SIMLoginAtivity
import com.onlineaginguniversity.main.ui.activity.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @desc: 登录工具类
 * @author: tanlifei
 * @date: 2021/4/9 13:52
 */
object LoginUtils {

    /**
     * 去登录界面
     */
    fun gotoLoginActivity() {
        OnKeyLoginUtils.checkEnvAvailable(object : OnKeyLoginListener.CheckEnvAvailable {
            override fun success() {
                SIMLoginAtivity.actionStart()
            }

            override fun failure() {
                LoginEntranceAtivity.actionStart()
            }
        })
    }

    /**
     * 登录完成
     *
     * @param token
     */
    fun loginSuccess(token: String) {
        ComFun.mToken = token
        UserInfoUtils.saveToken(token)
        MainActivity.actionStart()
    }

    /**
     * 校验手机号
     */
    fun checkPhone(phoneEdit: EditText): Boolean {
        if (phoneEdit.text.toString().isEmpty()) {
            toast("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileExact(getPhoneNumber(phoneEdit.text.toString()))) {
            toast("请输入正确的手机号码")
            return false
        }
        return true
    }

    /**
     * 校验验证码
     */
    fun checkCode(codeEdit: EditText): Boolean {
        if (codeEdit.text.toString().isEmpty()) {
            toast("请输入验证码")
            return false
        } else if (codeEdit.text.toString().length < 4) {
            toast("验证码最少4位")
            return false
        }
        return true
    }

    /**
     * 手机号344样格式化 如 13823297563 格式化为 138 2329 7563
     */
    fun phoneFormat(phoneEdit: EditText) {
        phoneEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                phoneEdit.setSelection(phoneEdit.text.toString().length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /**
             * 格式化手机号格式（三四四格式） 如：138 2132 3435
             */
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneFormatTextChanged(phoneEdit, s, count)
            }
        })
    }


    /**
     * 提示隐私协议
     */
    fun privacyDialog(context: Context) {
        var privacy = SPUtils.getInstance().getBoolean(ComConst.SPKey.PRIVACY, true)
        if (privacy) {
            ComFun.mHandler.postDelayed({
                ComDialogUtils.baseScrollContentDialog(
                    mContext = context,
                    content = string(R.string.privacy_txt),
                    cancelBtnText = "不同意并退出",
                    confirmBtnText = "我同意",
                    confirmListener = OnConfirmListener {
                        SPUtils.getInstance().put(ComConst.SPKey.PRIVACY, false)
                    },
                    cancelListener = OnCancelListener {
                        ActivityUtils.finishAllActivities()
                    }
                )
            }, 1500)
        }
    }


    /**
     * 是否选择协议
     */
    fun protocolCheck(protocolCheck: CheckBox, protocolPrompt: ImageView) {
        protocolCheck.setOnCheckedChangeListener { _, isChecked: Boolean ->
            if (isChecked) {
                protocolPrompt.gone()
            }
        }
    }

    /**
     * 延时隐藏提示
     */
    fun delayedProtocolPrompt(protocolPrompt: ImageView) {
        protocolPrompt.visible()
        ComFun.mHandler.postDelayed({
            protocolPrompt.gone()
        }, 1500)
    }

    /**
     * 微信登录结果
     */
    fun wxBind(resultBean: WxLoginResultBean) {
        //授权状态1=已授权2=已绑定手机号
        if (ObjectUtils.isNotEmpty(resultBean)) {
            //已绑定过手机,直接登录成功
            if (resultBean.status == 2) {
                resultBean.token?.let { token -> loginSuccess(token) }
            } else { //去绑定手机
                OnKeyLoginUtils.checkEnvAvailable(object : OnKeyLoginListener.CheckEnvAvailable {
                    override fun success() {
                        //一键绑定
                        resultBean.openid?.let { BindSIMPhoneAtivity.actionStart(it) }
                    }

                    override fun failure() {
                        //手机绑定
                        resultBean.openid?.let { BindInputPhoneAtivity.actionStart(it) }
                    }
                })
            }
        } else {
            toast("微信登录失败")
        }
    }

    /**
     * 计时器
     * @return
     */
    fun startTimer(smsCodeInterval: MutableLiveData<Long>, count: Long = 60) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {
                smsCodeInterval.value = EnumConst.TimerConst.START.value//倒计时过程禁止按钮点击
            }.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {
                    smsCodeInterval.value = t//倒计时
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    smsCodeInterval.value = EnumConst.TimerConst.COMPLETE.value//回复原来初始状态
                }
            })
    }


    /**
     * 倒计时开始时显示
     */
    fun onTimerStart(codeBtn: TextView) {
        codeBtn.isClickable = false //在发送数据的时候设置为不能点击
        codeBtn.setTextColor(color(R.color.txt_help))
    }

    /**
     * 倒计时正行中显示
     */

    fun onTimerChanged(codeBtn: TextView, second: Long) {
        codeBtn.text = "${second}s"
    }

    /**
     * 倒计时结束后显示
     */
    fun onTimerComplete(codeBtn: TextView) {
        codeBtn.isClickable = true
        codeBtn.setTextColor(color(R.color.theme))
        codeBtn.text = "重新获取"
    }

    /**
     * 显示切换环境显示的logo
     */
    fun synchEnvironmentLogo(logo: ImageView) {
        logo.setImageResource(EnvironmentUtils.appLogo())
    }

    /**
     * 格式式化输入手机
     */
    fun phoneFormatTextChanged(phone: EditText, s: CharSequence?, count: Int) {
        val length = s.toString().length
        s?.let {
            //删除数字
            if (count == 0) {
                if (length == 4) {
                    phone.setText(s.subSequence(0, 3))
                }
                if (length == 9) {
                    phone.setText(s.subSequence(0, 8))
                }
            }
            //添加数字
            if (count == 1) {
                if (length == 4) {
                    val part1 = s.subSequence(0, 3).toString()
                    val part2 = s.subSequence(3, length).toString()
                    phone.setText("$part1 $part2")
                }
                if (length == 9) {
                    val part1 = s.subSequence(0, 8).toString()
                    val part2 = s.subSequence(8, length).toString()
                    phone.setText("$part1 $part2")
                }
            }
            //复制粘贴
            if (count == 11) {
                val part1 = s.subSequence(0, 3).toString()
                val part2 = s.subSequence(3, 7).toString()
                val part3 = s.subSequence(7, length).toString()
                phone.setText("$part1 $part2 $part3")
            }
        }

    }

    /**
     * 去手机号344格式空格
     *
     * @param str
     * @return
     */
    fun getPhoneNumber(str: String): String {
        return str.replace(12288.toChar(), ' ').replace(" ", "").trim { it <= ' ' }
    }

    /**
     * 检验密码 需要8-20位，且含字母/数字
     */
    fun regexPwd(input: CharSequence?): Boolean {
        val regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$"
        return input != null && input.isNotEmpty() && Pattern.matches(regex, input)
    }

}