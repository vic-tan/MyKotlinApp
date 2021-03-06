package com.onlineaginguniversity.common.widget.video

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.common.widget.component.extension.drawable
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.visible
import com.onlineaginguniversity.R

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/29 17:35
 */
class JzvdStdList(context: Context, attrs: AttributeSet) : JzvdStd(context, attrs) {

    override fun init(context: Context?) {
        super.init(context)
        bottomContainer.gone()
        topContainer.gone()
        loadingProgressBar.indeterminateDrawable = drawable(R.drawable.video_progress)
        posterImageView.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    //changeUiTo 真能能修改ui的方法
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        bottomContainer.gone()
        topContainer.gone()
    }

    override fun setAllControlsVisiblity(
        topCon: Int, bottomCon: Int, startBtn: Int, loadingPro: Int,
        posterImg: Int, bottomPro: Int, retryLayout: Int
    ) {
        topContainer.visibility = View.INVISIBLE
        bottomContainer.visibility = View.INVISIBLE
        startButton.visibility = startBtn
        loadingProgressBar.visibility = loadingPro
        posterImageView.visibility = posterImg
        mRetryLayout.visibility = retryLayout
    }

    override fun dissmissControlView() {
        if (state != Jzvd.STATE_NORMAL && state != Jzvd.STATE_ERROR && state != Jzvd.STATE_AUTO_COMPLETE
        ) {
            post {
                bottomContainer.visibility = View.INVISIBLE
                topContainer.visibility = View.INVISIBLE
                startButton.visibility = View.INVISIBLE
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss()
                }
            }
        }
    }

    override fun updateStartImage() {
        when (state) {
            Jzvd.STATE_PLAYING -> {
                startButton.visible()
                startButton.setImageResource(R.mipmap.ic_play_pager)
                replayTextView.gone()
            }
            Jzvd.STATE_ERROR -> {
                startButton.visible()
                replayTextView.gone()
            }
            Jzvd.STATE_AUTO_COMPLETE -> {
                startButton.visible()
                startButton.setImageResource(R.mipmap.ic_restart)
                replayTextView.gone()
            }
            else -> {
                startButton.setImageResource(R.mipmap.ic_play_pager)
                replayTextView.gone()
            }
        }
    }

    override fun showWifiDialog() {
        WIFI_TIP_DIALOG_SHOWED = true
        when (state) {
            STATE_PAUSE -> {
                startButton.performClick()
            }
            else -> {
                startVideo()
            }
        }
    }
}