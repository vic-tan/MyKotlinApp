package com.tanlifei.app.classmatecircle.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.common.utils.PhotoUtils
import com.common.utils.extension.toast
import com.common.widget.LoadingLayout
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
        initRecycler()
    }


    override fun initView() {
        super.initView()
        mAdapter = FollowAdapter()
        mAdapter.mData = mViewModel.mData as MutableList<ClassmateCircleBean>
        mAdapter.setItemClickListener(object :
            OnItemClickListener<ItemFollowBinding, ClassmateCircleBean> {
            override fun click(
                binding: ItemFollowBinding,
                bean: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
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
                        PhotoUtils.showListPhoto(context,binding.banner,position,list)
                    }
                }
            }


        })
    }

    override fun setAdapter():  Any {
        return mAdapter
    }

    override fun smartRefreshLayout(): SmartRefreshLayout {
        return mBinding.refreshLayout.smartRefreshLayout
    }

    override fun refreshLoadingLayout(): LoadingLayout {
        return mBinding.refreshLayout.refreshLoadingLayout
    }

    override fun refreshRecycler(): RecyclerView {
        return mBinding.refreshLayout.refreshRecycler
    }

}