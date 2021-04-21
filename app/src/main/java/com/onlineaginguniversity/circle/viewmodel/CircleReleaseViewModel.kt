package com.onlineaginguniversity.circle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.BaseViewModel
import com.luck.picture.lib.entity.LocalMedia
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.circle.bean.TopicTagBean
import com.onlineaginguniversity.common.repository.Repository


/**
 * @desc:发布视频ViewModel
 * @author: tanlifei
 * @date: 2021/4/20 15:50
 */
class CircleReleaseViewModel(var result: List<LocalMedia>?, var isVideo: Boolean) :
    BaseViewModel() {

    /**
     * 列表数据改变的LveData
     */
    val mDataChanged: LiveData<Boolean> get() = dataChanged
    private var dataChanged = MutableLiveData<Boolean>()

    var mCategoryList: MutableList<CategoryBean> = mutableListOf()
    var mTopicList: MutableList<TopicTagBean> = mutableListOf()


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


}