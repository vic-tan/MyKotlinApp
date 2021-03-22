package com.tanlifei.app.classmatecircle.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.common.utils.PhotoUtils
import com.common.utils.extension.toast
import com.lxj.xpopup.XPopup
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding
import com.tanlifei.app.databinding.ItemFollowBinding


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseRecyclerBVMFragment<FragmentFollowBinding, FollowViewModel>() {

    private lateinit var mAdapter: FollowAdapter

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
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
    }


    override fun initView() {
        super.initView()
        mAdapter = FollowAdapter()
        mAdapter.mData = mViewModel.mData
        mAdapter.setItemClickListener(object :
            OnItemClickListener<ClassmateCircleBean> {
            override fun click(
                binding: ViewBinding,
                bean: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
                binding as ItemFollowBinding
                when (v) {
                    binding.more,
                    binding.shareLayout -> {
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
                                            toast(type.name)
                                        }

                                    })
                            ).show()
                        }
                    }
                    binding.banner -> {
                        var list = mutableListOf<String>()
                        var url = bean.image?.url
                        url?.let { list.add(it) }
                        PhotoUtils.showListPhoto(context, binding.banner, position, list)
                    }
                }
            }


        })
    }

    override fun setAdapter(): Any {
        return mAdapter
    }


}