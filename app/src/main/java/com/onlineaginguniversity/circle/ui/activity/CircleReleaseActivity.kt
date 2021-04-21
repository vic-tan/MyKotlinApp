package com.onlineaginguniversity.circle.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.base.listener.OnItemClickListener
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.utils.FullyGridLayoutManager
import com.common.utils.PermissionUtils
import com.common.utils.PhotoUtils
import com.common.utils.PictureSelectorUtils
import com.common.widget.component.extension.click
import com.common.widget.component.extension.dp2px
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.tools.ScreenUtils
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import com.onlineaginguniversity.circle.adapter.CategoryTagAdapter
import com.onlineaginguniversity.circle.adapter.ReleaseUploadAdapter
import com.onlineaginguniversity.circle.adapter.TopicTagAdapter
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.circle.bean.TopicTagBean
import com.onlineaginguniversity.circle.viewmodel.CircleReleaseViewModel
import com.onlineaginguniversity.databinding.*


/**
 * @desc:同学圈发布界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class CircleReleaseActivity :
    BaseToolBarActivity<ActivityCircleReleaseBinding, CircleReleaseViewModel>() {

    lateinit var mAdapter: ReleaseUploadAdapter
    lateinit var mCategoryTagAdapter: CategoryTagAdapter
    lateinit var mTopicTagAdapter: TopicTagAdapter
    private lateinit var mFooter: ItemReleaseAddBinding

    companion object {
        fun actionStart(result: List<LocalMedia?>, isVideo: Boolean) {
            startActivity<CircleReleaseActivity> {
                putParcelableArrayListExtra(GlobalConst.Extras.LIST, ArrayList(result))
                putExtra(GlobalConst.Extras.TYPE, isVideo)
            }
        }
    }

    override fun createViewModel(): CircleReleaseViewModel {
        return CircleReleaseViewModel(
            intent.getParcelableArrayListExtra(GlobalConst.Extras.LIST),
            intent.getBooleanExtra(GlobalConst.Extras.TYPE, false)
        )
    }

    override fun init() {
        mTitleBar.titleView.gone()
        initViewModelObserve()
        initListener()
        initData()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mDataChanged.observe(this, Observer {
            mCategoryTagAdapter.mData = mViewModel.mCategoryList.toMutableList()
            mCategoryTagAdapter.notifyDataSetChanged()

            mTopicTagAdapter.mData = mViewModel.mTopicList.toMutableList()
            mTopicTagAdapter.notifyDataSetChanged()
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {

    }

    private fun initData() {
        initImageAdatper()
        initCategoryTagAdapter()
        initTopicTagAdapter()
        mViewModel.requestData()
    }

    private fun initImageAdatper() {
        initFooterView()
        mAdapter = ReleaseUploadAdapter(mViewModel.isVideo)
        mViewModel.result?.let {
            mAdapter.mData = it.toMutableList()
        }
        mAdapter.setSelectMax(if (mViewModel.isVideo) 1 else 9)
        val manager = FullyGridLayoutManager(
            this,
            3, GridLayoutManager.VERTICAL, false
        )
        mBinding.uploadRecycler.layoutManager = manager
        mBinding.uploadRecycler.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                ScreenUtils.dip2px(this, 8f), false
            )
        )
        mBinding.uploadRecycler.adapter = mAdapter
        notifyImage()
        mAdapter.setItemClickListener(object : OnItemClickListener<LocalMedia> {
            override fun click(holder: ViewBinding, itemBean: LocalMedia, v: View, position: Int) {
                when (holder) {
                    is ItemReleaseUploadBinding -> {
                        when (v) {
                            holder.delete -> {
                                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                                if (position != RecyclerView.NO_POSITION && mAdapter.mData.size > position) {
                                    mAdapter.mData.removeAt(position)
                                    mAdapter.notifyItemRemoved(position)
                                    mAdapter.notifyItemRangeChanged(position, mAdapter.mData.size)
                                    notifyImage()
                                }
                            }
                            holder.cover -> {
                                var photoList = mutableListOf<String>()
                                for (s in mAdapter.mData as MutableList<LocalMedia>) {
                                    photoList.add(s.compressPath)
                                }
                                PhotoUtils.showLocalListPhoto(
                                    this@CircleReleaseActivity,
                                    holder.cover,
                                    position,
                                    photoList,
                                    OnSrcViewUpdateListener { popupView, index ->
                                        popupView.updateSrcView(
                                            mAdapter.getViewHolder(
                                                mBinding.uploadRecycler,
                                                index
                                            ).cover
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

        })
    }

    private fun initFooterView() {
        mFooter = ItemReleaseAddBinding.inflate(layoutInflater)
        mFooter.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(105F)
        )
        mFooter.add.click {
            PermissionUtils.requestCameraPermission(
                this@CircleReleaseActivity,
                callback = object : PermissionUtils.PermissionCallback {
                    override fun allGranted() {
                        PictureSelectorUtils.createCircle(
                            this@CircleReleaseActivity,
                            mAdapter.mData as MutableList<LocalMedia>
                        )
                            .forResult(object :
                                OnResultCallbackListener<LocalMedia?> {
                                override fun onResult(result: List<LocalMedia?>) {
                                    mAdapter.mData.clear()
                                    for (r in result) {
                                        r?.let {
                                            mAdapter.mData.add(it)
                                        }

                                    }
                                    notifyImage()
                                }

                                override fun onCancel() {
                                }
                            })
                    }
                })
        }
    }

    private fun addFooter() {
        if (mAdapter.isShowAddItem()) {
            mAdapter.removeFooterView(mFooter)
            mAdapter.addFooterView(mFooter)
        } else {
            mAdapter.removeFooterView(mFooter)
        }
    }

    private fun notifyImage() {
        addFooter()
        mAdapter.notifyDataSetChanged()
    }

    private fun initCategoryTagAdapter() {
        mCategoryTagAdapter = CategoryTagAdapter()
        mCategoryTagAdapter.mData = mViewModel.mCategoryList.toMutableList()
        val manager = FullyGridLayoutManager(
            this,
            3, GridLayoutManager.VERTICAL, false
        )
        mBinding.categoryRecycler.layoutManager = manager
        mBinding.categoryRecycler.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                ScreenUtils.dip2px(this, 8f), false
            )
        )
        mBinding.categoryRecycler.adapter = mCategoryTagAdapter
        mCategoryTagAdapter.setItemClickListener(object : OnItemClickListener<CategoryBean> {
            override fun click(
                holder: ViewBinding,
                itemBean: CategoryBean,
                v: View,
                position: Int
            ) {
                when (holder) {
                    is ItemCategoryTagBinding -> {
                        when (v) {
                            holder.rootView -> {
                                mCategoryTagAdapter.check(position)
                            }
                        }
                    }
                }
            }

        })
    }

    private fun initTopicTagAdapter() {
        mTopicTagAdapter = TopicTagAdapter()
        mTopicTagAdapter.mData = mViewModel.mTopicList.toMutableList()
        val manager = LinearLayoutManager(this, GridLayoutManager.HORIZONTAL, false)
        mBinding.topicRecycler.layoutManager = manager
        mBinding.topicRecycler.adapter = mTopicTagAdapter
        mTopicTagAdapter.setItemClickListener(object : OnItemClickListener<TopicTagBean> {
            override fun click(
                holder: ViewBinding,
                itemBean: TopicTagBean,
                v: View,
                position: Int
            ) {
                when (holder) {
                    is ItemTopicTagBinding -> {
                        when (v) {
                            holder.rootView -> {
                                mTopicTagAdapter.check(position)
                            }
                        }
                    }
                }
            }

        })
    }
}