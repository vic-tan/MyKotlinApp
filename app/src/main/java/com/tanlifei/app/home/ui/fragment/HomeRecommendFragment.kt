package com.tanlifei.app.home.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.listener.OnItemListener
import com.common.core.base.ui.fragment.BaseRecyclerBVMFragment
import com.common.core.share.ShareBean
import com.common.core.share.listener.OnShareListener
import com.common.core.share.ui.ShareView
import com.common.utils.ImageLoader
import com.common.widget.LoadingLayout
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding
import com.tanlifei.app.databinding.ItemFollowBinding


/**
 * @desc:首页Tab推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeRecommendFragment : BaseRecyclerBVMFragment<FragmentFollowBinding, FollowViewModel>() {

    private lateinit var adapter: FollowAdapter

    companion object {
        fun newInstance(): HomeRecommendFragment {
            val fragment = HomeRecommendFragment()
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
        adapter = FollowAdapter(context)
        adapter.mData = viewModel.mData as MutableList<ClassmateCircleBean>
        adapter.setOnItemChildClickListener(object :
            OnItemListener<ItemFollowBinding> {
            override fun onItemClick(v: View, binding: ItemFollowBinding, position: Int) {
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
                                            ToastUtils.show(type.name)
                                        }

                                    })
                            ).show()
                        }
                    }
                    binding.banner -> {
                        var list = mutableListOf<Any>()
                        var url = (viewModel.mData[position] as ClassmateCircleBean).imageUrl
                        url?.let { list.add(it) }
                        XPopup.Builder(binding.banner.context)
                            .asImageViewer(
                                binding.banner,
                                0,
                                list as List<Any>?,
                                true,
                                true,
                                -1,
                                -1,
                                -1,
                                true,
                                Color.rgb(32, 36, 46),
                                { popupView, position ->
                                    val rv =
                                        binding.banner.parent as RecyclerView
                                    popupView.updateSrcView(rv.getChildAt(position) as ImageView)
                                },
                                ImageLoader()
                            )
                            .show()

                    }
                }
            }
        })
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