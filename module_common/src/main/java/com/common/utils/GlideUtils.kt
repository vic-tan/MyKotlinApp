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
class GlideUtils {
    companion object {
        /**
         * 默认glide，不做任何处理，glide 从字符串中加载图片（网络地址或者本地地址）
         *
         * @param resId
         * @return
         */
        fun load(context: Context?, url: String?, view: ImageView) {
            if (ObjectUtils.isNotEmpty(context)
                && ObjectUtils.isNotEmpty(view)
                && ObjectUtils.isNotEmpty(url)
            ) {
                Glide.with(context!!).load(url).into(view)
            }
        }

        /**
         * 加载头像,Glide 不做圆角，让控件做圆角
         */
        fun loadAvatar(context: Context?, url: String, view: ImageView) {
            if (ObjectUtils.isNotEmpty(context)
                && ObjectUtils.isNotEmpty(view)
                && ObjectUtils.isNotEmpty(url)
            ) {
                Glide.with(context!!).apply {
                    RequestOptions.placeholderOf(R.mipmap.default_avatar)
                        .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                }.load(url).into(view)
            }
        }



        /**
         * 恢复请求，一般在停止滚动的时候
         *
         * @param context
         */
        fun resumeRequests(context: Context?) {
            if (ObjectUtils.isNotEmpty(context)) {
                Glide.with(context!!).resumeRequests()
            }
        }

        /**
         * 暂停请求 正在滚动的时候
         *
         * @param context
         */
        fun pauseRequests(context: Context?) {
            if (ObjectUtils.isNotEmpty(context)) {
                Glide.with(context!!).pauseRequests()
            }
        }

        /**
         * 清理磁盘缓存
         * 理磁盘缓存 需要在子线程中执行
         * @param context
         */
        fun clearDiskCache(context: Context?) {
            //理磁盘缓存 需要在子线程中执行
            if (ObjectUtils.isNotEmpty(context)) {
                Glide.get(context!!).clearDiskCache()
            }
        }

        /**
         * 清理内存缓存
         * 清理内存缓存  可以在UI主线程中进行
         * @param context
         */
        fun clearMemory(context: Context?) {
            //清理内存缓存  可以在UI主线程中进行
            if (ObjectUtils.isNotEmpty(context)) {
                Glide.get(context!!).clearMemory()
            }
        }
    }
}