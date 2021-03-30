package com.tanlifei.app.circle.bean

import java.io.Serializable

/**
 * @desc:图片实体类
 * @author: tanlifei
 * @date: 2021/2/8 10:33
 */
data class ImageBean(var url: String? = null, var width: Int = 360, var height: Int = 480) :
    Serializable {}