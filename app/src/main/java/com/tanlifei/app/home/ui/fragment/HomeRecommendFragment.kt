package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.listener.OnMultiItemListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.utils.extension.toast
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
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
    BaseRecyclerBVMFragment<FragmentFollowBinding, HomeRecommendViewModel>() {

    private lateinit var mAdapter: HomeRecommentAdapter

    companion object {
        fun newInstance(): HomeRecommendFragment {
            val fragment = HomeRecommendFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
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
        mAdapter = HomeRecommentAdapter()
        mAdapter.mData = mViewModel.mData
        mAdapter.setItemClickListener(object :
            OnMultiItemListener<ClassmateCircleBean> {
            override fun click(
                itemBinding: ViewBinding,
                itemBan: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
                itemBinding as ItemHomeRecommentBinding
                when (v) {
                    itemBinding.item -> toast(itemBan.nickName)
                }
            }
        })
    }

    fun refresh() {
        if (ObjectUtils.isNotEmpty(mViewModel)) {
            mViewModel.refresh()
        }
    }

    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }

    override fun setAdapter(): Any {
        return mAdapter
    }

}