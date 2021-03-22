package com.tanlifei.app.classmatecircle.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
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
    BaseRecyclerBVMFragment<FragmentRecommendBinding, ClassmateCircleBean, RecommendViewModel>() {


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
        mAdapter.mData = mViewModel.mData
        mAdapter.setItemClickListener(object :
            OnItemClickListener<ClassmateCircleBean> {
            override fun click(
                itemBinding: ViewBinding,
                itemBean: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
                itemBinding as ItemRecommendBinding
                when (v) {
                    itemBinding.item -> ClassmateCircleDetailActivity.actionStart(itemBean.publishId)
                }
            }

        })
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

    override fun setAdapter(): CommonRvAdapter<ClassmateCircleBean> {
        return RecommendAdapter()
    }


}