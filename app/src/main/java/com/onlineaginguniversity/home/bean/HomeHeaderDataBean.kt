package com.onlineaginguniversity.home.bean

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/16 16:04
 */
data class HomeHeaderDataBean(
    var banner: MutableList<BannerBean>,
    var menu: MutableList<MenuBean>,
    var adsnovice: MutableList<AdsnoviceBean>
) {
}