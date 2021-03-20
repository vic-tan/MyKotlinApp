package com.tanlifei.app.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.bean.UserBean
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.extension.toast
import com.obs.services.model.PutObjectResult
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.common.utils.HuaweiUploadManager
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.profile.bean.AreaBean
import com.tanlifei.app.profile.bean.AreaJsonBean
import com.tanlifei.app.profile.bean.UniversityBean


/**
 * @desc:个人资料ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class ProfileViewModel() : BaseViewModel() {

    var userBean: UserBean? = null

    var areaJsonList: MutableList<AreaJsonBean> = mutableListOf()
    var options1Items: MutableList<AreaBean> = mutableListOf()
    var options2Items: MutableList<MutableList<AreaBean>> = mutableListOf()

    var universityOptionsItems = mutableListOf<UniversityBean>()

    /**
     * 刷新用户信息
     */
    val refreshUserInfo: LiveData<UserBean> get() = _refreshUserInfo
    private val _refreshUserInfo = MutableLiveData<UserBean>()

    /**
     * 刷新地区学校信息
     */
    val refreshUniversityList: LiveData<MutableList<UniversityBean>> get() = _refreshUniversityList
    private val _refreshUniversityList = MutableLiveData<MutableList<UniversityBean>>()

    /**
     * 数据改变的LveData
     */
    val dataChanged: LiveData<UserBean> get() = _dataChanged
    private var _dataChanged = MutableLiveData<UserBean>()

    /**
     * 地区数据加载完成的LveData
     */
    val areaDataComplete: LiveData<Boolean> get() = _areaDataComplete
    private var _areaDataComplete = MutableLiveData<Boolean>()


    fun saveUser(url: String) {
        if (url.isNotEmpty()) {
            var uploadList: MutableList<String> = mutableListOf()
            uploadList.add(url)
            mLoadingState.value = LoadType.LOADING
            HuaweiUploadManager().statJob(
                HuaweiUploadManager.EnterType.ENTER_TYPE_CLASSMATE,
                uploadList,
                object : HuaweiUploadManager.HuaweiResultListener {
                    override fun onResult(resultList: MutableList<PutObjectResult>) {
                        userBean?.avatar = resultList[0].objectUrl
                        requestUpdateUser()
                    }

                    override fun onFail(errorType: HuaweiUploadManager.UploadType) {
                        toast("上传图片失败了")
                        mLoadingState.value = LoadType.ERROR
                    }

                }
            )

        }

    }

    fun requestUpdateUser() = launchByLoading {
        val user = ApiNetwork.requestUpdateUser(userBean!!)
        toast("保存完成")
        _dataChanged.value = userBean
    }


    /**
     * 请求用户信息
     */
    fun requestUser() = launchBySilence({
        val user = ApiNetwork.requestUserInfo()
        if (ObjectUtils.isNotEmpty(user)) {
            userBean = user
            _refreshUserInfo.value = userBean
            userBean?.areaId?.let { requestUniversity(it) }
        } else {
            findUserByDB()
        }
    }, {
        findUserByDB()
    })

    /**
     * 获取省市区JSON
     */
    fun requestAreaJsonList() = launchBySilence {
        var data = ApiNetwork.requestUniversityAreaList()
        if (ObjectUtils.isNotEmpty(data)) {
            areaJsonList = data
            analysisAreaJson(data)
            _areaDataComplete.value = true
        }
    }

    /**
     * 获取合作大学存在的省区
     */
    fun requestUniversity(id: Long) = launchBySilence {
        universityOptionsItems.clear()
        val universityList = ApiNetwork.requestUniversity(id)
        if (ObjectUtils.isNotEmpty(universityList)) {
            universityOptionsItems = universityList
            _refreshUniversityList.value = universityList
        }
    }


    /**
     * 查找数据库中是否保存广告
     */
    private fun findUserByDB() {
        if (ObjectUtils.isEmpty(userBean)) {
            userBean = UserInfoUtils.getUser()
            if (ObjectUtils.isNotEmpty(userBean)) {
                _refreshUserInfo.value = userBean
                userBean?.areaId?.let { requestUniversity(it) }
            }
        }
    }


    /**
     * 解析省份数据
     */
    private fun analysisAreaJson(areaList: MutableList<AreaJsonBean>) {
        if (ObjectUtils.isNotEmpty(areaList)) {
            options1Items.clear()
            options2Items.clear()

            var provinceList: MutableList<AreaBean> = mutableListOf() //该省（第一级）

            var cityList: MutableList<AreaBean> //该省的城市列表（第二级）
            for (p in areaList) { //遍历省份
                options1Items.add(AreaBean(p.id, p.pid, p.name))
                cityList = mutableListOf()
                for (c in p.areaListVOList) { //遍历该省份的所有城市
                    cityList.add(AreaBean(c.id, c.pid, c.name))
                }
                /**
                 * 添加城市数据
                 */
                options2Items.add(cityList)
            }
            /**
             * 添加省钩
             */
            options1Items.addAll(provinceList)
        }
    }
}