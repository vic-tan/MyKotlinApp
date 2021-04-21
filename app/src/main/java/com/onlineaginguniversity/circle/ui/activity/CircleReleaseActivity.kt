package com.onlineaginguniversity.circle.ui.activity

import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvHolder
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
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.tools.ScreenUtils
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import com.onlineaginguniversity.R
import com.onlineaginguniversity.circle.adapter.ReleaseUploadAdapter
import com.onlineaginguniversity.circle.viewmodel.CircleReleaseViewModel
import com.onlineaginguniversity.databinding.ActivityCircleReleaseBinding
import com.onlineaginguniversity.databinding.ItemReleaseAddBinding
import com.onlineaginguniversity.databinding.ItemReleaseUploadBinding


/**
 * @desc:同学圈发布界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class CircleReleaseActivity :
    BaseToolBarActivity<ActivityCircleReleaseBinding, CircleReleaseViewModel>() {

    lateinit var adapter: ReleaseUploadAdapter
    private lateinit var footer: ItemReleaseAddBinding

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

    }

    /**
     * 初始化监听
     */
    private fun initListener() {

    }

    private fun initData() {
        initImageAdatper()
    }

    private fun initImageAdatper() {
        initFooterView()
        adapter = ReleaseUploadAdapter(mViewModel.isVideo)
        mViewModel.result?.let {
            adapter.mData = it.toMutableList()
        }
        adapter.setSelectMax(if (mViewModel.isVideo) 1 else 9)
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
        mBinding.uploadRecycler.adapter = adapter
        notifyImage()
        adapter.setItemClickListener(object : OnItemClickListener<LocalMedia> {
            override fun click(holder: ViewBinding, itemBean: LocalMedia, v: View, position: Int) {
                when (holder) {
                    is ItemReleaseUploadBinding -> {
                        when (v) {
                            holder.delete -> {
                                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                                if (position != RecyclerView.NO_POSITION && adapter.mData.size > position) {
                                    adapter.mData.removeAt(position)
                                    adapter.notifyItemRemoved(position)
                                    adapter.notifyItemRangeChanged(position, adapter.mData.size)
                                    notifyImage()
                                }
                            }
                            holder.cover -> {
                                var photoList = mutableListOf<String>()
                                for (s in adapter.mData as MutableList<LocalMedia>) {
                                    photoList.add(s.compressPath)
                                }
                                PhotoUtils.showListPhoto(
                                    this@CircleReleaseActivity,
                                    holder.cover,
                                    position,
                                    photoList,
                                    OnSrcViewUpdateListener { popupView, index ->
                                        popupView.updateSrcView(
                                            adapter.getViewHolder(
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
        footer = ItemReleaseAddBinding.inflate(layoutInflater)
        footer.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(105F)
        )
        footer.add.click {
            PermissionUtils.requestCameraPermission(
                this@CircleReleaseActivity,
                callback = object : PermissionUtils.PermissionCallback {
                    override fun allGranted() {
                        PictureSelectorUtils.createCircle(
                            this@CircleReleaseActivity,
                            adapter.mData as MutableList<LocalMedia>
                        )
                            .forResult(object :
                                OnResultCallbackListener<LocalMedia?> {
                                override fun onResult(result: List<LocalMedia?>) {
                                    adapter.mData.clear()
                                    for (r in result) {
                                        r?.let {
                                            adapter.mData.add(it)
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
        if (adapter.isShowAddItem()) {
            adapter.removeFooterView(footer)
            adapter.addFooterView(footer)
        } else {
            adapter.removeFooterView(footer)
        }
    }

    private fun notifyImage() {
        addFooter()
        adapter.notifyDataSetChanged()
    }
}