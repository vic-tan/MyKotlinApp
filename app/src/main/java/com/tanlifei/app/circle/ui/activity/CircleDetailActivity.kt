package com.tanlifei.app.circle.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.constant.GlobalConst
import com.common.base.listener.OnItemClickListener
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalEnumConst
import com.common.databinding.LayoutLoadingEmptyBinding
import com.common.utils.ComDialogUtils
import com.common.utils.GlideUtils
import com.common.utils.RecyclerUtils
import com.common.widget.component.extension.*
import com.common.widget.component.popup.BottomInputEditView
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tanlifei.app.R
import com.tanlifei.app.circle.adapter.CircleBannerAdapter
import com.tanlifei.app.circle.adapter.CommentAdapter
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.bean.CommentBean
import com.tanlifei.app.circle.viewmodel.CircleDetailViewModel
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ActivityCircleDetailBinding
import com.tanlifei.app.databinding.ItemCommentBinding
import com.tanlifei.app.databinding.ItemHeaderClassmateCircleDetailBinding
import com.youth.banner.indicator.CircleIndicator


/**
 * @desc:详情界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class CircleDetailActivity :
    BaseToolBarActivity<ActivityCircleDetailBinding, CircleDetailViewModel>() {

    private lateinit var mAdapter: CommentAdapter
    private lateinit var mHeader: ItemHeaderClassmateCircleDetailBinding
    private lateinit var mEmptyView: LayoutLoadingEmptyBinding
    private lateinit var bannerAdapter: CircleBannerAdapter

    companion object {
        fun actionStart(id: Long) {
            startActivity<CircleDetailActivity> {
                putExtra(GlobalConst.Extras.ID, id)
            }
        }
    }

    override fun createViewModel(): CircleDetailViewModel {
        return CircleDetailViewModel(intent.getLongExtra(GlobalConst.Extras.ID, 0))
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
        //banner
        bannerAdapter =
            CircleBannerAdapter(mHeader.banner.viewPager2, mViewModel.mBannerData)
        mHeader.banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(bannerAdapter).indicator = CircleIndicator(this)



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
                GlobalEnumConst.UiType.REFRESH -> {
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
        var bean: CircleBean? = mViewModel.mBean
        mHeader.banner.layoutParams.width = screenWidth
        mHeader.banner.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, bean?.image)
        GlideUtils.loadAvatar(
            ComFun.mContext,
            bean?.avatar,
            mHeader.userHead
        )
        mHeader.name.text = bean?.nickName
        mHeader.school.text = bean?.universityName
        mHeader.school.setVisible(ObjectUtils.isNotEmpty(bean?.universityName))
        mHeader.content.text = bean?.content
        mHeader.commentTime.text = bean?.createtimeStr + ""
        if (bean?.isFollower === 1) {
            if (bean.isFollower === 1) {
                mHeader.attention.text = "相互关注"
            } else {
                mHeader.attention.text = "已关注"
            }
        } else {
            mHeader.attention.text = "  关注  "
        }
        mHeader.topicLayout.setVisible(ObjectUtils.isNotEmpty(bean?.entertainmentTopicName))
        mHeader.topicTxt.text = bean?.entertainmentTopicName + ""
        mHeader.totalCommentCount.text =
            bean?.comment?.let { "共${NumberUtils.setCommentCount("0", it)}条评论" }
        mHeader.banner.setDatas(mViewModel.mBannerData)
        mHeader.banner.currentItem = 1
        bannerAdapter.notifyDataSetChanged()
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
                                ComDialogUtils.comConfirm(this@CircleDetailActivity,
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