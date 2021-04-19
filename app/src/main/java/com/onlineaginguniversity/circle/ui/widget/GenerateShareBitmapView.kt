package com.onlineaginguniversity.circle.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import cn.sharesdk.framework.utils.QRCodeUtil.WriterException
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.common.constant.GlobalConst
import com.common.utils.GlideUtils
import com.common.utils.PermissionUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.dp2px
import com.common.widget.component.extension.setVisible
import com.common.widget.component.extension.toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.luck.picture.lib.tools.BitmapUtils
import com.lxj.xpopup.impl.FullScreenPopupView
import com.onlineaginguniversity.R
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.circle.utils.AlbumUtils
import com.onlineaginguniversity.common.utils.AutoHeightUtils
import com.onlineaginguniversity.common.widget.component.share.listener.ShareListener
import com.onlineaginguniversity.common.widget.component.share.utils.ShareSdkUtils
import com.onlineaginguniversity.databinding.LayoutGenerateImageBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/**
 * @desc:生成分享图界面
 * @author: tanlifei
 * @date: 2021/4/15 16:12
 */
class GenerateShareBitmapView(context: Context, var bean: CircleBean) :
    FullScreenPopupView(context) {

    lateinit var mBinding: LayoutGenerateImageBinding
    private var bitmap: Bitmap? = null
    private var job: Job? = null

    override fun getImplLayoutId(): Int {
        return R.layout.layout_generate_image
    }

    override fun onCreate() {
        super.onCreate()
        var coverWidth = ScreenUtils.getScreenWidth() - dp2px(100f)
        mBinding = LayoutGenerateImageBinding.bind(popupImplView)
        mBinding.cover.layoutParams.width = coverWidth
        mBinding.cover.layoutParams.height =
            AutoHeightUtils.getHeightParams(coverWidth, bean.image)
        GlideUtils.load(context, bean.avatar, mBinding.avatar)
        GlideUtils.load(context, bean.image?.url, mBinding.cover)
        Glide.with(context)
            .asBitmap()
            .load(bean.image?.url)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(24, 5)))
            .apply(RequestOptions.placeholderOf(R.mipmap.bg_profile_default))
            .dontAnimate()
            .into(object : BitmapImageViewTarget(mBinding.blurBg) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    mBinding.blurBg.setImageBitmap(bitmap)
                }
            })
        mBinding.name.text = bean.nickName
        mBinding.school.text = bean.universityName
        mBinding.content.text = bean.content
        mBinding.content.setVisible(ObjectUtils.isNotEmpty(bean.content))
        mBinding.school.setVisible(ObjectUtils.isNotEmpty(bean.universityName))
        mBinding.code.setImageBitmap(createQRCodeBitmap(bean.qrURL))
        clickListener(
            mBinding.wx,
            mBinding.wxCircle,
            mBinding.download,
            mBinding.cancel,
            clickListener = OnClickListener { v ->
                PermissionUtils.requestSDCardPermission(
                    context,
                    callback = object : PermissionUtils.PermissionCallback {
                        override fun allGranted() {
                            when (v) {
                                mBinding.wx -> {
                                    if (isWeixinAvilible()) {
                                        val downloadBitmap = generateBitmap()
                                        if (ObjectUtils.isNotEmpty(downloadBitmap)) {
                                            FileUtils.createOrExistsDir("${GlobalConst.FilesUrl.PICTURES}")
                                            val file = File(
                                                "${GlobalConst.FilesUrl.PICTURES}/${System.currentTimeMillis()}.jpg"
                                            )
                                            BitmapUtils.saveBitmapFile(downloadBitmap, file)
                                            if (FileUtils.isFileExists(file)) {
                                                ShareSdkUtils.wechatImage(file.absolutePath,
                                                    object :
                                                        ShareListener {

                                                    })
                                            }
                                        } else {
                                            toast("分享失败")
                                        }
                                    }
                                }
                                mBinding.wxCircle -> {
                                    if (isWeixinAvilible()) {
                                        val downloadBitmap = generateBitmap()
                                        if (ObjectUtils.isNotEmpty(downloadBitmap)) {
                                            FileUtils.createOrExistsDir("${GlobalConst.FilesUrl.PICTURES}")
                                            val file = File(
                                                    "${GlobalConst.FilesUrl.PICTURES}/${System.currentTimeMillis()}.jpg"
                                                )
                                            BitmapUtils.saveBitmapFile(downloadBitmap, file)
                                            if (FileUtils.isFileExists(file)) {
                                                ShareSdkUtils.wechatMomentsImage(file.absolutePath,
                                                    object :
                                                        ShareListener {

                                                    })
                                            }
                                        } else {
                                            toast("分享失败")
                                        }
                                    }
                                }

                                mBinding.download -> {
                                    saveGenerateBitmap()
                                }

                                mBinding.cancel -> {
                                    dismiss()
                                }

                            }
                        }
                    }
                )


            }
        )
    }


    /**
     * 合成图片
     */
    private fun generateBitmap(): Bitmap? {
        val contentLayoutBitmap = createBitmap(mBinding.contentLayout)
        val codeLayoutBitmap = createBitmap(mBinding.codeLayout)
        return if (ObjectUtils.isNotEmpty(contentLayoutBitmap) && ObjectUtils.isNotEmpty(
                codeLayoutBitmap
            )
        ) {
            mergeBitmap(contentLayoutBitmap!!, codeLayoutBitmap!!)
        } else {
            null
        }
    }

    /**
     * 保存合成的新图片
     */
    private fun saveGenerateBitmap() {
        val downloadBitmap = generateBitmap()
        if (ObjectUtils.isNotEmpty(downloadBitmap)) {
            saveBmpToAlbum(downloadBitmap!!)
        } else {
            toast("保存失败")
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        job?.let {
            it.cancel()
        }
    }

    /**
     * 生成图片
     * @param v
     * @return
     */
    private fun createBitmap(v: View): Bitmap? {
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_4444)
        val c = Canvas(bmp)
        c.drawColor(Color.TRANSPARENT)
        v.draw(c)
        return bmp
    }

    /**
     * 拼接图片
     *
     * @param contentBitmap
     * @param codeBitmap
     * @return 返回拼接后的Bitmap
     */
    private fun mergeBitmap(contentBitmap: Bitmap, codeBitmap: Bitmap): Bitmap? {
        val leftMargin = dp2px(35f)
        val topMargin = dp2px(30f)
        val horizontalMargin = leftMargin * 2
        val verticalMargin = topMargin * 2
        val spacing = dp2px(65f)
        val newWidth = contentBitmap.width + horizontalMargin
        val newHeight = contentBitmap.height + codeBitmap.height + verticalMargin + spacing
        var saveBitmap: Bitmap? = when {
            ObjectUtils.isNotEmpty(bitmap) -> {
                newBitmap(bitmap!!, newWidth, newHeight)
            }
            else -> {
                Bitmap.createBitmap(newHeight, newHeight, Bitmap.Config.ARGB_8888)
            }
        }
        saveBitmap?.let {
            val canvas = Canvas(it)
            if (ObjectUtils.isEmpty(bitmap)) {
                canvas.drawColor(Color.parseColor("#6F6F6F"))
            }
            canvas.drawBitmap(
                contentBitmap,
                leftMargin.toFloat(),
                topMargin.toFloat(),
                null
            )
            canvas.drawBitmap(codeBitmap, 0f, contentBitmap.height + spacing.toFloat(), null)
        }
        return saveBitmap
    }

    /**
     * 创建新的图片
     */
    private fun newBitmap(bitmap: Bitmap, contentWidth: Int, contentHeight: Int): Bitmap? {
        val matrixWidth = contentWidth.toFloat() / bitmap.width
        val matrixHeight = contentHeight.toFloat() / bitmap.height
        val matrix = Matrix()
        matrix.postScale(matrixWidth, matrixHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 保存图片到相册
     */
    private fun saveBmpToAlbum(bmp: Bitmap) {
        job = Job()
        job?.let {
            val scope = CoroutineScope(it)
            scope.launch {
                var save = AlbumUtils.saveBitmap2Gallery(context, bmp)
                toast(
                    if (save) {
                        "保存成功"
                    } else {
                        "保存失败"
                    }
                )
            }
        }
    }


    /**
     * 生成简单二维码
     *
     * @param content 字符串内容
     * @return BitMap
     */
    private fun createQRCodeBitmap(content: String?): Bitmap? {
        return createQRCodeBitmap(
            content, ConvertUtils.dp2px(70f), ConvertUtils.dp2px(70f),
            "UTF-8", "H", "1", Color.BLACK, Color.WHITE
        )
    }

    /**
     * 生成简单二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @return BitMap
     */
    private fun createQRCodeBitmap(
        content: String?, width: Int, height: Int,
        character_set: String, error_correction_level: String,
        margin: String, color_black: Int, color_white: Int
    ): Bitmap? {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null
        }
        // 宽和高>=0
        return if (width < 0 || height < 0) {
            null
        } else try {
            /** 1.设置二维码相关配置  */
            val hints: Hashtable<EncodeHintType, String> =
                Hashtable<EncodeHintType, String>()
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints[EncodeHintType.CHARACTER_SET] = character_set
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
            val bitMatrix: BitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black //黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 检测是否安装微信
     * @return
     */
    private fun isWeixinAvilible(): Boolean {
        val packageManager = context.packageManager // 获取packagemanager
        val pinfo =
            packageManager.getInstalledPackages(0) // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (pn == "com.tencent.mm") {
                    return true
                }
            }
        }
        toast("请求先安装微信客户端再分享")
        return false
    }


}