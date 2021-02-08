package com.tanlifei.app.classmatecircle.bean

import java.io.Serializable

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/8 10:33
 */
class ImageBean :Serializable{
    val DEF_IMG_WIDTH = 360
    val DEF_IMG_HEIGHT = 480

    var url: String? = null
    var width = DEF_IMG_WIDTH.toString() + ""
    var height = DEF_IMG_HEIGHT.toString() + ""
    override fun toString(): String {
        return "ImageBean(url=$url, width='$width', height='$height')"
    }
}