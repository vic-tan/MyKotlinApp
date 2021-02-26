package com.common.core.base.bean

import java.io.Serializable

/**
 * @desc:用户信息
 * @author: tanlifei
 * @date: 2021/1/28 16:02
 */
class UserBean : BaseLitePalBean(),Serializable {
    override val modelId: Long
        get() = id
    var id: Long = 0
    var token: String = ""
    var avatar: String = ""
    var nickname: String = ""
    var address: String = ""
    var university: Long = 0
    var universityName: String = ""
    var gender: String = ""
    var age: String = ""
    var goodsAddress: Long = 0L
    var score: Long = 0
    var areaId: Long = 0
    var points: Long = 0
    var mobile: String = ""
    var recommendCategoryList: String = ""
    var bio: String = ""
    var email: String = ""
    var hasStudyCard: Long = 0
    var studyCardEndTime: String = ""
    var level: Int = 0
    var levelImg: String = ""
    var isLecturer: Int = 0
}