package com.onlineaginguniversity.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.bean.UserBean
import com.common.base.viewmodel.BaseViewModel
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.profile.bean.AddressBean
import com.onlineaginguniversity.profile.bean.AreaBean
import com.onlineaginguniversity.profile.bean.AreaJsonBean


/**
 * @desc:地址管理ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class AddressViewModel(mUser: UserBean) : BaseViewModel() {


    var mUser: UserBean = mUser
    var mAddressBean: AddressBean? = null
    var mAreaJsonList: MutableList<AreaJsonBean> = mutableListOf()
    var mOptions1Items: MutableList<AreaBean> = mutableListOf()
    var mOptions2Items: MutableList<MutableList<AreaBean>> = mutableListOf()
    var mOptions3Items: MutableList<MutableList<MutableList<AreaBean>>> = mutableListOf()


    /**
     * 地址数据加载完成的LveData
     */
    val mAddressDataComplete: LiveData<AddressBean> get() = addressDataComplete
    private var addressDataComplete = MutableLiveData<AddressBean>()

    /**
     * 地区数据加载完成的LveData
     */
    val mAreaDataComplete: LiveData<Boolean> get() = areaDataComplete
    private var areaDataComplete = MutableLiveData<Boolean>()

    /**
     * 的LveData
     */
    val mEditAddressComplete: LiveData<Int> get() = editAddressComplete
    private var editAddressComplete = MutableLiveData<Int>()


    /**
     * 获取省市区JSON
     */
    fun requestAreaJsonList() = comRequest({
        var data = Repository.requestAreaJsonList()
        if (ObjectUtils.isNotEmpty(data)) {
            mAreaJsonList = data
            analysisAreaJson(data)
            areaDataComplete.value = true
        }
    }, uiLiveData = false)

    /**
     * 更新收货地址
     */
    fun requestEidtGoodsAddress() = comRequest({
        mAddressBean?.let {
            var id = Repository.requestEidtGoodsAddress(
                mUser.goodsAddress == 0L,
                it
            )
            if (ObjectUtils.isNotEmpty(id)) {
                editAddressComplete.value = id.toInt()
            }

        }
    })

    /**
     * 获取收货地址
     */
    fun requestGoodsAddress() {
        comRequest({
            var address = Repository.requestGoodsAddress(mUser.goodsAddress)
            if (ObjectUtils.isNotEmpty(address)) {
                mAddressBean = address
                addressDataComplete.value = mAddressBean
            }
        }, uiLiveData = false)
    }

    /**
     * 解析省份数据
     */
    private fun analysisAreaJson(areaList: MutableList<AreaJsonBean>) {
        if (ObjectUtils.isNotEmpty(areaList)) {
            mOptions1Items.clear()
            mOptions2Items.clear()
            mOptions3Items.clear()

            var provinceList: MutableList<AreaBean> = mutableListOf() //该省（第一级）

            var cityList: MutableList<AreaBean> //该省的城市列表（第二级）
            var provinceAreaList: MutableList<MutableList<AreaBean>> //该省的所有地区列表（第三极）
            for (p in areaList) { //遍历省份
                mOptions1Items.add(AreaBean(p.id, p.pid, p.name))
                cityList = mutableListOf()
                provinceAreaList = mutableListOf()
                var cityAreaList: MutableList<AreaBean> //该城市的所有地区列表
                for (c in p.areaListVOList) { //遍历该省份的所有城市
                    cityList.add(AreaBean(c.id, c.pid, c.name))
                    cityAreaList = mutableListOf()
                    cityAreaList.addAll(c.areaList)
                    provinceAreaList.add(cityAreaList) //添加该省所有地区数据
                }
                /**
                 * 添加城市数据
                 */
                mOptions2Items.add(cityList)
                /**
                 * 添加地区数据
                 */
                mOptions3Items.add(provinceAreaList)
            }
            /**
             * 添加省钩
             */
            mOptions1Items.addAll(provinceList)
        }
    }


}