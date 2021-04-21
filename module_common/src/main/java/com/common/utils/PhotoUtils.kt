package com.common.utils

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.widget.component.extension.color
import com.common.widget.component.popup.ImageViewerPopup
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/18 17:25
 */
object PhotoUtils {


    /**
     * 显示单张
     */
    fun showSinglePhoto(mContext: Context?, imageView: ImageView, url: String?) {
        if (ObjectUtils.isNotEmpty(url) && null != mContext) {
            var list = mutableListOf<String>()
            list.add(url!!)
            showXPopup(mContext, initImageViewerPopup(mContext, imageView, 0, list))
        }
    }

    /**
     * 列表
     */
    fun showListPhoto(
        mContext: Context?,
        imageView: ImageView,
        position: Int,
        list: List<String>,
        srcViewUpdateListener: OnSrcViewUpdateListener
    ) {
        if (ObjectUtils.isNotEmpty(list) && null != mContext) {
            val viewerPopup = initImageViewerPopup(mContext, imageView, position, list)
            viewerPopup.setSrcViewUpdateListener(srcViewUpdateListener)
            showXPopup(mContext, viewerPopup)
        }
    }

    /**
     * 显示本地图片
     */
    fun showLocalListPhoto(
        mContext: Context?,
        imageView: ImageView,
        position: Int,
        list: List<String>,
        srcViewUpdateListener: OnSrcViewUpdateListener
    ) {
        if (ObjectUtils.isNotEmpty(list) && null != mContext) {
            val viewerPopup = initImageViewerPopup(mContext, imageView, position, list, false)
            viewerPopup.setSrcViewUpdateListener(srcViewUpdateListener)
            showXPopup(mContext, viewerPopup)
        }
    }


    /**
     * viewpage2 显示大图
     */
    fun showBannerPhoto(
        mContext: Context?,
        imageView: ImageView,
        position: Int,
        pager2: ViewPager2,
        list: List<String>
    ) {
        if (ObjectUtils.isNotEmpty(list) && null != mContext) {
            val viewerPopup = initImageViewerPopup(mContext, imageView, position, list)
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
            showXPopup(mContext, viewerPopup)
        }


    }

    private fun showXPopup(mContext: Context, viewerPopup: ImageViewerPopup) {
        XPopup.Builder(mContext)
            .isDestroyOnDismiss(true)
            .asCustom(viewerPopup)
            .show()
    }

    private fun initImageViewerPopup(
        mContext: Context,
        imageView: ImageView,
        position: Int,
        list: List<String>,
        showSaveBtn: Boolean = true
    ): ImageViewerPopup {
        val viewerPopup = ImageViewerPopup(mContext)
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
        viewerPopup.isShowSaveButton(showSaveBtn)
        viewerPopup.setXPopupImageLoader(ImageLoader())
        return viewerPopup
    }


}