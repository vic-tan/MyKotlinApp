package com.tanlifei.app.common.widget.video

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/29 18:01
 */
object AutoPlayUtils {
    var positionInList = -1 //记录当前播放列表位置

    /**
     * @param firstVisiblePosition 首个可见item位置
     * @param lastVisiblePosition  最后一个可见item位置
     */
    fun onScrollPlayVideo(
        recyclerView: RecyclerView,
        jzvdId: Int,
        firstVisiblePosition: Int,
        lastVisiblePosition: Int
    ) {
        try {
            for (i in 0..lastVisiblePosition - firstVisiblePosition) {
                val child = recyclerView.getChildAt(i)
                val player = child.findViewById<View>(jzvdId)
                if (player != null && player is Jzvd) {
                    val viewVisiblePercent = getViewVisiblePercent(player)
                    if (viewVisiblePercent > 0.85 && viewVisiblePercent <= 1f) {
                        if (positionInList != i + firstVisiblePosition) {
                            if (player.state != JzvdStd.STATE_PLAYING) {
                                player.startButton.performClick()
                            }
                        }
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param firstVisiblePosition 首个可见item位置
     * @param lastVisiblePosition  最后一个可见item位置
     * @param percent              当item被遮挡percent/1时释放,percent取值0-1
     */
    fun onScrollReleaseAllVideos(
        firstVisiblePosition: Int,
        lastVisiblePosition: Int,
        percent: Float
    ) {
        if (Jzvd.CURRENT_JZVD == null) return
        if (positionInList >= 0) {
            if (positionInList <= firstVisiblePosition || positionInList >= lastVisiblePosition - 1) {
                if (getViewVisiblePercent(Jzvd.CURRENT_JZVD) < percent) {
                    Jzvd.releaseAllVideos()
                }
            }
        }
    }

    /**
     * @param view
     * @return 当前视图可见比列
     */
    private fun getViewVisiblePercent(view: View?): Float {
        if (view == null) {
            return 0f
        }
        val height = view.height.toFloat()
        val rect = Rect()
        if (!view.getLocalVisibleRect(rect)) {
            return 0f
        }
        val visibleHeight = rect.bottom - rect.top.toFloat()
        return visibleHeight / height
    }
}