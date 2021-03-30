package com.tanlifei.app.circle.ui.fragment

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
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.common.utils.RecyclerUtils
import com.common.widget.component.extension.toast
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.tanlifei.app.R
import com.tanlifei.app.circle.adapter.FollowAdapter
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.ui.activity.CircleVideoPagerActivity
import com.tanlifei.app.circle.viewmodel.FollowViewModel
import com.tanlifei.app.common.constant.EnumConst
import com.tanlifei.app.common.widget.video.AutoPlayUtils
import com.tanlifei.app.common.widget.video.JzvdStdList
import com.tanlifei.app.databinding.FragmentFollowBinding
import com.tanlifei.app.databinding.ItemFollowBinding
import com.tanlifei.app.main.ui.activity.MainActivity
import com.tanlifei.app.main.viewmodel.MainViewModel


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment :
    BaseRvFragment<FragmentFollowBinding, FollowViewModel, CircleBean>() {

    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mHomeViewModel: MainViewModel

    companion object {
        fun newInstance() = FollowFragment()
    }

    override fun createViewModel(): FollowViewModel {
        return FollowViewModel()
    }

    override fun onFirstVisibleToUser() {
        mHomeViewModel = (activity as MainActivity).mViewModel
        initRecycler(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
        initListener()
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

    override fun itemClick(
        holder: ViewBinding,
        itemBean: CircleBean,
        v: View,
        position: Int
    ) {
        holder as ItemFollowBinding
        when (v) {
            holder.more,
            holder.shareLayout -> {
                context?.let {
                    XPopup.Builder(it).asCustom(
                        ShareView(
                            it,
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