package com.common.core.share.ui

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.databinding.LayoutBottomEditViewBinding
import com.common.databinding.LayoutShareBinding
import com.common.utils.AntiShakeUtils
import com.common.utils.ViewUtils
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:分享
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class ShareView(context: Context, share: ShareBean, listener: OnShareListener) :
    BottomPopupView(context),
    View.OnClickListener {
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
        ViewUtils.setOnClickListener(
            this,
            binding.wx,
            binding.wxCircle,
            binding.report,
            binding.credit,
            binding.cancel
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
        ToastUtils.show("请求先安装微信客户端再分享")
        return false
    }


    override fun onClick(v: View) {
        if (ObjectUtils.isEmpty(binding)) {
            return
        }
        if (AntiShakeUtils.isInvalidClick(v)) return
        when (v) {
            binding.wx -> {
                if (isWeixinAvilible()) {
                    dismiss()
                    mlistener.onItemClick(
                        v,
                        ShareType.WX
                    )
                }
            }
            binding.wxCircle -> {
                if (isWeixinAvilible()) {
                    mlistener.onItemClick(
                        v,
                        ShareType.WX_CIRCLE
                    )
                    dismiss()
                }
            }
            binding.report -> {
                dismiss()
                mlistener.onItemClick(
                    v,
                    ShareType.REPORT
                )
            }
            binding.credit -> {
                dismiss()
                mlistener.onItemClick(
                    v,
                    ShareType.CREDIT
                )
            }
            binding.cancel -> dismiss()

        }
    }


}