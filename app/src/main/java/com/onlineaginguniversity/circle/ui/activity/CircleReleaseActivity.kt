package com.onlineaginguniversity.circle.ui.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.utils.FullyGridLayoutManager
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.ScreenUtils
import com.onlineaginguniversity.circle.adapter.ReleaseUploadAdapter
import com.onlineaginguniversity.circle.viewmodel.CircleReleaseViewModel
import com.onlineaginguniversity.databinding.ActivityCircleReleaseBinding


/**
 * @desc:同学圈发布界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class CircleReleaseActivity :
    BaseToolBarActivity<ActivityCircleReleaseBinding, CircleReleaseViewModel>() {

    lateinit var adpter: ReleaseUploadAdapter

    companion object {
        fun actionStart(result: List<LocalMedia?>, isVideo: Boolean) {
            startActivity<CircleReleaseActivity> {
                putParcelableArrayListExtra(GlobalConst.Extras.LIST, ArrayList(result))
                putExtra(GlobalConst.Extras.TYPE, isVideo)
            }
        }
    }

    override fun createViewModel(): CircleReleaseViewModel {
        return CircleReleaseViewModel(
            intent.getParcelableArrayListExtra(GlobalConst.Extras.LIST),
            intent.getBooleanExtra(GlobalConst.Extras.TYPE, false)
        )
    }

    override fun init() {
        mTitleBar.titleView.gone()
        initViewModelObserve()
        initListener()
        initData()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {

    }

    /**
     * 初始化监听
     */
    private fun initListener() {

    }

    private fun initData() {
        adpter = ReleaseUploadAdapter(mViewModel.isVideo)
        mViewModel.result?.let {
            adpter.mData = it.toMutableList()
        }
        adpter.setSelectMax(if (mViewModel.isVideo) 1 else 9)
        val manager = FullyGridLayoutManager(
            this,
            3, GridLayoutManager.VERTICAL, false
        )
        mBinding.uploadRecycler.layoutManager = manager
        mBinding.uploadRecycler.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                ScreenUtils.dip2px(this, 8f), false
            )
        )
        mBinding.uploadRecycler.adapter = adpter
    }
}