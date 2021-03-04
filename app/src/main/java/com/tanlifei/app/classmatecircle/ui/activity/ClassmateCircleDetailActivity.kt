package com.tanlifei.app.classmatecircle.ui.activity

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComApplication
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.utils.*
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.adapter.CommentAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.classmatecircle.viewmodel.ClassmateCircleDetailViewModel
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ActivityClassmateCircleDetailBinding
import com.tanlifei.app.databinding.ItemHeaderClassmateCircleDetailBinding


/**
 * @desc:详情界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ClassmateCircleDetailActivity :
    BaseToolBarActivity<ActivityClassmateCircleDetailBinding, ClassmateCircleDetailViewModel>(),
    View.OnClickListener {
    private lateinit var adapter: CommentAdapter
    private lateinit var header: ViewBinding

    companion object {
        fun actionStart(id: Long) {
            var intent =
                Intent(
                    ActivityUtils.getTopActivity(),
                    ClassmateCircleDetailActivity::class.java
                ).apply {
                    putExtra(GlobalConst.Extras.ID, id)
                }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun createViewModel(): ClassmateCircleDetailViewModel {
        return ClassmateCircleDetailViewModel(intent.getLongExtra(GlobalConst.Extras.ID, 0))
    }

    override fun init() {
        initHeaderView()
        initAdapter()
        initViewModelObserve()
        initListener()
        initData()
    }

    private fun initHeaderView() {
        header = ItemHeaderClassmateCircleDetailBinding.inflate(layoutInflater)
        header.root.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        RecyclerUtils.uiBehaviorObserve(
            binding.refreshLayout.smartRefreshLayout, binding.refreshLayout.refreshLoadingLayout,
            viewModel, this, true
        )
        viewModel.dataChanged.observe(this, Observer {
            when (it) {
                BaseListViewModel.DataChagedType.REFRESH -> {
                    binding.refreshLayout.smartRefreshLayout.finishRefresh()
                    setHeaderData()
                    adapter.removeHeaderView(header)
                    adapter.addHeaderView(header)
                    adapter.refreshItemRange()
                }
                BaseListViewModel.DataChagedType.LOADMORE -> {
                    binding.refreshLayout.smartRefreshLayout.finishLoadMore()
                    adapter.loadmoreItemRange(viewModel.loadMoreStartPos)

                }
                BaseListViewModel.DataChagedType.ERROE -> {
                    binding.refreshLayout.smartRefreshLayout.finishRefresh()
                    binding.refreshLayout.smartRefreshLayout.finishLoadMore()
                }
                else -> {
                    binding.refreshLayout.smartRefreshLayout.finishRefresh()
                    binding.refreshLayout.smartRefreshLayout.finishLoadMore()
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setHeaderData() {
        val headerBinding = header as ItemHeaderClassmateCircleDetailBinding
        var bean: ClassmateCircleBean? = viewModel.bean
        GlideUtils.load(ComApplication.context, bean?.image?.url, headerBinding.banner)
        GlideUtils.loadAvatar(
            ComApplication.context,
            bean?.avatar,
            headerBinding.userHead
        )
        headerBinding.name.text = bean?.nickName
        headerBinding.school.text = bean?.universityName
        headerBinding.content.text = bean?.content
        headerBinding.commentTime.text = bean?.createtimeStr + ""
        if (bean?.isFollower === 1) {
            if (bean.isFollower === 1) {
                headerBinding.attention.text = "相互关注"
            } else {
                headerBinding.attention.text = "已关注"
            }
        } else {
            headerBinding.attention.text = "  关注  "
        }
        headerBinding.topicLayout.visibility =
            if (ObjectUtils.isEmpty(bean?.entertainmentTopicName)) View.GONE else View.VISIBLE
        headerBinding.topicTxt.text = bean?.entertainmentTopicName + ""
        headerBinding.totalCommentCount.text =
            bean?.comment?.let { "共${NumberUtils.setCommentCount("0", it)}条评论" }
        binding.commentBtn.text = bean?.comment?.let { NumberUtils.setCommentCount("评论", it) }
        binding.praiseBtn.text = bean?.star?.let { NumberUtils.setPraiseCount(it) }
        binding.praiseBtn.helper.iconNormalLeft =
            ResUtils.getDrawable(if (ObjectUtils.isNotEmpty(bean) && bean?.isStar!!) R.mipmap.ic_praise_white_pre else R.mipmap.ic_praise_white)
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        ViewUtils.setOnClickListener(this)
    }

    private fun initAdapter() {
        adapter = CommentAdapter()
        adapter.mData = viewModel.mData as MutableList<CommentBean>
    }

    private fun initData() {
        binding.refreshLayout.refreshRecycler.adapter = adapter
        RecyclerUtils.initRecyclerView(this, binding.refreshLayout.refreshRecycler)
        RecyclerUtils.initRefreshLayoutListener(binding.refreshLayout.smartRefreshLayout, viewModel)
        RecyclerUtils.initLoadingLayoutListener(
            binding.refreshLayout.refreshLoadingLayout,
            viewModel
        )
        RecyclerUtils.initData(viewModel)
    }

    override fun onClick(v: View) {
        if (AntiShakeUtils.isInvalidClick(v)) return

    }
}