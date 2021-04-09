package com.onlineaginguniversity.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.bean.UserBean
import com.common.base.viewmodel.BaseViewModel
import com.common.constant.GlobalEnumConst
import com.common.widget.component.extension.toast
import com.obs.services.model.PutObjectResult
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.common.utils.HuaweiUploadManager
import com.onlineaginguniversity.common.utils.UserInfoUtils
import com.onlineaginguniversity.profile.bean.AreaBean
import com.onlineaginguniversity.profile.bean.AreaJsonBean
import com.onlineaginguniversity.profile.bean.UniversityBean


/**
 * @desc:个人资料ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class ProfileViewModel : BaseViewModel() {

    var mUserBean: UserBean? = null

    var mAreaJsonList: MutableList<AreaJsonBean> = mutableListOf()
    var mOptions1Items: MutableList<AreaBean> = mutableListOf()
    var mOptions2Items: MutableList<MutableList<AreaBean>> = mutableListOf()

    var mUniversityOptionsItems = mutableListOf<UniversityBean>()

    /**
     * 刷新用户信息
     */
    val mRefreshUserInfo: LiveData<UserBean> get() = refreshUserInfo
    private val refreshUserInfo = MutableLiveData<UserBean>()

    /**
     * 刷新地区学校信息
     */
    val mRefreshUniversityList: LiveData<MutableList<UniversityBean>> get() = refreshUniversityList
    private val refreshUniversityList = MutableLiveData<MutableList<UniversityBean>>()

    /**
     * 数据改变的LveData
     */
    val mDataChanged: LiveData<UserBean> get() = dataChanged
    private var dataChanged = MutableLiveData<UserBean>()

    /**
     * 地区数据加载完成的LveData
     */
    val mAreaDataComplete: LiveData<Boolean> get() = areaDataComplete
    private var areaDataComplete = MutableLiveData<Boolean>()


    fun saveUser(url: String) {
        if (url.isNotEmpty()) {
            var uploadList: MutableList<String> = mutableListOf()
            uploadList.add(url)
            setUI(GlobalEnumConst.UiType.LOADING)
            HuaweiUploadManager().statJob(
                HuaweiUploadManager.EnterType.ENTER_TYPE_CLASSMATE,
                uploadList,
                object : HuaweiUploadManager.HuaweiResultListener {
                    override fun onResult(resultList: MutableList<PutObjectResult>) {
                        mUserBean?.avatar = resultList[0].objectUrl
                        requestUpdateUser()
                    }

                    override fun onFail(errorType: HuaweiUploadManager.UploadType) {
                        toast("上传图片失败了")
                        setUI(GlobalEnumConst.UiType.ERROR)
                    }

                }
            )

        }

    }

    fun requestUpdateUser() = comRequest({
        val user = Repository.requestUpdateUser(mUserBean!!)
        toast("保存完成")
        dataChanged.value = mUserBean
    })


    /**
     * 请求用户信息
     */
    fun requestUser() = comRequest({
        val user = Repository.requestUserInfo()
        if (ObjectUtils.isNotEmpty(user)) {
            mUserBean = user
            refreshUserInfo.value = mUserBean
            mUserBean?.areaId?.let { requestUniversity(it) }!!
        } else {
            findUserByDB()
        }
    }, onError = {
        findUserByDB()
    })

    /**
     * 获取省市区JSON
     */
    fun requestAreaJsonList() = comRequest({
        var data = Repository.requestUniversityAreaList()
        if (ObjectUtils.isNotEmpty(data)) {
            mAreaJsonList = data
            analysisAreaJson(data)
            areaDataComplete.value = true
        }
    }, uiLiveData = false)

    /**
     * 获取合作大学存在的省区
     */
    fun requestUniversity(id: Long) = comRequest({
        mUniversityOptionsItems.clear()
        val universityList = Repository.requestUniversity(id)
        if (ObjectUtils.isNotEmpty(universityList)) {
            mUniversityOptionsItems = universityList
            refreshUniversityList.value = universityList
        }
    }, uiLiveData = false)


    /**
     * 查找数据库中是否保存广告
     */
    private fun findUserByDB() {
        if (ObjectUtils.isEmpty(mUserBean)) {
            mUserBean = UserInfoUtils.getUser()
            if (ObjectUtils.isNotEmpty(mUserBean)) {
                refreshUserInfo.value = mUserBean
                mUserBean?.areaId?.let { requestUniversity(it) }
            }
        }
    }


    /**
     * 解析省份数据
     */
    private fun analysisAreaJson(areaList: MutableList<AreaJsonBean>) {
        if (ObjectUtils.isNotEmpty(areaList)) {
            mOptions1Items.clear()
            mOptions2Items.clear()

            var provinceList: MutableList<AreaBean> = mutableListOf() //该省（第一级）

            var cityList: MutableList<AreaBean> //该省的城市列表（第二级）
            for (p in areaList) { //遍历省份
                mOptions1Items.add(AreaBean(p.id, p.pid, p.name))
                cityList = mutableListOf()
                for (c in p.areaListVOList) { //遍历该省份的所有城市
                    cityList.add(AreaBean(c.id, c.pid, c.name))
                }
                /**
                 * 添加城市数据
                 */
                mOptions2Items.add(cityList)
            }
            /**
             * 添加省钩
             */
            mOptions1Items.addAll(provinceList)
        }
    }
}