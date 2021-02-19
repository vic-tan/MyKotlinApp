package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.viewmodel.RecommendViewModel
import com.tanlifei.app.databinding.FragmentRecommendBinding

/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendFragment : BaseRecyclerBVMFragment<FragmentRecommendBinding, RecommendViewModel>() {
    private lateinit var adapter: FollowAdapter

    companion object {
        fun newInstance(): RecommendFragment {
            val args = Bundle()
            val fragment =
                RecommendFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
        super.onFirstVisibleToUser()
    }

    override fun createViewModel(): RecommendViewModel {
        return RecommendViewModel()
    }

    override fun initView() {
        adapter = FollowAdapter(viewModel.mData as MutableList<ClassmateCircleBean>)
        initRefreshView(
            binding.refreshLayout.smartRefreshLayout,
            binding.refreshLayout.refreshLoadingLayout,
            binding.refreshLayout.refreshRecycler
        )
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }


}