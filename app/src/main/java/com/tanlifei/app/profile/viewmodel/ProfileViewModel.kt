package com.tanlifei.app.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.bean.UserBean
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.common.utils.UserInfoUtils


/**
 * @desc:个人资料ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class ProfileViewModel() : BaseViewModel() {

    var userBean: UserBean? = null

    /**
     * 刷新用户信息
     */
    val refreshUserInfo: LiveData<UserBean> get() = mRefreshUserInfo
    private val mRefreshUserInfo = MutableLiveData<UserBean>()

    /**
     * 列表数据改变的LveData
     */
    val dataChanged: LiveData<Boolean> get() = mDataChanged
    private var mDataChanged = MutableLiveData<Boolean>()

    /**
     * 请求用户信息
     */
    fun requestUser() = launchBySilence({
        val user = ApiNetwork.requestUserInfo()
        if (ObjectUtils.isNotEmpty(user)) {
            userBean = user
            mRefreshUserInfo.value = userBean
        }else{
            findUserByDB()
        }
    }, {
        findUserByDB()
    })


    /**
     * 查找数据库中是否保存广告
     */
    private fun findUserByDB() {
        if (ObjectUtils.isEmpty(userBean)) {
            userBean = UserInfoUtils.getUser()
            if (ObjectUtils.isNotEmpty(userBean)) {
                mRefreshUserInfo.value = userBean
            }
        }
    }

}