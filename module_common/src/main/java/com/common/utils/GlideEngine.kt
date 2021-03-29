package com.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.common.R
import com.common.widget.extension.setVisible
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView


/**
 * @desc:图片选择库图片加载器
 * @author: tanlifei
 * @date: 2021/3/11 15:53
 */
object GlideEngine : ImageEngine {

    /**
     * 加载图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    override fun loadImage(mContext: Context, url: String, imageView: ImageView) {
        GlideUtils.load(mContext, url, imageView)
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param mContext
     * @param url
     * @param imageView
     * @param longImageView
     * @param callback      网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    override fun loadImage(
        mContext: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        GlideUtils.load(mContext, url, imageView)
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param mContext
     * @param url
     * @param imageView
     * @param longImageView
     * @ 已废弃
     */
    override fun loadImage(
        mContext: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
        Glide.with(mContext)
            .asBitmap()
            .load(url)
            .into(object : ImageViewTarget<Bitmap?>(imageView) {
                override fun setResource(resource: Bitmap?) {
                    if (resource != null) {
                        val eqLongImage = MediaUtils.isLongImg(
                            resource.width,
                            resource.height
                        )
                        longImageView!!.setVisible(eqLongImage)
                        imageView.setVisible(!eqLongImage)
                        if (eqLongImage) {
                            // 加载长图
                            longImageView!!.isQuickScaleEnabled = true
                            longImageView!!.isZoomEnabled = true
                            longImageView!!.setDoubleTapZoomDuration(100)
                            longImageView!!.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                            longImageView!!.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                            longImageView.setImage(
                                ImageSource.bitmap(resource),
                                ImageViewState(0f, PointF(0f, 0f), 0)
                            )
                        } else {
                            // 普通图片
                            imageView.setImageBitmap(resource)
                        }
                    }
                }
            })
    }

    /**
     * 加载gif
     *
     * @param mContext   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAsGifImage(mContext: Context, url: String, imageView: ImageView) {
        Glide.with(mContext)
            .asGif()
            .load(url)
            .into(imageView)
    }

    /**
     * 加载图片列表图片
     *
     * @param mContext   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(mContext: Context, url: String, imageView: ImageView) {
        Glide.with(mContext)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .apply(RequestOptions().placeholder(R.mipmap.bg_default_img))
            .into(imageView)
    }

    override fun loadFolderImage(mContext: Context, url: String, imageView: ImageView) {
        Glide.with(mContext)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .centerCrop()
            .sizeMultiplier(0.5f)
            .apply(RequestOptions().placeholder(R.mipmap.bg_default_img))
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.resources, resource)
                    circularBitmapDrawable.cornerRadius = 8f
                    imageView.setImageDrawable(circularBitmapDrawable)
                }

            })
    }

}