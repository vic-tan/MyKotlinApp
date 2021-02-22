package com.tanlifei.app.classmatecircle.bean

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/22 14:09
 */
data class CategoryBean(
    val name: String = "未命名",
    val iconDefault: String? = "",
    val iconSelect: String? = "",
    val categoryId: Long = 0,
    val type: Int = 0,
    var check: Boolean = false
)