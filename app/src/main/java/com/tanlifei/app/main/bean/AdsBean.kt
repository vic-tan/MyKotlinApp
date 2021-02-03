package com.tanlifei.app.main.bean

import com.common.base.bean.BaseLitePalBean
import java.io.Serializable

/**
 * @desc:广告实体
 * @author: tanlifei
 * @date: 2021/2/3 18:08
 */

class AdsBean(
    val name: String,
    val type: Int,//跳转类型:0=站外H5,1=站外APP,2=精品课程,3=学习历史,4=本周课表,5=全部直播,6=联系客服
    val url: String,
    val poster: String,
    val tabletPoster: String,
    val duration: Int,
    val status: Int//状态:0=隐藏,1=正常
) : BaseLitePalBean() ,Serializable{
    override val modelId: Long
        get() = 1
}