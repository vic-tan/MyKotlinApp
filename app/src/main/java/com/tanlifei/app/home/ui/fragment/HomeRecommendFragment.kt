package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.widget.LoadingLayout
import com.hjq.toast.ToastUtils
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

    private lateinit var adapter: HomeRecommentAdapter

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
        initRecycler()
        binding.refreshLayout.refreshRecycler.addItemDecoration(GridHomeRecommentItemDecoration(8))
    }


    override fun initView() {
        super.initView()
        adapter = HomeRecommentAdapter(context)
        adapter.mData = viewModel.mData as MutableList<ClassmateCircleBean>
        adapter.setOnItemClick(object :
            OnItemClickListener<ItemHomeRecommentBinding, ClassmateCircleBean> {
            override fun onItemClick(
                itemBinding: ItemHomeRecommentBinding,
                itemBan: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
                when (v) {
                    itemBinding.item -> ToastUtils.show(itemBan.nickName)
                }
            }
        })
    }

    fun refresh() {
        if (ObjectUtils.isNotEmpty(viewModel)) {
            viewModel.refresh()
        }
    }

    override fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }

    override fun smartRefreshLayout(): SmartRefreshLayout {
        return binding.refreshLayout.smartRefreshLayout
    }

    override fun refreshLoadingLayout(): LoadingLayout {
        return binding.refreshLayout.refreshLoadingLayout
    }

    override fun refreshRecycler(): RecyclerView {
        return binding.refreshLayout.refreshRecycler
    }

}