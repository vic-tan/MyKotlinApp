package com.tanlifei.app.circle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.base.viewmodel.BaseViewModel
import com.tanlifei.app.common.bean.FollowResponse
import com.tanlifei.app.common.bean.PraiseResponse
import com.tanlifei.app.common.repository.Repository

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
}