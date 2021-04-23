package com.onlineaginguniversity.circle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.base.viewmodel.BaseViewModel
import com.onlineaginguniversity.common.bean.FollowResponse
import com.onlineaginguniversity.common.bean.PraiseResponse
import com.onlineaginguniversity.common.repository.Repository

/**
 * @desc:同学圈公共接口ViewModel
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class CircleViewModel : BaseViewModel() {

    /**
     * 关注/取消关注改变的LveData
     */
    val mFollowChanged: LiveData<FollowResponse> get() = followChanged
    private var followChanged = MutableLiveData<FollowResponse>()

    /**
     * 点赞/取消点赞改变的LveData
     */
    val mPraiseChanged: LiveData<PraiseResponse.PraiseResult> get() = praiseChanged
    private var praiseChanged = MutableLiveData<PraiseResponse.PraiseResult>()

    /**
     * 删除改变的LveData
     */
    val mDeleteChanged: LiveData<Int> get() = deleteChanged
    private var deleteChanged = MutableLiveData<Int>()

    /**
     * 关注/取消关注
     */
    fun requestFollow(id: Long, isFollow: Boolean, position: Int?) {
        comRequest({
            var mfollowResponse: FollowResponse = Repository.requestFollow(id, isFollow)
            mfollowResponse.position = position
            followChanged.value = mfollowResponse
        })
    }

    /**
     * 点赞/取消点赞
     */
    fun requestPraise(id: Long, isPraise: Boolean, position: Int?) {
        comRequest({
            var mPraiseResponse: PraiseResponse = Repository.requestPraise(id, isPraise)
            mPraiseResponse?.let {
                mPraiseResponse.obj?.let {
                    it.position = position
                    praiseChanged.value = it
                }
            }
        })
    }

    /**
     * 删除
     */
    fun requestDelete(id: Long,position: Int?) {
        comRequest({
            Repository.requestEntertainmentDelete(id)
            deleteChanged.value = position
        })
    }
}