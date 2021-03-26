package com.tanlifei.app.classmatecircle.ui.activity

import com.blankj.utilcode.util.BarUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.utils.extension.startActivity
import com.tanlifei.app.classmatecircle.adapter.VideoPagerAdapter
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.classmatecircle.viewmodel.CircleVideoPagerViewModel
import com.tanlifei.app.databinding.ActivityCircleVideoPagerBinding

/**
 * @desc: 同学圈抖音效果
 * @author: tanlifei
 * @date: 2021/3/26 10:27
 */
class CircleVideoPagerActivity :
    BaseRecyclerBVMActivity<ActivityCircleVideoPagerBinding, CircleBean, CircleVideoPagerViewModel>() {

    companion object {
        fun actionStart(id: Long, uid: Long, type: Int) {
            startActivity<CircleDetailActivity> {
                putExtra(GlobalConst.Extras.ID, id)
                putExtra(GlobalConst.Extras.UID, uid)
                putExtra(GlobalConst.Extras.TYPE, type)
            }
        }
    }

    override fun init() {
        initRefreshView(
            mBinding.smartRefreshLayout,
            mBinding.refreshRecycler,
            mBinding.refreshLoadingLayout
        )
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.arrowBack)
    }

    override fun createViewModel(): CircleVideoPagerViewModel {
        return CircleVideoPagerViewModel(
            intent.getLongExtra(GlobalConst.Extras.ID, 0),
            intent.getLongExtra(GlobalConst.Extras.UID, 0),
            intent.getIntExtra(GlobalConst.Extras.TYPE, 0)
        )
    }

    override fun setAdapter(): CommonRvAdapter<CircleBean> {
        return VideoPagerAdapter()
    }
}