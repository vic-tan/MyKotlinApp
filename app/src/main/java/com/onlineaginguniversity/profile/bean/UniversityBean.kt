package com.onlineaginguniversity.profile.bean

import com.contrarywind.interfaces.IPickerViewData

/**
 * @desc:学校
 * @author: tanlifei
 * @date: 2021/3/19 15:40
 */
data class UniversityBean(val id: Long, val name: String, val addressId: Long) : IPickerViewData {
    override fun getPickerViewText(): String {
        return name
    }
}

