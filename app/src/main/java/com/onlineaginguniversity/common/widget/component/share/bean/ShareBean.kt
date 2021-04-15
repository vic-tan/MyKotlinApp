package com.onlineaginguniversity.common.widget.component.share.bean

/**
 * @desc:分享实体
 * @author: tanlifei
 * @date: 2021/2/25 15:05
 */
data class ShareBean(
    val title: String,
    val targetUrl: String,
    val subTitle: String,
    val targetImgUrl: String? = "https://appxw.jinlingkeji.cn/app_icon.png"//默认分享图片，防止为空时报错
) {
}