package com.onlineaginguniversity.common.widget.component.share.ui

import android.content.Context
import android.view.View.OnClickListener
import androidx.lifecycle.*
import com.blankj.utilcode.util.ObjectUtils
import com.common.constant.GlobalEnumConst.ShareType
import com.common.constant.GlobalEnumConst.ShareUIType
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.toast
import com.common.widget.component.extension.visible
import com.lxj.xpopup.core.BottomPopupView
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.viewmodel.ShareViewModel
import com.onlineaginguniversity.common.widget.component.share.listener.ShareListener
import com.onlineaginguniversity.common.widget.component.share.utils.ShareSdkUtils
import com.onlineaginguniversity.databinding.LayoutShareBinding


/**
 * @desc:分享
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class ShareView(
    private val mContext: Context,
    private val owner: LifecycleOwner,
    private val uiType: ShareUIType,
    private val moduleId: Long?,
    private val moduleCode: EnumConst.ShareModuleCode?,
    private val mListener: ShareListener
) :
    BottomPopupView(mContext) {
    lateinit var mBinding: LayoutShareBinding
    lateinit var mViewModel: ShareViewModel

    var mlistener: ShareListener = mListener
    override fun getImplLayoutId(): Int {
        return R.layout.layout_share
    }

    override fun onCreate() {
        super.onCreate()
        //将ReportFragment注册到Activity中
        mBinding = LayoutShareBinding.bind(popupImplView)
        mViewModel = ShareViewModel()
        moduleId?.let {
            moduleCode?.let {
                mViewModel.requestShare(moduleId, it)
            }
        }
        initViewModelObserve()
        showUiTypeView()
        clickListener(
            mBinding.wx,
            mBinding.wxCircle,
            mBinding.image,
            mBinding.report,
            mBinding.delete,
            mBinding.cancel,
            clickListener = OnClickListener {
                when (it) {
                    mBinding.wx -> {
                        if (isWeixinAvilible() && ObjectUtils.isNotEmpty(mViewModel.shareBean)) {
                            ShareSdkUtils.wx(mViewModel.shareBean!!, mListener)
                        }
                    }
                    mBinding.wxCircle -> {
                        if (isWeixinAvilible() && ObjectUtils.isNotEmpty(mViewModel.shareBean)) {
                            ShareSdkUtils.wxMoments(mViewModel.shareBean!!, mListener)
                        }
                    }
                    mBinding.image -> {
                        mlistener.onClick(
                            it,
                            ShareType.IMAGE
                        )
                    }
                    mBinding.report -> {
                        mlistener.onClick(
                            it,
                            ShareType.REPORT
                        )
                    }
                    mBinding.delete -> {
                        mlistener.onClick(
                            it,
                            ShareType.DELETE
                        )
                    }
                }
                dismiss()
            }
        )
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {

    }

    /**
     * 根据显示类型显示不同的分享按钮
     */
    private fun showUiTypeView() {
        when (uiType) {
            ShareUIType.CIRCLE -> {//同学圈分享UI（显示举报，隐藏删除）
                mBinding.image.visible()
                mBinding.report.visible()
            }
            ShareUIType.CIRCLE_AUTHOR -> {//同学圈分享作者UI（显示删除，隐藏举报）
                mBinding.image.visible()
                mBinding.delete.visible()
            }
        }
    }

    /**
     * 检测是否安装微信
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

//    override fun getLifecycle(): Lifecycle {
//        return mLifecycleRegistry
//    }


}