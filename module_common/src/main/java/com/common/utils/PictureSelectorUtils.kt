package com.common.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ConvertUtils
import com.common.R
import com.common.widget.extension.color
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle


/**
 * @desc:相册设置管理类
 * @author: tanlifei
 * @date: 2021/3/11 18:16
 */
object PictureSelectorUtils {

    fun create(mContext: Context, chooseMode: Int, selectNum: Int = 1): PictureSelectionModel {
        return when (mContext) {
            is Fragment -> initSelectionModel(
                PictureSelector.create(mContext),
                chooseMode,
                selectNum
            )
            else -> initSelectionModel(
                PictureSelector.create(mContext as Activity),
                chooseMode,
                selectNum
            )
        }
    }


    /**
     *
     * 加载头像并裁剪为圆形
     */
    fun createAvatarCrop(mContext: Context): PictureSelectionModel {
        return create(mContext, PictureMimeType.ofImage())
            .isEnableCrop(true)
            .circleDimmedLayer(true)
            .selectionMode(PictureConfig.SINGLE)//单选
            .freeStyleCropEnabled(true)
            .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回
            .cropImageWideHigh(ConvertUtils.dp2px(150f), ConvertUtils.dp2px(150f))
            .setCropDimmedColor(color(R.color.com_shadow))//设置圆形裁剪背景色值
            .setCircleDimmedBorderColor(color(R.color.white))//设置圆形裁剪边框色值
            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
    }


    private fun initSelectionModel(
        selector: PictureSelector,
        chooseMode: Int,
        selectNum: Int
    ): PictureSelectionModel {
        return selector.openGallery(chooseMode).imageEngine(GlideEngine)
            .setPictureUIStyle(getPictureUIStyle())
            .isCompress(true)
            .maxSelectNum(selectNum)
            .isMaxSelectEnabledMask(true)//选择条件达到阀时列表是否启用蒙层效果
            .isAutomaticTitleRecyclerTop(true)//图片列表超过一屏连续点击顶部标题栏快速回滚至顶部
    }


    private fun getPictureUIStyle(): PictureSelectorUIStyle? {
        val uiStyle = PictureSelectorUIStyle()
        uiStyle.picture_switchSelectTotalStyle = true
        uiStyle.picture_statusBarChangeTextColor = true
        uiStyle.picture_switchSelectNumberStyle = true
        uiStyle.picture_statusBarBackgroundColor = color(R.color.white)
        uiStyle.picture_container_backgroundColor = color(R.color.white)
        uiStyle.picture_navBarColor = color(R.color.white)
        uiStyle.picture_check_style = R.drawable.picture_checkbox_selector

        uiStyle.picture_top_leftBack = R.mipmap.ic_arrow_left_black
        uiStyle.picture_top_titleRightTextColor = intArrayOf(
            color(R.color.black),
            color(R.color.black)
        )
        uiStyle.picture_top_titleRightTextSize = 14
        uiStyle.picture_top_titleTextSize = 18
        uiStyle.picture_top_titleArrowUpDrawable = R.mipmap.ic_arrow_up
        uiStyle.picture_top_titleArrowDownDrawable = R.mipmap.ic_arrow_down
        uiStyle.picture_top_titleTextColor = color(R.color.black)
        uiStyle.picture_top_titleBarBackgroundColor = color(R.color.white)

        uiStyle.picture_bottom_previewTextSize = 14
        uiStyle.picture_bottom_previewTextColor = intArrayOf(
            color(R.color.theme),
            color(R.color.theme)
        )

        uiStyle.picture_bottom_completeRedDotTextSize = 12
        uiStyle.picture_bottom_completeTextSize = 14
        uiStyle.picture_bottom_completeRedDotTextColor = color(R.color.white)
        uiStyle.picture_bottom_completeRedDotBackground = R.drawable.picture_num_oval_pre
        uiStyle.picture_bottom_completeTextColor = intArrayOf(
            color(R.color.theme),
            color(R.color.theme)
        )
        uiStyle.picture_bottom_barBackgroundColor = color(R.color.white)


        uiStyle.picture_adapter_item_camera_backgroundColor =
            color(R.color.txt_help)
        uiStyle.picture_adapter_item_camera_textColor = color(R.color.white)
        uiStyle.picture_adapter_item_camera_textSize = 14
        uiStyle.picture_adapter_item_camera_textTopDrawable =
            com.luck.picture.lib.R.drawable.picture_icon_camera

        uiStyle.picture_adapter_item_textSize = 12
        uiStyle.picture_adapter_item_textColor = color(R.color.white)
        uiStyle.picture_adapter_item_video_textLeftDrawable =
            com.luck.picture.lib.R.drawable.picture_icon_video
        uiStyle.picture_adapter_item_audio_textLeftDrawable =
            com.luck.picture.lib.R.drawable.picture_icon_audio

        uiStyle.picture_bottom_originalPictureTextSize = 14
        uiStyle.picture_bottom_originalPictureCheckStyle = R.drawable.picture_original_checkbox
        uiStyle.picture_bottom_originalPictureTextColor = color(R.color.white)
        // 相册启动退出动画
        val windowAnimationStyle = PictureWindowAnimationStyle()
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out)
        return uiStyle
    }
}