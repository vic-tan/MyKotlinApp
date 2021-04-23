package com.onlineaginguniversity.circle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.BaseViewModel
import com.common.constant.GlobalEnumConst
import com.common.widget.component.extension.log
import com.common.widget.component.extension.toast
import com.luck.picture.lib.entity.LocalMedia
import com.obs.services.model.ProgressListener
import com.obs.services.model.ProgressStatus
import com.obs.services.model.PutObjectResult
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.circle.bean.ImageBean
import com.onlineaginguniversity.circle.bean.TopicTagBean
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.common.utils.HuaweiUploadManager


/**
 * @desc:发布视频ViewModel
 * @author: tanlifei
 * @date: 2021/4/20 15:50
 */
class CircleReleaseViewModel(var result: List<LocalMedia>?, var isVideos: Boolean) :
    BaseViewModel() {

    /**
     * 列表数据改变的LveData
     */
    val mDataChanged: LiveData<Boolean> get() = dataChanged
    private var dataChanged = MutableLiveData<Boolean>()

    var mCategoryList: MutableList<CategoryBean> = mutableListOf()
    var mTopicList: MutableList<TopicTagBean> = mutableListOf()

    /**
     * 请求网络上传是否正在加载的LveData
     */
    val mUploadChange: LiveData<GlobalEnumConst.UiType> get() = uploadChange
    private val uploadChange = MutableLiveData<GlobalEnumConst.UiType>()

    /**
     * 上传进度变化 加载的LveData
     */
    val mUploadProgress: LiveData<Int> get() = uploadProgress
    private val uploadProgress = MutableLiveData<Int>()


    fun requestData() {
        comRequest({
            var categoryList = Repository.requestEntertainmentCategory()
            if (ObjectUtils.isNotEmpty(categoryList)) {
                for (c in categoryList) {
                    //为0的时候，添加的时候，才显示
                    if (c.type == 0) {
                        mCategoryList.add(c)
                    }
                }
            }
            val other = CategoryBean("其它", categoryId = -1)
            mCategoryList.add(other)
            mTopicList = Repository.requestEntertainmentTopic()
            dataChanged.value = true
        })
    }

    /**
     * 上传资源
     */
    fun startUpload(
        content: String,
        categoryId: Long?,
        entertainmentTopicId: Long?,
        originalList: MutableList<LocalMedia>
    ) {
        if (ObjectUtils.isNotEmpty(originalList)) {
            uploadChange.value = GlobalEnumConst.UiType.LOADING
            var uploadList = mutableListOf<String>()
            for (l in originalList) {
                if (isVideos) {
                    uploadList.add(l.realPath)
                } else {
                    uploadList.add(l.compressPath)
                }
            }
            HuaweiUploadManager().statJob(
                HuaweiUploadManager.EnterType.ENTER_TYPE_CLASSMATE,
                uploadList,
                object : HuaweiUploadManager.HuaweiResultListener {
                    override fun onResult(resultList: MutableList<PutObjectResult>) {
                        var requestList = mutableListOf<ImageBean>()
                        var imageBean: ImageBean
                        var url: String? = null
                        for ((i, r) in resultList.withIndex()) {
                            if (isVideos) {
                                url = r.objectUrl
                            } else {
                                imageBean = ImageBean(
                                    r.objectUrl,
                                    originalList[i].width,
                                    originalList[i].height
                                )
                                requestList.add(imageBean)
                            }
                        }
                        requestAdd(
                            content,
                            categoryId,
                            url,
                            entertainmentTopicId,
                            requestList
                        )
                    }

                    override fun onFail(errorType: HuaweiUploadManager.UploadType) {
                        uploadChange.value = GlobalEnumConst.UiType.ERROR
                    }

                },
                ProgressListener {
                    log(it.transferPercentage)
                    uploadProgress.postValue(it.transferPercentage)
                }
            )
        } else {
            toast("上传图片不正确")
        }

    }

    /**
     * 把上传信息同步到后台
     */
    fun requestAdd(
        content: String,
        categoryId: Long?,
        videoUrl: String?,
        entertainmentTopicId: Long?,
        uploadList: MutableList<ImageBean>
    ) {
        rxLifeScope.launch({
            Repository.requestEntertainmentAdd(
                content,
                if (isVideos) 1 else 0,
                categoryId,
                videoUrl,
                entertainmentTopicId,
                uploadList
            )
            uploadChange.value = GlobalEnumConst.UiType.SUCCESS
        }, {
            uploadChange.value = GlobalEnumConst.UiType.ERROR
        }, {
            uploadChange.value = GlobalEnumConst.UiType.LOADING
        }, {
            uploadChange.value = GlobalEnumConst.UiType.COMPLETE
        })
    }
}