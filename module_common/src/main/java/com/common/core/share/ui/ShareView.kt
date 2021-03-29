package com.common.core.share.ui

import android.content.Context
import android.view.View.OnClickListener
import com.common.R
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.databinding.LayoutShareBinding
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.toast
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:分享
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class ShareView(mContext: Context, mShare: ShareBean, mListener: OnShareListener) :
    BottomPopupView(mContext) {
    lateinit var mBinding: LayoutShareBinding

    enum class ShareType {
        WX,//微信
        WX_CIRCLE,//朋友圈
        REPORT,//举报
        CREDIT,//删除
    }

    var mlistener: OnShareListener = mListener
    var share: ShareBean = mShare
    override fun getImplLayoutId(): Int {
        return R.layout.layout_share
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = LayoutShareBinding.bind(popupImplView)
        clickListener(
            mBinding.wx,
            mBinding.wxCircle,
            mBinding.report,
            mBinding.credit,
            mBinding.cancel,
            clickListener = OnClickListener {
                when (it) {
                    mBinding.wx -> {
                        if (isWeixinAvilible()) {
                            dismiss()
                            mlistener.onItemClick(
                                it,
                                ShareType.WX
                            )
                        }
                    }
                    mBinding.wxCircle -> {
                        if (isWeixinAvilible()) {
                            mlistener.onItemClick(
                                it,
                                ShareType.WX_CIRCLE
                            )
                            dismiss()
                        }
                    }
                    mBinding.report -> {
                        dismiss()
                        mlistener.onItemClick(
                            it,
                            ShareType.REPORT
                        )
                    }
                    mBinding.credit -> {
                        dismiss()
                        mlistener.onItemClick(
                            it,
                            ShareType.CREDIT
                        )
                    }
                    mBinding.cancel -> dismiss()
                }
            }
        )
    }

    /**
     * 检测是否安装微信
     * @param activity
     * @return
     */
    private fun isWeixinAvilible(): Boolean {
        val packageManager = context.packageManager // 获取packagemanager
        val pinfo =
            packageManager.getInstalledPackages(0) // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (pn == "com.tencent.mm") {
                    return true
                }
            }
        }
        toast("请求先安装微信客户端再分享")
        return false
    }


}