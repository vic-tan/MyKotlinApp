package com.common.utils

import android.content.Context
import android.widget.ImageView
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.common.R
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * @desc:Glide 工具类
 * @author: tanlifei
 * @date: 2021/2/19 9:30
 */
object GlideUtils {


    /**
     * 默认glide，不做任何处理，glide 从字符串中加载图片（网络地址或者本地地址）
     *
     * @param resId
     * @return
     */
    fun load(
        mContext: Context?,
        url: String?,
        view: ImageView,
        defaultId: Int = R.mipmap.bg_default_img
    ) {
        if (ObjectUtils.isNotEmpty(mContext)
            && ObjectUtils.isNotEmpty(view)
            && ObjectUtils.isNotEmpty(url)
        ) {
            Glide.with(mContext!!)
                .load(url)
                .apply(RequestOptions.placeholderOf(defaultId))
                .dontAnimate()
                .into(view)
        }
    }

    /**
     * 加载头像,Glide 不做圆角，让控件做圆角
     */
    fun loadAvatar(mContext: Context?, url: String?, view: ImageView) {
        if (ObjectUtils.isNotEmpty(mContext)
            && ObjectUtils.isNotEmpty(view)
            && ObjectUtils.isNotEmpty(url)
        ) {
            load(mContext, url, view, R.mipmap.ic_default_avatar)
        }
    }

    /**
     * 加载高斯模糊图片处理
     */
    fun loadBlur(
        mContext: Context?,
        url: String,
        view: ImageView,
        defaultBlurId: Int,
        radius: Int = 24,
        sampling: Int = 5
    ) {
        if (ObjectUtils.isNotEmpty(mContext)
            && ObjectUtils.isNotEmpty(view)
            && ObjectUtils.isNotEmpty(url)
        ) {
            Glide.with(mContext!!)
                .load(url)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
                .apply(RequestOptions.placeholderOf(defaultBlurId))
                .dontAnimate()
                .into(view)
        }
    }

    /**
     * 恢复请求，一般在停止滚动的时候
     *
     * @param mContext
     */
    fun resumeRequests(mContext: Context?) {
        if (ObjectUtils.isNotEmpty(mContext)) {
            Glide.with(mContext!!).resumeRequests()
        }
    }

    /**
     * 暂停请求 正在滚动的时候
     *
     * @param mContext
     */
    fun pauseRequests(mContext: Context?) {
        if (ObjectUtils.isNotEmpty(mContext)) {
            Glide.with(mContext!!).pauseRequests()
        }
    }

    /**
     * 清理磁盘缓存
     * 理磁盘缓存 需要在子线程中执行
     * @param mContext
     */
    fun clearDiskCache(mContext: Context?) {
        //理磁盘缓存 需要在子线程中执行
        if (ObjectUtils.isNotEmpty(mContext)) {
            Glide.get(mContext!!).clearDiskCache()
        }
    }

    /**
     * 清理内存缓存
     * 清理内存缓存  可以在UI主线程中进行
     * @param mContext
     */
    fun clearMemory(mContext: Context?) {
        //清理内存缓存  可以在UI主线程中进行
        if (ObjectUtils.isNotEmpty(mContext)) {
            Glide.get(mContext!!).clearMemory()
        }
    }
}