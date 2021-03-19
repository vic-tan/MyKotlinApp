package com.tanlifei.app.profile.bean

import com.google.gson.annotations.SerializedName

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/25 18:30
 */
data class AreaJsonBean(
    var id: Long = 0,
    val name: String = "",
    val pid: Int = 0,
    @SerializedName(value = "areaListVOList", alternate = ["areaList"])
    var areaListVOList: List<CityListBean>
) {

}