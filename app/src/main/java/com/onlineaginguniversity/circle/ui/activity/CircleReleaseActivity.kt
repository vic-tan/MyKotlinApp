package com.onlineaginguniversity.circle.ui.activity

import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.common.base.listener.OnItemClickListener
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.constant.GlobalEnumConst
import com.common.utils.FullyGridLayoutManager
import com.common.utils.PermissionUtils
import com.common.utils.PhotoUtils
import com.common.utils.PictureSelectorUtils
import com.common.widget.component.extension.*
import com.common.widget.component.popup.UploadProgressView
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.tools.BitmapUtils
import com.luck.picture.lib.tools.ScreenUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import com.onlineaginguniversity.circle.adapter.CategoryTagAdapter
import com.onlineaginguniversity.circle.adapter.ReleaseUploadAdapter
import com.onlineaginguniversity.circle.adapter.TopicTagAdapter
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.circle.bean.TopicTagBean
import com.onlineaginguniversity.circle.viewmodel.CircleReleaseViewModel
import com.onlineaginguniversity.common.utils.HuaweiUploadManager
import com.onlineaginguniversity.databinding.*
import java.io.File
import java.io.IOException


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

    //上传加载框
    private lateinit var mUploadHud: UploadProgressView

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
        initUploadHud()
        mTitleBar.titleView.gone()
        initViewModelObserve()
        initListener()
        initData()
    }

    private fun initUploadHud() {
        mUploadHud = XPopup.Builder(this)
            .popupAnimation(PopupAnimation.NoAnimation)
            .hasShadowBg(false)
            .dismissOnBackPressed(false) // 按返回键是否关闭弹窗，默认为true
            .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
            .asCustom(UploadProgressView(this)) as UploadProgressView
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

        mViewModel.mUploadChange.observe(this, Observer {
            if (ObjectUtils.isNotEmpty(mUploadHud)) {
                when (it) {
                    GlobalEnumConst.UiType.LOADING -> {
                        if (mUploadHud.isDismiss) mUploadHud.show()
                        mUploadHud.setTitle("上传中..")
                    }
                    GlobalEnumConst.UiType.COMPLETE -> if (mUploadHud.isShow) mUploadHud.dismiss()
                    GlobalEnumConst.UiType.ERROR -> {
                        if (mUploadHud.isShow) mUploadHud.dismiss()
                        toast("上传失败")
                    }
                    GlobalEnumConst.UiType.SUCCESS -> {
                        if (mUploadHud.isShow) mUploadHud.dismiss()
                        toast("发布成功")
                        ActivityUtils.finishActivity(this@CircleReleaseActivity)
                    }
                    else -> {
                    }
                }
            }
        })
        mViewModel.mUploadProgress.observe(this, Observer {
            if (mUploadHud.isShow)
                mUploadHud.setProgress(it)
        })
    }

    /**
     * 获取视频第一帧
     */
    private fun getVideoCover(): LocalMedia? {
        var cover: LocalMedia? = null
        var video = mAdapter.mData[0] as LocalMedia
        // 获取第一个关键帧
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(video.realPath)
        var videoCover =
            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        try {

            FileUtils.createOrExistsDir("${GlobalConst.FilesUrl.PICTURES}")
            val file = File(
                "${GlobalConst.FilesUrl.PICTURES}/${System.currentTimeMillis()}.jpg"
            )
            BitmapUtils.saveBitmapFile(videoCover, file)
            if (ObjectUtils.isNotEmpty(videoCover)) {
                cover = LocalMedia()
                cover.path = file.absolutePath
                cover.width = videoCover!!.width
                cover.height = videoCover!!.height
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cover
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(mBinding.release,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.release -> {
                        if (ObjectUtils.isEmpty(mAdapter.mData.size)) {
                            toast("请选择图片！")
                            return@OnClickListener
                        }
                        if (mBinding.etInput.text.toString().isEmpty()) {
                            toast("为您的作品添加一个标题吧！")
                            return@OnClickListener
                        }
                        if (ObjectUtils.isEmpty(mCategoryTagAdapter.getCheck())) {
                            toast("请选择分类")
                            return@OnClickListener
                        }

                        if (ObjectUtils.isEmpty(mTopicTagAdapter.getCheck())) {
                            toast("请选择参与话题")
                            return@OnClickListener
                        }
                        if (mViewModel.isVideos) {
                            //+2 是因为还要上传一张封面，请求一个接口
                            mUploadHud.setMaxProgress(mAdapter.mData.size * 100 + 2)
                        } else {
                            //+1 是因为还要请求一个接口
                            mUploadHud.setMaxProgress(mAdapter.mData.size * 100 + 1)
                        }

                        mViewModel.startUpload(
                            mBinding.etInput.text.toString(),
                            mCategoryTagAdapter.getCheck()?.categoryId,
                            mTopicTagAdapter.getCheck()?.id,
                            mAdapter.mData as MutableList<LocalMedia>,
                            getVideoCover()
                        )
                    }
                }
            })
    }

    private fun initData() {
        initImageAdatper()
        initCategoryTagAdapter()
        initTopicTagAdapter()
        mViewModel.requestData()
    }


    private fun initImageAdatper() {
        initFooterView()
        mAdapter = ReleaseUploadAdapter(mViewModel.isVideos)
        mViewModel.result?.let {
            mAdapter.mData = it.toMutableList()
        }
        mAdapter.setSelectMax(if (mViewModel.isVideos) 1 else 9)
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
                                if (!mViewModel.isVideos) {
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

    override fun onDestroy() {
        super.onDestroy()
        HuaweiUploadManager().cancleJob()
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.etInput)
    }
}