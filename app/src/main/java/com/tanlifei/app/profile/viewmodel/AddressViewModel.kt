package com.tanlifei.app.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.bean.UserBean
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.profile.bean.AddressBean
import com.tanlifei.app.profile.bean.AreaBean
import com.tanlifei.app.profile.bean.AreaJsonBean


/**
 * @desc:地址管理ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class AddressViewModel(mUser: UserBean) : BaseViewModel() {


    var user: UserBean = mUser
    var addressBean: AddressBean? = null
    var areaJsonList: MutableList<AreaJsonBean> = mutableListOf()
    var options1Items: MutableList<AreaBean> = mutableListOf()
    var options2Items: MutableList<MutableList<AreaBean>> = mutableListOf()
    var options3Items: MutableList<MutableList<MutableList<AreaBean>>> = mutableListOf()


    /**
     * 地址数据加载完成的LveData
     */
    val addressDataComplete: LiveData<AddressBean> get() = _addressDataComplete
    private var _addressDataComplete = MutableLiveData<AddressBean>()

    /**
     * 地区数据加载完成的LveData
     */
    val areaDataComplete: LiveData<Boolean> get() = _areaDataComplete
    private var _areaDataComplete = MutableLiveData<Boolean>()

    /**
     * 的LveData
     */
    val editAddressComplete: LiveData<Int> get() = _editAddressComplete
    private var _editAddressComplete = MutableLiveData<Int>()

    /**
     * 是否修改过LveData
     */
    var isUpdate: Boolean = false

    /**
     * 获取省市区JSON
     */
    fun requestAreaJsonList() = launchBySilence {
        var data = ApiNetwork.requestAreaJsonList()
        if (ObjectUtils.isNotEmpty(data)) {
            areaJsonList = data
            analysisAreaJson(data)
            _areaDataComplete.value = true
        }
    }

    /**
     * 更新收货地址
     */
    fun requestEidtGoodsAddress() = launchByLoading {
        addressBean?.let {
            var id = ApiNetwork.requestEidtGoodsAddress(
                user.goodsAddress == 0L,
                it
            )
            if (ObjectUtils.isNotEmpty(id)) {
                _editAddressComplete.value = id.toInt()
            }

        }
    }

    /**
     * 获取收货地址
     */
    fun requestGoodsAddress() {
        launchBySilence {
            var address = ApiNetwork.requestGoodsAddress(user.goodsAddress)
            if (ObjectUtils.isNotEmpty(address)) {
                addressBean = address
                _addressDataComplete.value = addressBean
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
            options3Items.clear()

            var provinceList: MutableList<AreaBean> = mutableListOf() //该省（第一级）

            var cityList: MutableList<AreaBean> //该省的城市列表（第二级）
            var provinceAreaList: MutableList<MutableList<AreaBean>> //该省的所有地区列表（第三极）
            for (p in areaList) { //遍历省份
                options1Items.add(AreaBean(p.id, p.pid, p.name))
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
                options2Items.add(cityList)
                /**
                 * 添加地区数据
                 */
                options3Items.add(provinceAreaList)
            }
            /**
             * 添加省钩
             */
            options1Items.addAll(provinceList)
        }
    }


}