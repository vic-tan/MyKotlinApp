package com.onlineaginguniversity.circle.utils

import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import java.util.*

/**
 * @desc:图片URl地址裁剪
 * @author: tanlifei
 * @date: 2021/4/15 11:13
 */
object ImageUrlUtils {
    private val urlList: MutableList<String> =
        ArrayList()
    private val density = ScreenUtils.getScreenDensity() * 0.5

    init {
        //后台问题
        urlList.add("lndxappcdn.jinlingkeji.cn")//腾讯云图片
        urlList.add("hwcdn.jinlingkeji.cn")//华为云图片
    }


    fun getUrl(url: String?, width: Int): String? {
        val isHuaWeiYunUrl = isHuaWeiYunUrl(url)
        return if (isHuaWeiYunUrl) {
            "$url?x-image-process=image/resize,m_lfit,w_${(width * density).toInt()}"
        } else {
            url
        }
    }

    private fun isHuaWeiYunUrl(url: String?): Boolean {
        if (!StringUtils.isEmpty(url)) {
            for (str in urlList) {
                if (url!!.contains(str)) {
                    return true
                }
            }
        }
        return false
    }
}