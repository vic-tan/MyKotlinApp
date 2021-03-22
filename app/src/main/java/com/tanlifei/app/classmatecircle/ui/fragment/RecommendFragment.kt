package com.tanlifei.app.classmatecircle.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tanlifei.app.classmatecircle.adapter.RecommendAdapter
import com.tanlifei.app.classmatecircle.adapter.itemdecoration.GridItemDecoration
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.ui.activity.ClassmateCircleDetailActivity
import com.tanlifei.app.classmatecircle.viewmodel.RecommendViewModel
import com.tanlifei.app.databinding.FragmentRecommendBinding
import com.tanlifei.app.databinding.ItemRecommendBinding


/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendFragment :
    BaseRecyclerBVMFragment<FragmentRecommendBinding, RecommendViewModel>() {

    private lateinit var mAdapter: RecommendAdapter

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
        return RecommendViewModel(
            if (ObjectUtils.isEmpty(arguments)) 0 else arguments!!.getLong(
                GlobalConst.Extras.ID,
                0
            )
        )
    }

    override fun onFirstVisibleToUser() {
        mAdapter = RecommendAdapter()
        mAdapter.mData = mViewModel.mData as MutableList<ClassmateCircleBean>
        mAdapter.setItemClickListener(object :
            OnItemClickListener<ItemRecommendBinding, ClassmateCircleBean> {
            override fun click(
                itemBinding: ItemRecommendBinding,
                itemBean: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
                when (v) {
                    itemBinding.item -> ClassmateCircleDetailActivity.actionStart(itemBean.publishId)
                }
            }

        })
        initRecycler()
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

    override fun setAdapter(): Any {
        return mAdapter
    }

    override fun smartRefreshLayout(): SmartRefreshLayout {
        return mBinding.smartRefreshLayout
    }

    override fun refreshLoadingLayout(): LoadingLayout {
        return mBinding.refreshLoadingLayout
    }

    override fun refreshRecycler(): RecyclerView {
        return mBinding.refreshRecycler
    }

}