package com.tanlifei.app.classmatecircle.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.viewbinding.ViewBinding
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.fragment.BaseRvFragment
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.common.utils.RecyclerUtils
import com.common.widget.component.extension.toast
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.common.widget.video.AutoPlayUtils
import com.tanlifei.app.common.widget.video.JzvdStdList
import com.tanlifei.app.databinding.FragmentFollowBinding
import com.tanlifei.app.databinding.ItemFollowBinding


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment :
    BaseRvFragment<FragmentFollowBinding, FollowViewModel, CircleBean>() {


    companion object {
        fun newInstance() = FollowFragment()
    }

    override fun createViewModel(): FollowViewModel {
        return FollowViewModel()
    }

    override fun onFirstVisibleToUser() {
        initRecycler(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
        initListener()
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
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
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
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition(),
                            0.2f
                        )
                    }
                    if (!NetworkUtils.isConnected())//没有网不加载，让用户手动加载
                        return
                    // 获取 LayoutManger
                    val layoutManager = recyclerView.layoutManager
                    if (ObjectUtils.isNotEmpty(layoutManager)) {
                        // 如果列表正在往上滚动，并且表项最后可见表项索引值 等于 预加载阈值
                        if (dy > 0 && ObjectUtils.isNotEmpty(layoutManager!!.itemCount)
                            && RecyclerUtils.getOutLast(layoutManager) >= RecyclerUtils.getLoadCount(
                                layoutManager
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

    private fun initListener(){
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
            /*  holder.banner -> {
                  var list = mutableListOf<String>()
                  var url = itemBean.image?.url
                  url?.let { list.add(it) }
                  PhotoUtils.showListPhoto(context, holder.banner, position, list)
              }*/
        }
    }

}