package com.common.core.share.ui

import android.content.Context
import android.view.View.OnClickListener
import com.common.R
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.databinding.LayoutShareBinding
import com.common.utils.extension.clickListener
import com.common.utils.extension.toast
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:分享
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class ShareView(context: Context, share: ShareBean, listener: OnShareListener) :
    BottomPopupView(context) {
    lateinit var binding: LayoutShareBinding

    enum class ShareType {
        WX,//微信
        WX_CIRCLE,//朋友圈
        REPORT,//举报
        CREDIT,//删除
    }

    var mlistener: OnShareListener = listener
    var share: ShareBean = share
    override fun getImplLayoutId(): Int {
        return R.layout.layout_share
    }

    override fun onCreate() {
        super.onCreate()
        binding = LayoutShareBinding.bind(popupImplView)
        clickListener(
            binding.wx,
            binding.wxCircle,
            binding.report,
            binding.credit,
            binding.cancel,
            clickListener = OnClickListener {
                when (it) {
                    binding.wx -> {
                        if (isWeixinAvilible()) {
                            dismiss()
                            mlistener.onItemClick(
                                it,
                                ShareType.WX
                            )
                        }
                    }
                    binding.wxCircle -> {
                        if (isWeixinAvilible()) {
                            mlistener.onItemClick(
                                it,
                                ShareType.WX_CIRCLE
                            )
                            dismiss()
                        }
                    }
                    binding.report -> {
                        dismiss()
                        mlistener.onItemClick(
                            it,
                            ShareType.REPORT
                        )
                    }
                    binding.credit -> {
                        dismiss()
                        mlistener.onItemClick(
                            it,
                            ShareType.CREDIT
                        )
                    }
                    binding.cancel -> dismiss()
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