package com.tanlifei.app.profile.bean

/**
 * @desc:收货地址实体
 * @author: tanlifei
 * @date: 2021/2/26 9:36
 */
data class AddressBean(
    var id: Long,
    var username: String,
    var mobile: String,
    var provinceId: Int,
    var cityId: Int,
    var areaId: Int,
    var addressPrefix: String,
    var address: String,
    var status: Int,
    var email: String
) {
}