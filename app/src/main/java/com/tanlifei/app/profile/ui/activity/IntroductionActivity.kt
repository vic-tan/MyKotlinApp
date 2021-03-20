package com.tanlifei.app.profile.ui.activity

import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.core.base.ui.viewmodel.EmptyViewModel
import com.common.utils.extension.click
import com.common.utils.extension.toast
import com.common.widget.TextInputHelper
import com.tanlifei.app.databinding.ActivityIntroductionBinding
import com.common.cofing.constant.GlobalConst.ActivityResult


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class IntroductionActivity : BaseFormActivity<ActivityIntroductionBinding, EmptyViewModel>() {
    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart(content: String?) {
            var intent =
                Intent(ActivityUtils.getTopActivity(), IntroductionActivity::class.java).apply {
                    putExtra(GlobalConst.Extras.CONTENT, content)
                }
            ActivityUtils.startActivityForResult(
                ActivityUtils.getTopActivity(),
                intent,
                ActivityResult.REQUEST_CODE_1
            )
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


