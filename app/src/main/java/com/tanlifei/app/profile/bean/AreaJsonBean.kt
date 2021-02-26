package com.tanlifei.app.profile.bean

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/25 18:30
 */
data class AreaJsonBean(
    var id: Int = 0,
    val name: String = "",
    val pid: Int = 0,
    var areaListVOList: List<CityListBean>
) {

}