package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.databinding.LayoutRecyclerRefreshBinding
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.adapter.RecommendAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.viewmodel.RecommendTabViewModel
import com.tanlifei.app.classmatecircle.viewmodel.RecommendViewModel


/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendFragment() :
    BaseRecyclerBVMFragment<LayoutRecyclerRefreshBinding, RecommendViewModel>() {

    private lateinit var adapter: RecommendAdapter

    companion object {
        fun newInstance(id: Long): RecommendFragment {
            val fragment = RecommendFragment()
            val args = Bundle()
            args.putLong(GlobalConst.Extras.ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewModel(): RecommendViewModel {
        return RecommendViewModel()
    }

    override fun onFirstVisibleToUser() {
        super.onFirstVisibleToUser()
    }


    override fun initView() {
        super.initView()
        adapter = RecommendAdapter(viewModel.mData as MutableList<ClassmateCircleBean>)
        initRefreshView(
            binding.smartRefreshLayout,
            binding.refreshLoadingLayout,
            binding.refreshRecycler
        )
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }


}