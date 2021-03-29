package com.tanlifei.app.classmatecircle.ui.fragment

import android.view.View
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.ui.fragment.BaseRvFragment
import com.common.widget.share.ShareBean
import com.common.widget.share.listener.OnShareListener
import com.common.widget.share.ui.ShareView
import com.common.utils.PhotoUtils
import com.common.widget.extension.toast
import com.lxj.xpopup.XPopup
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding
import com.tanlifei.app.databinding.ItemFollowBinding


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment :
    BaseRvFragment<FragmentFollowBinding, FollowViewModel, CircleBean>() {


    companion object {
        fun newInstance() = FollowFragment()
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
    }

    override fun setAdapter(): CommonRvAdapter<CircleBean> {
        return FollowAdapter()
    }

    override fun itemClick(
        holder: ViewBinding,
        itemBean: CircleBean,
        v: View,
        position: Int
    ) {
        holder as ItemFollowBinding
        when (v) {
            holder.more,
            holder.shareLayout -> {
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
            holder.banner -> {
                var list = mutableListOf<String>()
                var url = itemBean.image?.url
                url?.let { list.add(it) }
                PhotoUtils.showListPhoto(context, holder.banner, position, list)
            }
        }
    }

}