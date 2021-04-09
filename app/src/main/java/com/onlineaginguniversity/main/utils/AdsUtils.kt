package com.onlineaginguniversity.main.utils

import com.onlineaginguniversity.main.bean.AdsBean
import org.litepal.LitePal

/**
 * @desc:广告工具类
 * @author: tanlifei
 * @date: 2021/2/3 18:13
 */
object AdsUtils {
    /**
     * 获取用户信息
     */
    fun getAds(): AdsBean? = LitePal.findLast(AdsBean::class.java)

}