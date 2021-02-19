package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseRecyclerBVMFragment<FragmentFollowBinding, FollowViewModel>() {

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
        super.onFirstVisibleToUser()
    }


    override fun initView() {
        super.initView()
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