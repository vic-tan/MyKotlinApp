package com.common.widget.component.popup

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.common.R
import com.common.databinding.LayoutImageViewerPopupBinding
import com.common.utils.PermissionUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.setVisible
import com.lxj.xpopup.core.ImageViewerPopupView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/18 17:27
 */
class ImageViewerPopup(mContext: Context,var showSaveBtn: Boolean = true) :
    ImageViewerPopupView(mContext) {
    lateinit var mBinding: LayoutImageViewerPopupBinding
    override fun getImplLayoutId(): Int {
        return R.layout.layout_image_viewer_popup
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = LayoutImageViewerPopupBinding.bind(customView)
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.arrowBack)
        mBinding.tvPagerIndicator.setVisible(urls.size > 1)
        showPagerIndicator()
        clickListener(mBinding.arrowBack, mBinding.tvSave, clickListener = OnClickListener {
            when (it) {
                mBinding.arrowBack -> dismiss()
                mBinding.tvSave -> {
                    PermissionUtils.requestSDCardPermission(
                        context,
                        callback = object : PermissionUtils.PermissionCallback {
                            override fun allGranted() {
                                save()
                            }
                        }
                    )
                }
            }
        })
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(i: Int) {
                position = i
                showPagerIndicator()
                //更新srcView
                if (srcViewUpdateListener != null) {
                    srcViewUpdateListener.onSrcViewUpdate(this@ImageViewerPopup, i)
                }
            }

        })
    }


    private fun showPagerIndicator() {
        if (urls.size > 1) {
            val selectPos = if (isInfinite) position % urls.size else position
            mBinding.tvPagerIndicator.text = (selectPos + 1).toString() + "/" + urls.size
        }
        mBinding.tvSave.visibility = if(showSaveBtn) View.VISIBLE else View.INVISIBLE
    }


}