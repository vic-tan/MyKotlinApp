package com.onlineaginguniversity.common.widget.component.share.utils

import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.Platform.ShareParams
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import com.common.constant.GlobalEnumConst
import com.onlineaginguniversity.common.widget.component.share.bean.ShareBean
import com.onlineaginguniversity.common.widget.component.share.listener.ShareListener
import java.util.*

/**
 * @desc:ShareSdk 分享配置
 * @author: tanlifei
 * @date: 2021/4/15 14:35
 */
object ShareSdkUtils {

    /** ------------------------------------ 微信分享 -------------------------------------*/
    private fun comWx(
        shareBean: ShareBean,
        shareType: Int,
        name: String,
        listener: ShareListener?
    ) {
        var sp = shareContent(shareBean)
        sp.shareType = shareType
        share(sp, name, listener)
    }

    /**
     * 分享微信好友文案链接
     */
    fun wechat(
        shareBean: ShareBean,
        listener: ShareListener?
    ) {
        comWx(shareBean, Platform.SHARE_WEBPAGE, Wechat.NAME, listener)
    }

    /**
     * 分享微信朋友圈文案链接
     */
    fun wechatMoments(
        shareBean: ShareBean,
        listener: ShareListener?
    ) {

        comWx(shareBean, Platform.SHARE_WEBPAGE, WechatMoments.NAME, listener)
    }


    /**
     * 分享微信好友图片
     */
    fun wechatImage(
        imagePath: String,
        listener: ShareListener?
    ) {
        val sp = ShareParams()
        sp.imagePath = imagePath
        sp.shareType = Platform.SHARE_IMAGE
        share(sp, Wechat.NAME, listener)
    }

    /**
     * 分享微信朋友圈图片
     */
    fun wechatMomentsImage(
        imagePath: String,
        listener: ShareListener?
    ) {
        val sp = ShareParams()
        sp.imagePath = imagePath
        sp.shareType = Platform.SHARE_IMAGE
        share(sp, WechatMoments.NAME, listener)
    }


    private fun shareContent(bean: ShareBean): ShareParams {
        val sp = ShareParams()
        //title标题
        sp.title = bean.title

        //Url网络链接
        sp.url = bean.targetUrl
        sp.titleUrl = bean.targetUrl

        //内容
        sp.text = bean.subTitle

        //图片
        sp.imageUrl = bean.targetImgUrl
        return sp
    }


    /**
     * 开始分享
     *
     * @param listener
     * @param sp
     */
    private fun share(
        sp: ShareParams,
        name: String,
        listener: ShareListener?
    ) {
        val platform = ShareSDK.getPlatform(name)
        var type = when (name) {
            Wechat.NAME -> GlobalEnumConst.ShareType.WECHAT//微信
            WechatMoments.NAME -> GlobalEnumConst.ShareType.WECHATMOMENTS//朋友圈
            else -> GlobalEnumConst.ShareType.OTHER
        }
        platform.SSOSetting(false)
        platform.platformActionListener = object : PlatformActionListener {
            override fun onComplete(
                platform: Platform,
                i: Int,
                hashMap: HashMap<String, Any>
            ) {
                listener?.let { it.onComplete(type) }
            }

            override fun onError(
                platform: Platform,
                i: Int,
                throwable: Throwable
            ) {
                listener?.let { it.onError(type, throwable) }
            }

            override fun onCancel(platform: Platform, i: Int) {
                listener?.let { it.onCancel(type) }
            }
        }
        platform.share(sp)
    }
}