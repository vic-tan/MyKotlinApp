package com.onlineaginguniversity.circle.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.fragment.BaseRvFragment
import com.common.constant.GlobalConst
import com.common.core.event.BaseEvent
import com.common.utils.ComDialogUtils
import com.common.widget.component.extension.clickListener
import com.onlineaginguniversity.circle.adapter.RecommendAdapter
import com.onlineaginguniversity.circle.adapter.itemdecoration.GridItemDecoration
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.circle.ui.activity.CircleDetailActivity
import com.onlineaginguniversity.circle.ui.activity.CircleReleaseActivity
import com.onlineaginguniversity.circle.ui.activity.CircleVideoPagerActivity
import com.onlineaginguniversity.circle.utils.CircleComUtils
import com.onlineaginguniversity.circle.viewmodel.CircleViewModel
import com.onlineaginguniversity.circle.viewmodel.RecommendViewModel
import com.onlineaginguniversity.common.event.PraiseEvent
import com.onlineaginguniversity.databinding.FragmentRecommendBinding
import com.onlineaginguniversity.databinding.ItemRecommendBinding
import com.onlineaginguniversity.profile.ui.activity.AboutActivity
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
        clickListener(mBinding.circleRelease, clickListener = View.OnClickListener {
            when (it) {
                mBinding.circleRelease -> {
                    CircleReleaseActivity.actionStart()
                }
            }

        })
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