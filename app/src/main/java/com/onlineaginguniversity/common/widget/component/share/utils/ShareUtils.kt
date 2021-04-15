package com.onlineaginguniversity.common.widget.component.share.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.common.constant.GlobalEnumConst
import com.lxj.xpopup.XPopup
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.widget.component.share.listener.OnShareListener
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
        listener: OnShareListener
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
}