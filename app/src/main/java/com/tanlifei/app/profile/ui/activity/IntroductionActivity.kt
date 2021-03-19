package com.tanlifei.app.profile.ui.activity

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.core.base.ui.viewmodel.EmptyViewModel
import com.common.utils.extension.click
import com.common.utils.extension.showKeyboard
import com.common.utils.extension.toast
import com.common.widget.TextInputHelper
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityIntroductionBinding


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
            ActivityUtils.startActivityForResult(ActivityUtils.getTopActivity(), intent, 1)
        }
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun init() {
        mInputHelper = TextInputHelper(this, binding.save)
        mInputHelper.addViews(binding.content)
        binding.content.setText(intent.getStringExtra(GlobalConst.Extras.CONTENT))
        binding.content.setSelection(binding.content.text.length)
        binding.content.showKeyboard()
        binding.save.click {
            if (ObjectUtils.isEmpty(binding.content.text)) {
                toast("请输入内容")
                return@click
            }
            val i = Intent()
            i.putExtra(GlobalConst.Extras.CONTENT, binding.content.text.toString())
            setResult(RESULT_OK, i)
            ActivityUtils.finishActivity(this)
        }
    }

    override fun showSoftByEditViewIds(): IntArray {
        return return intArrayOf(R.id.content)
    }


}


