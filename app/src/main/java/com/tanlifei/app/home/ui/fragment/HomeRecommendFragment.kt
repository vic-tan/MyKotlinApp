package com.tanlifei.app.home.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.ui.fragment.BaseRvFragment
import com.common.widget.extension.toast
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.databinding.FragmentFollowBinding
import com.tanlifei.app.databinding.ItemHomeRecommentBinding
import com.tanlifei.app.home.adapter.HomeRecommentAdapter
import com.tanlifei.app.home.view.GridHomeRecommentItemDecoration
import com.tanlifei.app.home.viewmodel.HomeRecommendViewModel


/**
 * @desc:首页Tab推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeRecommendFragment :
    BaseRvFragment<FragmentFollowBinding, HomeRecommendViewModel, CircleBean>() {


    companion object {
        fun newInstance() = HomeRecommendFragment()
    }

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

    override fun setAdapter(): CommonRvAdapter<CircleBean> {
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