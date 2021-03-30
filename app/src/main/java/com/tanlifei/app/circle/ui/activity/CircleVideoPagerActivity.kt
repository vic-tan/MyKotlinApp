package com.tanlifei.app.circle.ui.activity

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.common.constant.GlobalConst
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.activity.BaseRvActivity
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.startActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.tanlifei.app.R
import com.tanlifei.app.circle.adapter.VideoPagerAdapter
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.viewmodel.CircleVideoPagerViewModel
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
    BaseRvActivity<ActivityCircleVideoPagerBinding, CircleBean, CircleVideoPagerViewModel>() {
    private var mViewPagerLayoutManager: ViewPagerLayoutManager? = null
    private var mCurrentPosition = 0

    companion object {
        const val TYPE_RECOMMEND = 1//从首页,智能随机推荐作品列表
        const val TYPE_ONESELF = 2//从个人中心进入,只查看自己作品列表
        const val TYPE_CURRENT = 3//从消息进入,只查看当前作品列表
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
        mBinding.smartRefreshLayout.setEnableOverScrollDrag(false)//是否启用越界拖动（仿苹果效果）
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
        mBinding.refreshRecycler.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
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

        clickListener(mBinding.arrowBack, clickListener = View.OnClickListener {
            when (it) {
                mBinding.arrowBack -> {
                    Jzvd.releaseAllVideos()
                    ActivityUtils.finishActivity(this)
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


    override fun setAdapter(): BaseRvAdapter<CircleBean> {
        return VideoPagerAdapter()
    }


    override fun initImmersionBar() {
        immersionBar() {
            statusBarDarkFont(false, 0.2f)
        }
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