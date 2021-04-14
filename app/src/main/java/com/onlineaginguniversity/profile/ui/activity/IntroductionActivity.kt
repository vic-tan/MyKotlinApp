package com.onlineaginguniversity.profile.ui.activity

import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.base.viewmodel.EmptyViewModel
import com.common.constant.GlobalConst
import com.common.constant.GlobalConst.ActivityResult
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.click
import com.common.widget.component.extension.startActivityForResult
import com.common.widget.component.extension.toast
import com.onlineaginguniversity.databinding.ActivityIntroductionBinding


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class IntroductionActivity : BaseToolBarActivity<ActivityIntroductionBinding, EmptyViewModel>() {
    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart(content: String?) {
            startActivityForResult<IntroductionActivity>(ActivityResult.REQUEST_CODE_1){
                putExtra(GlobalConst.Extras.CONTENT, content)
            }
        }
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun init() {
        mInputHelper = TextInputHelper(this, mBinding.save)
        mInputHelper.addViews(mBinding.content)
        mBinding.content.setText(intent.getStringExtra(GlobalConst.Extras.CONTENT))
        mBinding.content.setSelection(mBinding.content.text.length)
        mBinding.content.requestFocus()
        mBinding.save.click {
            if (ObjectUtils.isEmpty(mBinding.content.text)) {
                toast("请输入内容")
                return@click
            }
            val i = Intent()
            i.putExtra(GlobalConst.Extras.CONTENT, mBinding.content.text.toString())
            setResult(RESULT_OK, i)
            ActivityUtils.finishActivity(this)
        }
    }

    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.content)
    }


}


