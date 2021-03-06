package com.onlineaginguniversity.circle.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.viewbinding.ViewBinding
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.fragment.BaseRvFragment
import com.common.constant.GlobalEnumConst
import com.common.core.event.BaseEvent
import com.common.utils.ComDialogUtils
import com.onlineaginguniversity.common.widget.component.share.listener.ShareListener
import com.common.utils.RecyclerUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.onlineaginguniversity.R
import com.onlineaginguniversity.circle.adapter.FollowAdapter
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.circle.ui.activity.CircleVideoPagerActivity
import com.onlineaginguniversity.circle.utils.CircleComUtils
import com.onlineaginguniversity.circle.viewmodel.CircleViewModel
import com.onlineaginguniversity.circle.viewmodel.FollowViewModel
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.event.PraiseEvent
import com.onlineaginguniversity.common.utils.UserInfoUtils
import com.onlineaginguniversity.common.widget.component.share.bean.ShareBean
import com.onlineaginguniversity.common.widget.component.share.utils.ShareUtils
import com.onlineaginguniversity.common.widget.video.AutoPlayUtils
import com.onlineaginguniversity.common.widget.video.JzvdStdList
import com.onlineaginguniversity.databinding.FragmentFollowBinding
import com.onlineaginguniversity.databinding.ItemFollowBinding
import com.onlineaginguniversity.main.ui.activity.MainActivity
import com.onlineaginguniversity.main.viewmodel.MainViewModel
import com.scwang.smart.refresh.layout.constant.RefreshState
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment :
    BaseRvFragment<FragmentFollowBinding, FollowViewModel, CircleBean>() {

    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mHomeViewModel: MainViewModel
    private lateinit var mCircleViewModel: CircleViewModel


    override fun createViewModel(): FollowViewModel {
        return FollowViewModel()
    }

    override fun onFirstVisibleToUser() {
        mCircleViewModel = CircleViewModel()
        mHomeViewModel = (activity as MainActivity).mViewModel
        initRecycler(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
        initListener()
        initViewModelObserve()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mHomeViewModel.mShowFollowFragment.observe(this, Observer {
            when (it) {
                EnumConst.CircleTabTag.CIRCLE.value -> {
                    if (mViewModel.mData.isNotEmpty()) {
                        autoPlay()
                    }
                }
                else -> {//其它TAB 关闭视频
                    Jzvd.releaseAllVideos()
                }
            }

        })
        mCircleViewModel.mDeleteChanged.observe(this, Observer {
            mAdapter.mData.removeAt(it)
            mAdapter.notifyDataSetChanged()
        })
        CircleComUtils.notifyPraiseObserve(mCircleViewModel, this, mViewModel.mData)
    }


    override fun dataChangeObserve() {
        RecyclerUtils.dataChangeObserve(
            mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>,
            mViewModel,
            this
        ) {
            when (it.uiType) {
                /**上拉刷新**/
                GlobalEnumConst.UiType.REFRESH -> {
                    if (mViewModel.mData.isNotEmpty()) {
                        ComFun.mHandler.postDelayed({
                            autoPlay()
                        }, 1000)
                    }
                }
            }
        }
    }

    private fun autoPlay() {
        AutoPlayUtils.onScrollPlayVideo(
            mBinding.refreshLayout.refreshRecycler,
            R.id.player,
            linearLayoutManager.findFirstVisibleItemPosition(),
            linearLayoutManager.findLastVisibleItemPosition()
        )
    }

    override fun addOnScrollListener(mRefreshRecycler: RecyclerView) {
        mBinding.refreshLayout.refreshRecycler.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        AutoPlayUtils.onScrollPlayVideo(
                            recyclerView,
                            R.id.player,
                            linearLayoutManager.findFirstVisibleItemPosition(),
                            linearLayoutManager.findLastVisibleItemPosition()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                try {
                    if (dy != 0) {
                        AutoPlayUtils.onScrollReleaseAllVideos(
                            linearLayoutManager.findFirstVisibleItemPosition(),
                            linearLayoutManager.findLastVisibleItemPosition(),
                            0.2f
                        )
                    }
                    if (!NetworkUtils.isConnected())//没有网不加载，让用户手动加载
                        return
                    // 获取 LayoutManger
                    if (ObjectUtils.isNotEmpty(linearLayoutManager)) {
                        // 如果列表正在往上滚动，并且表项最后可见表项索引值 等于 预加载阈值
                        if (dy > 0 && ObjectUtils.isNotEmpty(linearLayoutManager!!.itemCount)
                            && RecyclerUtils.getOutLast(linearLayoutManager) >= RecyclerUtils.getLoadCount(
                                linearLayoutManager
                            )
                            && mViewModel.mRefreshState == RefreshState.None
                        ) {
                            mViewModel.loadMore()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun initView() {
        super.initView()

    }

    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        linearLayoutManager = LinearLayoutManager(context)
        return linearLayoutManager
    }

    private fun initListener() {
        //添加OnScrollListener 监听 ，循环遍历 可见区域
        mBinding.refreshLayout.refreshRecycler.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                val jzvd: Jzvd = view.findViewById<View>(R.id.player) as JzvdStdList
                if (jzvd != null && Jzvd.CURRENT_JZVD != null && null != jzvd.jzDataSource &&
                    jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.currentUrl)
                ) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos()
                    }
                }
            }

            override fun onChildViewAttachedToWindow(view: View) {
            }
        })
    }

    override fun setAdapter(): BaseRvAdapter<CircleBean> {
        return FollowAdapter()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(event: BaseEvent) {
        if (event is PraiseEvent) {
            CircleComUtils.syncPraise(
                event,
                mViewModel.mData,
                mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
            )
        }
    }


    override fun registerEventBus(): Boolean {
        return true
    }

    override fun itemClick(
        holder: ViewBinding,
        itemBean: CircleBean,
        v: View,
        position: Int
    ) {
        holder as ItemFollowBinding
        when (v) {
            holder.praiseLayout -> {
                mCircleViewModel.requestPraise(
                    itemBean.publishId,
                    itemBean.isStar,
                    position
                )
            }
            holder.more,
            holder.shareLayout -> {
                ShareUtils.showView(context = context,
                    owner = this,
                    uiType = if (itemBean.uid == UserInfoUtils.getUid()) GlobalEnumConst.ShareUIType.CIRCLE_AUTHOR
                    else GlobalEnumConst.ShareUIType.CIRCLE,
                    id = itemBean.publishId,
                    moduleCode = if (itemBean.mediaType == 0) EnumConst.ShareModuleCode.CIRCLE_IMAGE
                    else EnumConst.ShareModuleCode.CIRCLE_VIDEO,
                    listener = object :
                        ShareListener {
                        override fun onClick(
                            v: View,
                            type: GlobalEnumConst.ShareType,
                            shareBean: ShareBean?
                        ) {
                            when (type) {
                                GlobalEnumConst.ShareType.GENERATE_BITMAP -> {//生成分享图
                                    shareBean?.let {
                                        itemBean.qrURL = it.targetUrl
                                    }
                                    ShareUtils.showGenerateShareBitmapView(context, itemBean)
                                }
                                GlobalEnumConst.ShareType.DELETE -> {//删除
                                    ComDialogUtils.showMultDialogByNoTitle(context, "确认要删除该作品?",
                                        OnConfirmListener {
                                            mCircleViewModel.requestDelete(
                                                itemBean.publishId,
                                                position
                                            )
                                        }
                                    )
                                }
                            }
                        }

                    })

            }
            holder.playerControl -> {
                Jzvd.releaseAllVideos()
                CircleVideoPagerActivity.actionStart(
                    itemBean.publishId, -1,
                    CircleVideoPagerActivity.TYPE_RECOMMEND
                )
            }
        }
    }
}