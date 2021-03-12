package com.common.utils

import com.common.R
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle


/**
 * @desc:相册设置管理类
 * @author: tanlifei
 * @date: 2021/3/11 18:16
 */
object PictureSelectorManager {
    fun getPictureUIStyle(): PictureSelectorUIStyle? {
        val uiStyle = PictureSelectorUIStyle()
        uiStyle.picture_switchSelectTotalStyle = true
        uiStyle.picture_statusBarChangeTextColor = true
        uiStyle.picture_switchSelectNumberStyle = true
        uiStyle.picture_statusBarBackgroundColor = ResUtils.getColor(R.color.white)
        uiStyle.picture_container_backgroundColor = ResUtils.getColor(R.color.white)
        uiStyle.picture_navBarColor = ResUtils.getColor(R.color.white)
        uiStyle.picture_check_style = R.drawable.picture_checkbox_selector

        uiStyle.picture_top_leftBack = R.mipmap.ic_back_black
        uiStyle.picture_top_titleRightTextColor = intArrayOf(
            ResUtils.getColor(R.color.black),
            ResUtils.getColor(R.color.black)
        )
        uiStyle.picture_top_titleRightTextSize = 14
        uiStyle.picture_top_titleTextSize = 18
        uiStyle.picture_top_titleArrowUpDrawable = R.mipmap.ic_arrow_up
        uiStyle.picture_top_titleArrowDownDrawable = R.mipmap.ic_arrow_down
        uiStyle.picture_top_titleTextColor = ResUtils.getColor(R.color.black)
        uiStyle.picture_top_titleBarBackgroundColor = ResUtils.getColor(R.color.white)

        uiStyle.picture_bottom_previewTextSize = 14
        uiStyle.picture_bottom_previewTextColor = intArrayOf(
            ResUtils.getColor(R.color.theme_color),
            ResUtils.getColor(R.color.theme_color)
        )

        uiStyle.picture_bottom_completeRedDotTextSize = 12
        uiStyle.picture_bottom_completeTextSize = 14
        uiStyle.picture_bottom_completeRedDotTextColor = ResUtils.getColor(R.color.white)
        uiStyle.picture_bottom_completeRedDotBackground = R.drawable.picture_num_oval_pre
        uiStyle.picture_bottom_completeTextColor = intArrayOf(
            ResUtils.getColor(R.color.theme_color),
            ResUtils.getColor(R.color.theme_color)
        )
        uiStyle.picture_bottom_barBackgroundColor = ResUtils.getColor(R.color.white)


        uiStyle.picture_adapter_item_camera_backgroundColor =
            ResUtils.getColor(R.color.color_999999)
        uiStyle.picture_adapter_item_camera_textColor = ResUtils.getColor(R.color.white)
        uiStyle.picture_adapter_item_camera_textSize = 14
        uiStyle.picture_adapter_item_camera_textTopDrawable =
            com.luck.picture.lib.R.drawable.picture_icon_camera

        uiStyle.picture_adapter_item_textSize = 12
        uiStyle.picture_adapter_item_textColor = ResUtils.getColor(R.color.white)
        uiStyle.picture_adapter_item_video_textLeftDrawable =
            com.luck.picture.lib.R.drawable.picture_icon_video
        uiStyle.picture_adapter_item_audio_textLeftDrawable =
            com.luck.picture.lib.R.drawable.picture_icon_audio

        uiStyle.picture_bottom_originalPictureTextSize = 14
        uiStyle.picture_bottom_originalPictureCheckStyle = R.drawable.picture_original_checkbox
        uiStyle.picture_bottom_originalPictureTextColor = ResUtils.getColor(R.color.white)
        // 相册启动退出动画
        val windowAnimationStyle = PictureWindowAnimationStyle()
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out)
        return uiStyle
    }
}