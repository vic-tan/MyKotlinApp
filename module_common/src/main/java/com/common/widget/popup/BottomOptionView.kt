package com.common.widget.popup

import android.content.Context
import android.view.View
import com.common.R
import com.common.utils.ViewUtils
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:底部列表
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class BottomOptionView<T>(context: Context) :
    BottomPopupView(context),
    View.OnClickListener {

    enum class ShareType {
        WX,//微信
        WX_CIRCLE,//朋友圈
        REPORT,//举报
        CREDIT,//删除
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_share
    }

    override fun onCreate() {
        super.onCreate()
        ViewUtils.setOnClickListener(
            this,
            this,
            R.id.wx,
            R.id.wx_circle,
            R.id.report,
            R.id.credit,
            R.id.cancel
        )
    }




    override fun onClick(v: View) {

    }


}