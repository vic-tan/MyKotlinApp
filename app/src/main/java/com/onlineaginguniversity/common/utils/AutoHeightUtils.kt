package com.onlineaginguniversity.common.utils

import com.blankj.utilcode.util.ObjectUtils
import com.onlineaginguniversity.circle.bean.ImageBean
import java.math.BigDecimal

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/23 12:16
 */
class AutoHeightUtils {
    companion object {
        //默认除法运算精度
        private const val DEF_DIV_SCALE = 10
        private const val DEF_IMG_WIDTH: Double = 360.0
        private const val DEF_IMG_HEIGHT: Double = 480.0

        /**
         * 根据图片大小比例显示不同ImageView的大小
         * 从相册选择照片后的，截取规则
         * 假设图片的宽:高比=w
         * 1、当所上传的图片，3/4<w></w><5/3，显示全部
         * 2、当所上传的图片，3/4>w，裁剪为3/4
         * 3、当所上传的图片，5/3<w></w>,裁剪为5/3
         */
        fun getHeightParams(screenWidth: Int, imageBean: ImageBean?): Int {
            if (ObjectUtils.isEmpty(imageBean)) {
                return getShowHeight(screenWidth.toDouble(), DEF_IMG_WIDTH, DEF_IMG_HEIGHT)
            }
            var imgW: Double = DEF_IMG_WIDTH
            var imgH: Double = DEF_IMG_HEIGHT
            if (ObjectUtils.isNotEmpty(imageBean!!.width) && ObjectUtils.isNotEmpty(imageBean.height)) {
                imgW = imageBean.width.toDouble()
                imgH = imageBean.height.toDouble()
            }
            if (imgW <= 0) {
                imgW = DEF_IMG_WIDTH
            }
            if (imgH <= 0) {
                imgH = DEF_IMG_HEIGHT
            }
            return getShowHeight(screenWidth.toDouble(), imgW, imgH)
        }


        /**
         * 根据图片大小比例显示不同ImageView的大小
         * 从相册选择照片后的，截取规则
         * 假设图片的宽:高比=w
         * 1、当所上传的图片，0.75<w></w><1.67，显示全部
         * 2、当所上传的图片，3/4<w></w>，裁剪为3/4
         * 3、当所上传的图片，5/3>w,裁剪为5/3
         */
        fun getShowHeight(
            screenWidth: Double,
            imgW: Double,
            imgH: Double
        ): Int {
            val scan: Double = div(imgW, imgH)
            val smallScan = 0.75 //3/4
            val bigScan = 1.67 //5/3;
            val showHeight: Double
            showHeight = when {
                scan < smallScan -> div(screenWidth, smallScan)
                scan > bigScan -> div(screenWidth, bigScan)
                else -> div(screenWidth, scan)
            }
            //MyLogTools.showLog("imgW =" + imgW + ",----imgH =" + imgH + ",----scan=" + scan + ",----screenWidth=" + screenWidth + ",----showHeight =" + showHeight);
            return showHeight.toInt()
        }

        /**
         * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
         * 定精度，以后的数字四舍五入。
         *
         * @param v1    被除数
         * @param v2    除数
         * @param scale 表示表示需要精确到小数点以后几位。
         * @return 两个参数的商
         */
        fun div(v1: Double, v2: Double): Double {
            require(DEF_DIV_SCALE >= 0) { "The scale must be a positive integer or zero" }
            val b1 = BigDecimal(v1.toString())
            val b2 = BigDecimal(v2.toString())
            return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).toDouble()
        }
    }
}