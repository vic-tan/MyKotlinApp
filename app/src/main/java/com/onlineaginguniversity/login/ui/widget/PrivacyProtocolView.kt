package com.onlineaginguniversity.login.ui.widget

import android.content.Context
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.iwgang.simplifyspan.SimplifySpanBuild
import cn.iwgang.simplifyspan.other.OnClickableSpanListener
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit
import cn.iwgang.simplifyspan.unit.SpecialTextUnit
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.common.base.listener.ComListener
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.widget.component.extension.*
import com.lxj.xpopup.core.CenterPopupView
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.common.constant.ComConst
import com.onlineaginguniversity.databinding.DialogPrivacyProtocolLayoutBinding


/**
 * @desc:隐私协议View
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class PrivacyProtocolView(
    mContext: Context,
    private val listener: ComListener.ViewClick
) :
    CenterPopupView(mContext) {
    lateinit var mBinding: DialogPrivacyProtocolLayoutBinding

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_privacy_protocol_layout
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = DialogPrivacyProtocolLayoutBinding.bind(popupImplView)
        mBinding.rootView.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            maxHeight
        )
        mBinding.tvContent.text = setContent().build()
        clickListener(
            mBinding.tvCancel,
            mBinding.tvConfirm,
            clickListener = OnClickListener { v ->
                when (v) {
                    mBinding.tvCancel -> {
                        dismiss()
                        ActivityUtils.finishAllActivities()
                    }
                    mBinding.tvConfirm -> {
                        SPUtils.getInstance().put(ComConst.SPKey.PRIVACY, false)
                        listener.click(v)
                        dismiss()
                    }
                }
            }
        )
    }

    override fun getMaxHeight(): Int {
        return (screenHeight * 0.6).toInt()
    }

    private fun setContent() = SimplifySpanBuild()
        .append(SpecialTextUnit(string(R.string.privacy_start)))
        .append(
            SpecialTextUnit(
                "《第三方SDK收集使用信息说明》",
                color(R.color.color_A47E68)
            )
                .setClickableUnit(
                    SpecialClickableUnit(
                        mBinding.tvContent,
                        OnClickableSpanListener { _, _ ->
                            BaseWebViewActivity.actionStart(
                                "隐私政策",
                                ApiUrlConst.URL_USER_SDK
                            )
                        }
                    )
                        .setPressBgColor(color(R.color.white))
                )
        )
        .append(string(R.string.privacy_center))
        .append(
            SpecialTextUnit("《用户协议》", color(R.color.color_A47E68))
                .setClickableUnit(
                    SpecialClickableUnit(
                        mBinding.tvContent,
                        OnClickableSpanListener { _, _ ->
                            BaseWebViewActivity.actionStart(
                                "用户协议",
                                ApiUrlConst.URL_USER_AGREEMENT
                            )
                        }
                    )
                        .setPressBgColor(color(R.color.white))
                )
        )
        .append("及")
        .append(
            SpecialTextUnit("《隐私政策》", color(R.color.color_A47E68))
                .setClickableUnit(
                    SpecialClickableUnit(
                        mBinding.tvContent,
                        OnClickableSpanListener
                        { _, _ ->
                            BaseWebViewActivity.actionStart(
                                "隐私政策",
                                ApiUrlConst.URL_PRIVATE_AGREEMENT
                            )
                        }
                    ).setPressBgColor(color(R.color.white))
                )
        )
        .append(string(R.string.privacy_end))
}