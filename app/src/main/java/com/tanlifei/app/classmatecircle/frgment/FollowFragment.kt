package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.databinding.FragmentFollowBinding


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseBVMFragment<FragmentFollowBinding, FollowViewModel>() {

    private lateinit var adapter: FollowAdapter

    companion object {
        fun newInstance(): FollowFragment {
            val args = Bundle()
            val fragment =
                FollowFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewModel(): FollowViewModel {
        return FollowViewModel()
    }

    override fun onFirstVisibleToUser() {
        initViewModelObserve()
        initListener()
        initData()
    }


    override fun initView() {
        adapter = FollowAdapter(viewModel.mData as MutableList<ClassmateCircleBean>)
        binding.refreshLayout.refreshRecycler.layoutManager = LinearLayoutManager(activity)
        binding.refreshLayout.refreshRecycler.adapter = adapter
    }

    private fun initData() {
        viewModel.refresh()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.dataChanged.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
        viewModel.uiBehavior.observe(this, Observer {
            when (it) {
                BaseListViewModel.UIType.NOTMOREDATA -> {
                    binding.refreshLayout.smartRefreshLayout.finishLoadMoreWithNoMoreData() //将不会再次触发加载更多事件
                }
            }

        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        binding.refreshLayout.smartRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            it.finishRefresh()
        }
        binding.refreshLayout.smartRefreshLayout.setOnLoadMoreListener {
            viewModel.loadMore()
            it.finishLoadMore()
        }
    }


}