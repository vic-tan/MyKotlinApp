package com.common.utils

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.common.widget.popup.ImageViewerPopup
import com.lxj.xpopup.XPopup
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/18 17:25
 */
object PhotoUtils {

    fun show(context: Context, imageView: ImageView, url: String?) {
        //自定义的弹窗需要用asCustom来显示，之前的asImageViewer这些方法当然不能用了。
        val viewerPopup = ImageViewerPopup(context)
        //自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
        viewerPopup.setSingleSrcView(imageView, url)
        viewerPopup.setXPopupImageLoader(ImageLoader())
        XPopup.Builder(context)
            .isDestroyOnDismiss(true)
            .asCustom(viewerPopup)
            .show()
    }

    fun showBanner(
        context: Context?,
        imageView: ImageView,
        position: Int,
        pager2: ViewPager2,
        list: List<String>
    ) {
        XPopup.Builder(context).asImageViewer(
            imageView, position, list,
            { popupView, position ->
                pager2.setCurrentItem(position, false)
                //一定要post，因为setCurrentItem内部实现是RecyclerView.scrollTo()，这个是异步的
                pager2.post(Runnable { //由于ViewPager2内部是包裹了一个RecyclerView，而RecyclerView始终维护一个子View
                    val rv = pager2.getChildAt(0) as RecyclerView
                    //再拿子View，就是ImageView
                    popupView.updateSrcView(rv.getChildAt(0) as ImageView)
                })
            }, ImageLoader()
        ).show()
    }

}