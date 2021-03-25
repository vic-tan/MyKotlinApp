package com.tanlifei.app.classmatecircle.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.cofing.constant.GlobalConst
import com.common.cofing.enumconst.UiType
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.databinding.LayoutLoadingEmptyBinding
import com.common.utils.ComDialogUtils
import com.common.utils.GlideUtils
import com.common.utils.PhotoUtils
import com.common.utils.RecyclerUtils
import com.common.utils.extension.*
import com.common.widget.popup.BottomInputEditView
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
    BaseToolBarActivity<ActivityClassmateCircleDetailBinding, ClassmateCircleDetailViewModel>() {
    private lateinit var mAdapter: CommentAdapter
    private lateinit var mHeader: ViewBinding
    private lateinit var mEmptyView: ViewBinding

    companion object {
        fun actionStart(id: Long) {
            startActivity<ClassmateCircleDetailActivity> {
                putExtra(GlobalConst.Extras.ID, id)
            }
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
        mHeader = ItemHeaderClassmateCircleDetailBinding.inflate(layoutInflater)
        mHeader.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mEmptyView = LayoutLoadingEmptyBinding.inflate(layoutInflater)
        mEmptyView.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ConvertUtils.dp2px(300f)
        )
        mEmptyView.root.setBackgroundColor(color(R.color.white))
        val emptyBinding = mEmptyView as LayoutLoadingEmptyBinding
        emptyBinding.emptyText.text = "暂无评论，说两句吧~"
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        RecyclerUtils.uiObserve(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshLoadingLayout,
            mViewModel, this, isHeaderOrFooter = true, isMoreWithNoMoreData = false
        )
        RecyclerUtils.dataChangeObserve(
            mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>,
            mViewModel,
            this
        ) {
            when (it.uiType) {
                UiType.REFRESH -> {
                    addHeader()
                }
                else -> {
                    addEmptyView()
                }
            }

        }
        mViewModel.mItemDataChanged.observe(this, Observer {
            mAdapter.notifyItemChanged(
                mAdapter.mHeaderViews.size + it
            )
            mAdapter.removeHeaderView(mEmptyView)

        })
        mViewModel.mBeanChanged.observe(this, Observer {
            refreshHeaderData()
        })
    }

    private fun addHeader() {
        if (mViewModel.mPageNum == 1) {
            refreshHeaderData()
            mAdapter.removeHeaderView(mHeader)
            mAdapter.addHeaderView(mHeader)
            addEmptyView()
        }
    }

    private fun addEmptyView() {
        if (mViewModel.mData.isEmpty()) {
            mAdapter.removeHeaderView(mEmptyView)
            mAdapter.addHeaderView(mEmptyView)
        } else {
            mAdapter.removeHeaderView(mEmptyView)
        }
    }

    private fun refreshHeaderData() {
        val headerBinding = mHeader as ItemHeaderClassmateCircleDetailBinding
        var bean: ClassmateCircleBean? = mViewModel.mBean
        headerBinding.banner.layoutParams.width = screenWidth
        headerBinding.banner.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, bean?.image)
        GlideUtils.load(ComFun.mContext, bean?.image?.url, headerBinding.banner)
        GlideUtils.loadAvatar(
            ComFun.mContext,
            bean?.avatar,
            headerBinding.userHead
        )
        headerBinding.name.text = bean?.nickName
        headerBinding.school.text = bean?.universityName
        headerBinding.school.setVisible(ObjectUtils.isNotEmpty(bean?.universityName))
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
        headerBinding.topicLayout.setVisible(ObjectUtils.isNotEmpty(bean?.entertainmentTopicName))
        headerBinding.topicTxt.text = bean?.entertainmentTopicName + ""
        headerBinding.totalCommentCount.text =
            bean?.comment?.let { "共${NumberUtils.setCommentCount("0", it)}条评论" }
        headerBinding.banner.click {
            PhotoUtils.showSinglePhoto(this, headerBinding.banner, bean?.image?.url)
        }
        mBinding.commentBtn.text = bean?.comment?.let { NumberUtils.setCommentCount("评论", it) }
        mBinding.praiseBtn.text = bean?.star?.let { NumberUtils.setPraiseCount(it) }
        mBinding.praiseIcon.setImageResource(if (ObjectUtils.isNotEmpty(bean) && bean?.isStar!!) R.mipmap.ic_praise_white_pre else R.mipmap.ic_praise_white)

    }


    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(mBinding.commentLayout, mBinding.input,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.commentLayout,
                    mBinding.input -> {
                        ComDialogUtils.showInputEditView(
                            this,
                            object : BottomInputEditView.CallBack {
                                override fun callback(inputText: String) {
                                    mViewModel.requestComment(inputText)
                                }
                            })
                    }
                }
            })
    }

    private fun initAdapter() {
        mAdapter = CommentAdapter()
        mAdapter.mData = mViewModel.mData
        mAdapter.setItemClickListener(object : OnItemClickListener<CommentBean> {
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
                                    OnConfirmListener { mViewModel.requestDeleteComment(itemBean) })
                            }
                        }
                    }
                }
            }

        })
    }

    private fun initData() {
        mBinding.refreshLayout.refreshRecycler.adapter = mAdapter
        RecyclerUtils.initRecyclerView(this, mBinding.refreshLayout.refreshRecycler)
        RecyclerUtils.initRefreshLayoutListener(
            mBinding.refreshLayout.smartRefreshLayout,
            mViewModel
        )
        RecyclerUtils.initLoadingLayoutListener(
            mBinding.refreshLayout.refreshLoadingLayout,
            mViewModel
        )
        RecyclerUtils.initData(mViewModel)
    }


}