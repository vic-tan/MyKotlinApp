package com.onlineaginguniversity.common.widget.component.share.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.common.constant.GlobalEnumConst
import com.common.utils.PermissionUtils
import com.lxj.xpopup.XPopup
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.circle.ui.widget.GenerateShareBitmapView
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.widget.component.share.listener.ShareListener
import com.onlineaginguniversity.common.widget.component.share.ui.ShareView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/4/15 10:55
 */
object ShareUtils {

    /**
     * 显示分享View
     */
    fun showView(
        context: Context?,
        owner: LifecycleOwner,
        uiType: GlobalEnumConst.ShareUIType,
        id: Long?,
        moduleCode: EnumConst.ShareModuleCode?,
        listener: ShareListener
    ) {
        context?.let {
            XPopup.Builder(it).asCustom(
                ShareView(
                    mContext = it,
                    owner = owner,
                    uiType = uiType,
                    moduleId = id,
                    moduleCode = moduleCode,
                    mListener = listener
                )
            ).show()
        }
    }

    /**
     * 显示生成分享图分享View
     */
    fun showGenerateShareBitmapView(
        context: Context?,
        bean: CircleBean
    ) {
        context?.let {
            XPopup.Builder(context)
                .hasStatusBarShadow(false)
                .dismissOnBackPressed(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .dismissOnTouchOutside(false)
                .asCustom(GenerateShareBitmapView(context, bean))
                .show()
        }
    }
}