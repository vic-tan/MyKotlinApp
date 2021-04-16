package com.onlineaginguniversity.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.BaseViewModel
import com.onlineaginguniversity.common.widget.component.share.bean.ShareBean
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.repository.Repository


/**
 * @desc:公用请求，需要可继承该ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class ShareViewModel : BaseViewModel() {


    var shareBean: ShareBean? = null

    /**
     * 密码登录结果
     */
    val mShareResult: LiveData<ShareBean> get() = shareResult
    private val shareResult = MutableLiveData<ShareBean>()


    /**
     * 分享接口
     * 模块code
     * 1话题分享2直播专栏分享3文娱图片分享4课程分享5文娱视频分享6自己作业分享7名师分享8直播分享9回播分享10同学的作业
     * 11邀请好友12结课证书13专题分享14电台音频分享15学习周报
     */
    fun requestShare(id: Long?, moduleCode: EnumConst.ShareModuleCode?) = comRequest({
        if (ObjectUtils.isNotEmpty(id) && ObjectUtils.isNotEmpty(moduleCode)) {
            shareBean = Repository.requestShare(id!!, moduleCode!!)
            shareResult.value = shareBean
        }
    }, uiLiveData = false)

}