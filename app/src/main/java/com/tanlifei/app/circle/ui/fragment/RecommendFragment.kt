package com.tanlifei.app.circle.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.fragment.BaseRvFragment
import com.common.constant.GlobalConst
import com.common.core.event.BaseEvent
import com.common.widget.component.extension.log
import com.tanlifei.app.circle.adapter.RecommendAdapter
import com.tanlifei.app.circle.adapter.itemdecoration.GridItemDecoration
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.ui.activity.CircleDetailActivity
import com.tanlifei.app.circle.ui.activity.CircleVideoPagerActivity
import com.tanlifei.app.circle.utils.CircleComUtils
import com.tanlifei.app.circle.viewmodel.CircleViewModel
import com.tanlifei.app.circle.viewmodel.RecommendViewModel
import com.tanlifei.app.common.event.PraiseEvent
import com.tanlifei.app.databinding.FragmentRecommendBinding
import com.tanlifei.app.databinding.ItemRecommendBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendFragment :
    BaseRvFragment<FragmentRecommendBinding, RecommendViewModel, CircleBean>() {

    private lateinit var circleViewModel: CircleViewModel

    override fun createViewModel(): RecommendViewModel {
        return RecommendViewModel(
            if (ObjectUtils.isEmpty(arguments)) 0 else arguments!!.getLong(
                GlobalConst.Extras.ID,
                0
            )
        )
    }

    override fun onFirstVisibleToUser() {
        circleViewModel = CircleViewModel()
        initRecycler(
            mBinding.smartRefreshLayout,
            mBinding.refreshRecycler,
            mBinding.refreshLoadingLayout
        )
        mBinding.refreshRecycler.addItemDecoration(
            GridItemDecoration(
                8
            )
        )
        initViewModelObserve()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        CircleComUtils.notifyPraiseObserve(circleViewModel, this, mViewModel.mData)
    }

    /**
     * 设置 RecyclerView LayoutManager
     */
    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun setAdapter(): BaseRvAdapter<CircleBean> {
        return RecommendAdapter()
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
        holder as ItemRecommendBinding
        when (v) {
            holder.praise -> {
                circleViewModel.requestPraise(
                    itemBean.publishId,
                    itemBean.isStar,
                    position
                )
            }
            holder.item -> {
                if (itemBean.mediaType == 1) {
                    CircleVideoPagerActivity.actionStart(
                        itemBean.publishId, -1,
                        CircleVideoPagerActivity.TYPE_RECOMMEND
                    )
                } else {
                    CircleDetailActivity.actionStart(itemBean.publishId)
                }

            }
        }
    }

}