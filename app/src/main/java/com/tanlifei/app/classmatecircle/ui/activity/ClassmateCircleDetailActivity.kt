package com.tanlifei.app.classmatecircle.ui.activity

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.ComFun
import com.common.cofing.constant.GlobalConst
import com.common.core.base.listener.OnMultiItemListener
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.databinding.LayoutLoadingEmptyBinding
import com.common.utils.*
import com.common.utils.extension.clickListener
import com.common.utils.extension.color
import com.common.widget.popup.BottomInputEditView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.adapter.CommentAdapter
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.classmatecircle.viewmodel.ClassmateCircleDetailViewModel
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ActivityClassmateCircleDetailBinding
import com.tanlifei.app.databinding.ItemCommentBinding
import com.tanlifei.app.databinding.ItemHeaderClassmateCircleDetailBinding


/**
 * @desc:详情界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ClassmateCircleDetailActivity :
    BaseToolBarActivity<ActivityClassmateCircleDetailBinding, ClassmateCircleDetailViewModel>(){
    private var screenWidth = ScreenUtils.getScreenWidth()
    private lateinit var adapter: CommentAdapter
    private lateinit var header: ViewBinding
    private lateinit var emptyView: ViewBinding

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
        header.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        emptyView = LayoutLoadingEmptyBinding.inflate(layoutInflater)
        emptyView.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ConvertUtils.dp2px(300f)
        )
        emptyView.root.setBackgroundColor(color(R.color.white))
        val emptyBinding = emptyView as LayoutLoadingEmptyBinding
        emptyBinding.emptyText.text = "暂无评论，说两句吧~"
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
                    binding.refreshLayout.smartRefreshLayout.setEnableLoadMore(true)
                    binding.refreshLayout.smartRefreshLayout.finishRefresh()
                    addHeader()
                    if (viewModel.mData.isEmpty()) {
                        adapter.notifyDataSetChanged()
                        binding.refreshLayout.smartRefreshLayout.setEnableLoadMore(false) //将不会再次触发加载更多事件
                    } else {
                        adapter.refreshItemRange()
                    }
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
                    addEmptyView()
                    adapter.notifyDataSetChanged()
                }
            }
        })
        viewModel.itemDataChanged.observe(this, Observer {
            adapter.notifyItemChanged(
                adapter.mHeaderViews.size + it
            )
            adapter.removeHeaderView(emptyView)

        })
        viewModel.beanChanged.observe(this, Observer {
            refreshHeaderData()
        })
    }

    private fun addHeader() {
        if (viewModel.pageNum == 1) {
            refreshHeaderData()
            adapter.removeHeaderView(header)
            adapter.addHeaderView(header)
            addEmptyView()
        }
    }

    private fun addEmptyView() {
        if (viewModel.mData.isEmpty()) {
            adapter.removeHeaderView(emptyView)
            adapter.addHeaderView(emptyView)
        } else {
            adapter.removeHeaderView(emptyView)
        }
    }

    private fun refreshHeaderData() {
        val headerBinding = header as ItemHeaderClassmateCircleDetailBinding
        var bean: ClassmateCircleBean? = viewModel.bean
        headerBinding.banner.layoutParams.width = screenWidth
        headerBinding.banner.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, bean?.image)
        GlideUtils.load(ComFun.context, bean?.image?.url, headerBinding.banner)
        GlideUtils.loadAvatar(
            ComFun.context,
            bean?.avatar,
            headerBinding.userHead
        )
        headerBinding.name.text = bean?.nickName
        headerBinding.school.text = bean?.universityName
        headerBinding.school.visibility =
            if (ObjectUtils.isEmpty(bean?.universityName)) View.GONE else View.VISIBLE
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
        headerBinding.banner.setOnClickListener {
            XPopup.Builder(this)
                .asImageViewer(
                    headerBinding.banner,
                    bean?.image?.url, ImageLoader()
                )
                .show()
        }

        binding.commentBtn.text = bean?.comment?.let { NumberUtils.setCommentCount("评论", it) }
        binding.praiseBtn.text = bean?.star?.let { NumberUtils.setPraiseCount(it) }
        binding.praiseIcon.setImageResource(if (ObjectUtils.isNotEmpty(bean) && bean?.isStar!!) R.mipmap.ic_praise_white_pre else R.mipmap.ic_praise_white)

    }


    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(binding.commentLayout, binding.input,
            clickListener = View.OnClickListener {
                when (it) {
                    binding.commentLayout,
                    binding.input -> {
                        ComDialogUtils.showInputEditView(
                            this,
                            object : BottomInputEditView.CallBack {
                                override fun callback(inputText: String) {
                                    viewModel.requestComment(inputText)
                                }
                            })
                    }
                }
            })
    }

    private fun initAdapter() {
        adapter = CommentAdapter()
        adapter.mData = viewModel.mData as MutableList<CommentBean>
        adapter.setItemClickListener(object : OnMultiItemListener<CommentBean> {
            override fun click(
                holder: ViewBinding,
                itemBean: CommentBean,
                v: View,
                position: Int
            ) {
                when (holder) {
                    is ItemCommentBinding -> {
                        when (v) {
                            holder.delete -> {
                                ComDialogUtils.comConfirm(this@ClassmateCircleDetailActivity,
                                    "确定删除些评论?",
                                    OnConfirmListener { viewModel.requestDeleteComment(itemBean) })
                            }
                        }
                    }
                }
            }

        })
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


}