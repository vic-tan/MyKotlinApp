package com.onlineaginguniversity.login.utils

import android.widget.TextView
import cn.iwgang.simplifyspan.SimplifySpanBuild
import cn.iwgang.simplifyspan.other.OnClickableSpanListener
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit
import cn.iwgang.simplifyspan.unit.SpecialTextUnit
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.widget.component.extension.color
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.constant.ApiUrlConst

/**
 * @desc:协议
 * @author: tanlifei
 * @date: 2021/4/14 15:41
 */
object ProtocolTxtUtils {

    /**
     *勾选表示已阅读并同意《用户协议》和《隐私政策》
     */
    fun loginProtocolTxt(protocolView: TextView) {
        val titleBuild = SimplifySpanBuild()
        titleBuild.apply {
            append(
                SpecialTextUnit("勾选表示已阅读并同意").setTextColor(
                    color(R.color.txt_sub)
                )
            )
            append(
                SpecialTextUnit("《用户协议》", color(R.color.color_A47E68))
                    .setClickableUnit(
                        SpecialClickableUnit(
                            protocolView,
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
            append("和")
            append(
                SpecialTextUnit("《隐私政策》", color(R.color.color_A47E68))
                    .setClickableUnit(
                        SpecialClickableUnit(
                            protocolView,
                            OnClickableSpanListener { _, _ ->
                                BaseWebViewActivity.actionStart(
                                    "隐私政策",
                                    ApiUrlConst.URL_PRIVATE_AGREEMENT
                                )
                            }
                        ).setPressBgColor(color(R.color.white))
                    )
            )
        }
        protocolView.text = titleBuild.build()
    }
}