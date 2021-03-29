package com.tanlifei.app.classmatecircle.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.ui.fragment.BaseRecyclerFragment
import com.tanlifei.app.classmatecircle.adapter.RecommendAdapter
import com.tanlifei.app.classmatecircle.adapter.itemdecoration.GridItemDecoration
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.classmatecircle.ui.activity.CircleDetailActivity
import com.tanlifei.app.classmatecircle.ui.activity.CircleVideoPagerActivity
import com.tanlifei.app.classmatecircle.viewmodel.RecommendViewModel
import com.tanlifei.app.databinding.FragmentRecommendBinding
import com.tanlifei.app.databinding.ItemRecommendBinding


/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendFragment :
    BaseRecyclerFragment<FragmentRecommendBinding, RecommendViewModel, CircleBean>() {


    companion object {
        const val TYPE_HOME = 1//从首页
        fun newInstance(id: Long) = RecommendFragment().apply {
            arguments?.apply {
                putLong(GlobalConst.Extras.ID, id)
            }
        }
    }

    override fun createViewModel(): RecommendViewModel {
        return RecommendViewModel(
            if (ObjectUtils.isEmpty(arguments)) 0 else arguments!!.getLong(
                GlobalConst.Extras.ID,
                0
            )
        )
    }

    override fun onFirstVisibleToUser() {
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

    }

    /**
     * 设置 RecyclerView LayoutManager
     */
    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun setAdapter(): CommonRvAdapter<CircleBean> {
        return RecommendAdapter()
    }

    override fun itemClick(
        holder: ViewBinding,
        itemBean: CircleBean,
        v: View,
        position: Int
    ) {
        holder as ItemRecommendBinding
        when (v) {
            holder.item -> {
                if (itemBean.mediaType == 1) {
                    CircleVideoPagerActivity.actionStart(
                        itemBean.publishId, -1,
                        TYPE_HOME
                    )
                } else {
                    CircleDetailActivity.actionStart(itemBean.publishId)
                }

            }
        }
    }

}