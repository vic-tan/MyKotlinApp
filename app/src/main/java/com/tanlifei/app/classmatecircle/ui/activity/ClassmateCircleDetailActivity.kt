package com.tanlifei.app.classmatecircle.ui.activity

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.utils.AntiShakeUtils
import com.common.utils.ViewUtils
import com.tanlifei.app.classmatecircle.viewmodel.ClassmateCircleDetailViewModel
import com.tanlifei.app.databinding.ActivityClassmetaCircleDetailBinding


/**
 * @desc:详情界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ClassmateCircleDetailActivity :
    BaseToolBarActivity<ActivityClassmetaCircleDetailBinding, ClassmateCircleDetailViewModel>(),
    View.OnClickListener {


    companion object {
        fun actionStart(id: Long) {
            var intent =
                Intent(
                    ActivityUtils.getTopActivity(),
                    ClassmateCircleDetailActivity::class.java
                ).apply {
                    putExtra(GlobalConst.Extras.ID, id)
                }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun createViewModel(): ClassmateCircleDetailViewModel {
        return ClassmateCircleDetailViewModel(intent.getLongExtra(GlobalConst.Extras.ID, 0))
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.dataChanged.observe(this, Observer {

        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        ViewUtils.setOnClickListener(this)
    }

    private fun initData() {
        viewModel.requestDetail()
    }

    override fun onClick(v: View) {
        if (AntiShakeUtils.isInvalidClick(v)) return

    }


}