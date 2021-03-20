package com.common.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/9 17:57
 */
class ImageLoader: XPopupImageLoader {
    override fun loadImage(position: Int, url: Any, imageView: ImageView) {
        //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
        Glide.with(imageView).load(url)
            .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
            .into(imageView)
    }

    override fun getImageFile(mContext: Context, uri: Any): File? {
        try {
            return Glide.with(mContext).downloadOnly().load(uri).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}