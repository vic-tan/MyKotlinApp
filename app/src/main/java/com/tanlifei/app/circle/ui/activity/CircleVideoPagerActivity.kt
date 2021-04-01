package com.tanlifei.app.circle.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.viewbinding.ViewBinding
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.common.ComFun
import com.common.base.adapter.BaseRvAdapter
import com.common.base.listener.ComListener
import com.common.base.ui.activity.BaseRvActivity
import com.common.constant.GlobalConst
import com.common.constant.GlobalEnumConst
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.common.utils.RecyclerUtils
import com.common.widget.component.extension.*
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.SimpleCallback
import com.tanlifei.app.R
import com.tanlifei.app.circle.adapter.VideoPagerAdapter
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.utils.CircleComUtils
import com.tanlifei.app.circle.viewmodel.CircleVideoPagerViewModel
import com.tanlifei.app.circle.viewmodel.CircleViewModel
import com.tanlifei.app.circle.widget.VideoShadowPopupView
import com.tanlifei.app.common.event.PraiseEvent
import com.tanlifei.app.common.event.UserEvent
import com.tanlifei.app.common.widget.video.JzvdStdTikTok
import com.tanlifei.app.common.widget.video.OnViewPagerListener
import com.tanlifei.app.common.widget.video.ViewPagerLayoutManager
import com.tanlifei.app.databinding.ActivityCircleVideoPagerBinding
import com.tanlifei.app.databinding.ItemVideoPagerBinding
import org.greenrobot.eventbus.EventBus


/**
 * @desc: 同学圈抖音效果
 * @author: tanlifei
 * @date: 2021/3/26 10:27
 */
class CircleVideoPagerActivity :
    BaseRvActivity<ActivityCircleVideoPagerBinding, CircleBean, CircleVideoPagerViewModel>(),
    ComListener.BackCall {
    private var mViewPagerLayoutManager: ViewPagerLayoutManager? = null
    private var mCurrentPosition = 0
    lateinit var circleViewModel: CircleViewModel

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
        circleViewModel = CircleViewModel()
        return CircleVideoPagerViewModel(
            intent.getLongExtra(GlobalConst.Extras.ID, 0),
            intent.getLongExtra(GlobalConst.Extras.UID, 0),
            intent.getIntExtra(GlobalConst.Extras.TYPE, 0)
        )
    }

    override fun dataChangeObserve() {
        RecyclerUtils.dataChangeObserve(
            mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>,
            mViewModel,
            this
        ) {
            when (it.uiType) {
                /**下拉刷新**/
                GlobalEnumConst.UiType.REFRESH -> {
                    if (mViewModel.mData.isNotEmpty()) {
                        mBinding.input.visible()
                    }
                }
            }
        }
    }


    override fun init() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.smartRefreshLayout)//为 view 增加 MarginTop 为状态栏高度
        initRefreshView(
            mBinding.smartRefreshLayout,
            mBinding.refreshRecycler,
            mBinding.refreshLoadingLayout
        )
        mBinding.smartRefreshLayout.setEnableOverScrollDrag(false)//是否启用越界拖动（仿苹果效果）
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.arrowBack)
        initListener()
        initViewModelObserve()
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
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


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        circleViewModel.mFollowChanged.observe(this, Observer { followResponse ->
            followResponse.position?.let {
                (mViewModel.mData[it] as CircleBean).apply {
                    isFollower = followResponse.isFollower
                    isFollowing = followResponse.isFollowing
                }
                notifyPartItemChanged(it)
            }
        })
        CircleComUtils.notifyPraiseObserve(
            circleViewModel,
            this,
            mViewModel.mData,
            true,
            mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        )
    }

    /**
     * 局部修改,因为视频不用刷新
     */
    private fun notifyPartItemChanged(position: Int) {
        mAdapter.notifyItemChanged(position, GlobalConst.Adapter.PAYLOAD)
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
        return VideoPagerAdapter(this)
    }

    override fun itemClick(holder: ViewBinding, itemBean: CircleBean, v: View, position: Int) {
        holder as ItemVideoPagerBinding
        when (v) {
            holder.attention -> {
                circleViewModel.requestFollow(
                    itemBean.publishId,
                    itemBean.isFollowing == 0,
                    position
                )
            }
            holder.praise -> {
                circleViewModel.requestPraise(
                    itemBean.publishId,
                    itemBean.isStar,
                    position
                )
            }
            holder.comment -> {

            }
            holder.share -> {
                XPopup.Builder(mActivity).asCustom(
                    ShareView(
                        mActivity,
                        ShareBean("网上老年大学", "https://www.baidu.com", "test 分享", ""),
                        object :
                            OnShareListener {
                            override fun onItemClick(
                                v: View,
                                type: ShareView.ShareType
                            ) {
                                toast(type.name)
                            }

                        })
                ).show()
            }
        }
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

    override fun call(any: Any?, any2: Any?) {
        val holder = any as ItemVideoPagerBinding
        var popup = XPopup.Builder(mActivity)
            .atView(holder.bottomLine)
            .popupPosition(PopupPosition.Top)
            .setPopupCallback(object : SimpleCallback() {

                override fun beforeShow(popupView: BasePopupView?) {
                    super.beforeShow(popupView)
                    ComFun.mHandler.postDelayed({
                        holder.bottomContent.gone()
                    }, 200)
                }

                override fun beforeDismiss(popupView: BasePopupView?) {
                    super.beforeDismiss(popupView)
                    ComFun.mHandler.postDelayed({
                        holder.bottomContent.visible()
                    }, 200)
                }


            })
            .asCustom(VideoShadowPopupView(mActivity, any2 as CircleBean))
        popup.show()
    }

}