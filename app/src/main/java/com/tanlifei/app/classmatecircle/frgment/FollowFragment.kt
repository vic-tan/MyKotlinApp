package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.listener.OnItemListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.XPopup
import com.tanlifei.app.R
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
            val fragment = FollowFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewModel(): FollowViewModel {
        return FollowViewModel()
    }

    override fun onFirstVisibleToUser() {
        initRecycler(
            binding.refreshLayout.smartRefreshLayout,
            binding.refreshLayout.refreshLoadingLayout,
            binding.refreshLayout.refreshRecycler
        )
    }


    override fun initView() {
        super.initView()
        adapter = FollowAdapter()
        adapter.mData = viewModel.mData as MutableList<ClassmateCircleBean>
        adapter.setOnItemChildClickListener(object :
            OnItemListener {
            override fun onItemClick(v: View, position: Int) {
                when (v?.id) {
                    R.id.more,
                    R.id.share_layout -> {
                        context?.let {
                            XPopup.Builder(it).asCustom(
                                ShareView(
                                    it,
                                    ShareBean("网上老年大学", "https://www.baidu.com", "test 分享", ""),
                                    object :
                                        OnShareListener {
                                        override fun onItemClick(
                                            v: View,
                                            type: ShareView.ShareType
                                        ) {
                                            ToastUtils.show(type.name)
                                        }

                                    })
                            ).show()
                        }
                    }
                }
            }
        })
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }


}