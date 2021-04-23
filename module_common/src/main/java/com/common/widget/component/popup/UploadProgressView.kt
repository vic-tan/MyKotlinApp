package com.common.widget.component.popup

import android.annotation.SuppressLint
import android.content.Context
import com.common.R
import com.common.databinding.LayoutDailogUploadProgressBinding
import com.dinuscxj.progressbar.CircleProgressBar
import com.lxj.xpopup.core.CenterPopupView


/**
 * @desc:上传进度加载框
 * @author: tanlifei
 * @date: 2021/2/1 17:40
 */
class UploadProgressView(mContext: Context) :
    CenterPopupView(mContext), CircleProgressBar.ProgressFormatter {
    private val mDefaultPattern = "%d%%"
    lateinit var mBinding: LayoutDailogUploadProgressBinding

    // 返回自定义弹窗的布局
    override fun getImplLayoutId(): Int {
        return R.layout.layout_dailog_upload_progress
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = LayoutDailogUploadProgressBinding.bind(popupImplView)
        mBinding.progress.max = 100
        mBinding.progress.setProgressFormatter(this)
    }

    /**
     * 设置进度
     */
    fun setProgress(progress: Int) {
        post {
            format(progress, mBinding.progress.max)
        }
    }

    /**
     * 设置最大进度
     */
    fun setMaxProgress(max: Int = 100) {
        post {
            mBinding.progress.max = max
        }
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        post {
            mBinding.tvTitle.text = title
        }
    }

    @SuppressLint("DefaultLocale")
    override fun format(progress: Int, max: Int): CharSequence {
        mBinding.progress.progress = progress
        return java.lang.String.format(
            mDefaultPattern,
            (progress.toFloat() / max.toFloat() * 100).toInt()
        )
    }
}