package com.onlineaginguniversity.home.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.fragment.BaseRvFragment
import com.common.widget.component.extension.toast
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.databinding.FragmentFollowBinding
import com.onlineaginguniversity.databinding.ItemHomeRecommentBinding
import com.onlineaginguniversity.home.adapter.HomeRecommentAdapter
import com.onlineaginguniversity.home.ui.widget.GridHomeRecommentItemDecoration
import com.onlineaginguniversity.home.viewmodel.HomeRecommendViewModel


/**
 * @desc:首页Tab推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeRecommendFragment :
    BaseRvFragment<FragmentFollowBinding, HomeRecommendViewModel, CircleBean>() {


    override fun createViewModel(): HomeRecommendViewModel {
        return HomeRecommendViewModel()
    }

    override fun onFirstVisibleToUser() {
        initRecycler(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
        mBinding.refreshLayout.refreshRecycler.addItemDecoration(GridHomeRecommentItemDecoration(8))
    }


    override fun initView() {
        super.initView()
    }

    fun refresh() {
        if (ObjectUtils.isNotEmpty(mViewModel)) {
            mViewModel.refresh()
        }
    }

    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }

    override fun setAdapter(): BaseRvAdapter<CircleBean> {
        return HomeRecommentAdapter()
    }

    override fun itemClick(
        holder: ViewBinding,
        itemBean: CircleBean,
        v: View,
        position: Int
    ) {
        holder as ItemHomeRecommentBinding
        when (v) {
            holder.item -> toast(itemBean.nickName)
        }
    }

}