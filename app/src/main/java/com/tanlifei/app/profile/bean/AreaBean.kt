package com.tanlifei.app.profile.bean

import com.contrarywind.interfaces.IPickerViewData

/**
 * @desc:省市区实体
 * @author: tanlifei
 * @date: 2021/2/25 18:27
 */
data class AreaBean(
    val id: Long = 0,
    val pid: Int = 0,
    val name: String = "",
    val level: Int = 0
) : IPickerViewData {
    override fun getPickerViewText(): String? {
        return name
    }
}