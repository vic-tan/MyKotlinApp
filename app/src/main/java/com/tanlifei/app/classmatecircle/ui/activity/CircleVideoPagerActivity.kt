package com.tanlifei.app.classmatecircle.ui.activity

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.BarUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.utils.extension.startActivity
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.adapter.VideoPagerAdapter
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.classmatecircle.viewmodel.CircleVideoPagerViewModel
import com.tanlifei.app.common.widget.video.JzvdStdTikTok
import com.tanlifei.app.common.widget.video.OnViewPagerListener
import com.tanlifei.app.common.widget.video.ViewPagerLayoutManager
import com.tanlifei.app.databinding.ActivityCircleVideoPagerBinding

/**
 * @desc: 同学圈抖音效果
 * @author: tanlifei
 * @date: 2021/3/26 10:27
 */
class CircleVideoPagerActivity :
    BaseRecyclerBVMActivity<ActivityCircleVideoPagerBinding, CircleBean, CircleVideoPagerViewModel>() {
    private var mViewPagerLayoutManager: ViewPagerLayoutManager? = null
    private var mCurrentPosition = 0

    companion object {
        fun actionStart(id: Long, uid: Long, type: Int) {
            startActivity<CircleVideoPagerActivity> {
                putExtra(GlobalConst.Extras.ID, id)
                putExtra(GlobalConst.Extras.UID, uid)
                putExtra(GlobalConst.Extras.TYPE, type)
            }
        }
    }

    override fun createViewModel(): CircleVideoPagerViewModel {
        return CircleVideoPagerViewModel(
            intent.getLongExtra(GlobalConst.Extras.ID, 0),
            intent.getLongExtra(GlobalConst.Extras.UID, 0),
            intent.getIntExtra(GlobalConst.Extras.TYPE, 0)
        )
    }

    override fun init() {
        initRefreshView(
            mBinding.smartRefreshLayout,
            mBinding.refreshRecycler,
            mBinding.refreshLoadingLayout
        )
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.arrowBack)
        mViewPagerLayoutManager?.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {
                //自动播放第一条
                autoPlayVideo()
            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
                if (mCurrentPosition == position) {
                    Jzvd.releaseAllVideos()
                }
            }

            override fun onPageSelected(position: Int, isBottom: Boolean) {
                if (mCurrentPosition == position) {
                    return
                }
                autoPlayVideo()
                mCurrentPosition = position
            }

        })
        mBinding.refreshRecycler.addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                val jzvd: Jzvd = view.findViewById(R.id.player)
                if (jzvd != null && Jzvd.CURRENT_JZVD != null && jzvd.jzDataSource != null &&
                    jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.currentUrl)
                ) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos()
                    }
                }
            }
        })


    }

    private fun autoPlayVideo() {
        if (mBinding.refreshRecycler == null || mBinding.refreshRecycler.getChildAt(0) == null) {
            return
        }
        val player: JzvdStdTikTok = mBinding.refreshRecycler.getChildAt(0).findViewById(R.id.player)
        player?.startVideoAfterPreloading()
    }


    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        mViewPagerLayoutManager = ViewPagerLayoutManager(this, OrientationHelper.VERTICAL, false)
        return mViewPagerLayoutManager as RecyclerView.LayoutManager
    }


    override fun setAdapter(): CommonRvAdapter<CircleBean> {
        return VideoPagerAdapter()
    }

    override fun showFullScreen(): Boolean {
        return true
    }

    override fun visibleToolbar(): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

}