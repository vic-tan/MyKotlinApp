package com.common.widget.popup

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.common.R
import com.common.databinding.LayoutImageViewerPopupBinding
import com.common.utils.PermissionUtils
import com.common.utils.extension.clickListener
import com.common.utils.extension.setVisible
import com.lxj.xpopup.core.ImageViewerPopupView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/18 17:27
 */
class ImageViewerPopup(context: Context) : ImageViewerPopupView(context) {
    lateinit var binding: LayoutImageViewerPopupBinding
    override fun getImplLayoutId(): Int {
        return R.layout.layout_image_viewer_popup
    }

    override fun onCreate() {
        super.onCreate()
        binding = LayoutImageViewerPopupBinding.bind(customView)
        BarUtils.addMarginTopEqualStatusBarHeight(binding.arrowBack)
        binding.tvPagerIndicator.setVisible(urls.size > 1)
        showPagerIndicator()
        clickListener(binding.arrowBack, binding.tvSave, clickListener = OnClickListener {
            when (it) {
                binding.arrowBack -> dismiss()
                binding.tvSave -> {
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
            binding.tvPagerIndicator.text = (selectPos + 1).toString() + "/" + urls.size
        }
        if (isShowSaveBtn) tv_save.visibility = View.VISIBLE
    }


}