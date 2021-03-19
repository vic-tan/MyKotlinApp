package com.common.utils

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.utils.extension.color
import com.common.widget.popup.ImageViewerPopup
import com.lxj.xpopup.XPopup

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/18 17:25
 */
object PhotoUtils {


    /**
     * 显示单张
     */
    fun showSinglePhoto(context: Context?, imageView: ImageView, url: String?) {
        if (ObjectUtils.isNotEmpty(url) && null != context) {
            var list = mutableListOf<String>()
            list.add(url!!)
            showXPopup(context, initImageViewerPopup(context, imageView, 0, list))
        }
    }

    /**
     * 列表
     */
    fun showListPhoto(
        context: Context?,
        imageView: ImageView,
        position: Int,
        list: List<String>
    ) {

        if (ObjectUtils.isNotEmpty(list) && null != context) {
            val viewerPopup = initImageViewerPopup(context, imageView, position, list)
            viewerPopup.setSrcViewUpdateListener { popupView, _ ->
                val rv =
                    imageView.parent as RecyclerView
                popupView.updateSrcView(rv.getChildAt(0) as ImageView)
            }
            showXPopup(context, viewerPopup)
        }
    }


    /**
     * viewpage2 显示大图
     */
    fun showBannerPhoto(
        context: Context?,
        imageView: ImageView,
        position: Int,
        pager2: ViewPager2,
        list: List<String>
    ) {
        if (ObjectUtils.isNotEmpty(list) && null != context) {
            val viewerPopup = initImageViewerPopup(context, imageView, position, list)
            viewerPopup.setSrcViewUpdateListener { popupView, position ->
                pager2.setCurrentItem(position, false)
                //一定要post，因为setCurrentItem内部实现是RecyclerView.scrollTo()，这个是异步的
                //由于ViewPager2内部是包裹了一个RecyclerView，而RecyclerView始终维护一个子View
                pager2.post {
                    val rv = pager2.getChildAt(0) as RecyclerView
                    //再拿子View，就是ImageView
                    popupView.updateSrcView(rv.getChildAt(0) as ImageView)
                }
            }
            showXPopup(context, viewerPopup)
        }


    }

    private fun showXPopup(context: Context, viewerPopup: ImageViewerPopup) {
        XPopup.Builder(context)
            .isDestroyOnDismiss(true)
            .asCustom(viewerPopup)
            .show()
    }

    private fun initImageViewerPopup(
        context: Context,
        imageView: ImageView,
        position: Int,
        list: List<String>
    ): ImageViewerPopup {
        val viewerPopup = ImageViewerPopup(context)
        //自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
        viewerPopup.setSingleSrcView(imageView, if (list.size > 1) list else list[0])
        if (list.size > 1) {
            viewerPopup.setSrcView(imageView, position)
            viewerPopup.setImageUrls(list)
        } else {
            viewerPopup.setSingleSrcView(imageView, list[0])
        }
        viewerPopup.isShowSaveButton(false)
        viewerPopup.isShowIndicator(false)
        viewerPopup.setBgColor(color(R.color.black))
        viewerPopup.setXPopupImageLoader(ImageLoader())
        return viewerPopup
    }


}