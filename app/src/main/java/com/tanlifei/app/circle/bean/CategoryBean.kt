package com.tanlifei.app.circle.bean

import com.google.gson.annotations.SerializedName

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/22 14:09
 */
class CategoryBean(
    val name: String = "未命名",
    val iconDefault: String? = "",
    val iconSelect: String? = "",
    @SerializedName(value = "id", alternate = ["categoryId"])
    val categoryId: Long = 0,
    val type: Int = 0,
    var check: Boolean = false
)