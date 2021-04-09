package com.onlineaginguniversity.profile.bean

/**
 * @desc: 市实体
 * @author: tanlifei
 * @date: 2021/2/25 18:30
 */
data class CityListBean(
    var id: Long = 0,
    val name: String = "",
    val pid: Int = 0,
    val level: Int = 0,
    var areaList: List<AreaBean>
) {
}