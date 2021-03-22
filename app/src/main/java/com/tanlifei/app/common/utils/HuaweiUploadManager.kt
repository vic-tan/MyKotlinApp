package com.tanlifei.app.common.utils

import com.blankj.utilcode.util.ObjectUtils
import com.obs.services.ObsClient
import com.obs.services.model.PutObjectRequest
import com.obs.services.model.PutObjectResult
import kotlinx.coroutines.*
import java.io.File
import java.util.*

/**
 * @desc:华为云图片语音上传管理类
 * @author: tanlifei
 * @date: 2021/3/19 18:02
 */
class HuaweiUploadManager() {

    /**
     *
     */
    enum class EnterType {
        ENTER_TYPE_HOMEWORK,//加载中
        ENTER_TYPE_CLASSMATE,//完成
        ENTER_TYPE_GROUP,//报错界面
        ENTER_TYPE_USER_ID_CARD_VERIFIED,//报错界面
    }

    /**
     *
     */
    enum class UploadType {
        ERROR,//上传失败
        COUNT_ERROR,//数量不对
    }

    companion object {
        private const val HUAWEI_ACCESSKEY = "DMIDFEDMCGIVXH8EXRDZ"
        private const val HUAWEI_SECRETKEY = "KBOJ33cJVevY6KAR8J6XuoogxSDXKSY6MXWu9rGg"
        private const val HUAWEI_ENDPOINT = "https://obs.cn-north-1.myhuaweicloud.com"
    }


    //协程
    private val job: Job = Job()
    private val scope = CoroutineScope(job)

    fun statJob(
        enterType: EnterType,
        uploadList: MutableList<String>,
        callback: HuaweiResultListener
    ) {
        try {
            var resultList: MutableList<PutObjectResult> = mutableListOf()
            if (ObjectUtils.isNotEmpty(uploadList)) {
                scope.launch {
                    var asyncList: MutableList<Deferred<PutObjectResult>> = mutableListOf()
                    for (u in uploadList) {
                        asyncList.add(async {
                            upload(enterType, u)
                        })
                    }
                    if (ObjectUtils.isNotEmpty(asyncList)) {
                        for (u in asyncList) {
                            resultList.add(u.await())
                        }

                    }
                    cancle()//上传完成取消息协程
                    if (resultList.isNotEmpty()) {
                        if (resultList.size == uploadList.size) {
                            callback.onResult(resultList)
                        } else {
                            callback.onFail(UploadType.COUNT_ERROR)
                        }
                    } else {
                        callback.onFail(UploadType.COUNT_ERROR)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onFail(UploadType.ERROR)
        }
    }


    /**
     * 界面关闭时最好调用此方法取消协程，防止上传过程中关闭了当前界面，应该调用些方法中止上传
     */
    fun cancle() {
        if (ObjectUtils.isNotEmpty(job)) {
            job.cancel()
        }
    }

    suspend fun upload(enterType: EnterType, filePath: String): PutObjectResult {
        val file = File(filePath)
        val buffer = StringBuffer()
        buffer.append(fileStorePath(enterType))
        buffer.append(UserInfoUtils.getUid().toString() + "/")
        buffer.append(file.name)
        // 创建ObsClient实例
        val obsClient = ObsClient(
            HUAWEI_ACCESSKEY,
            HUAWEI_SECRETKEY,
            HUAWEI_ENDPOINT
        )
        val request = PutObjectRequest(bucketName(enterType), buffer.toString())
        request.file = file // filePath为上传的本地文件路径，需要指定到具体的文件名
        // 每上传1MB数据反馈上传进度
        request.progressInterval = 1024 * 1024L
        return obsClient.putObject(request)
    }

    private fun fileStorePath(enterType: EnterType): String {
        var fileStorePath = ""
        when (enterType) {
            EnterType.ENTER_TYPE_HOMEWORK,
            EnterType.ENTER_TYPE_CLASSMATE -> {
                fileStorePath = "homework/android/"
            }
            EnterType.ENTER_TYPE_GROUP -> {
                fileStorePath = "uploads/images/"
            }
            EnterType.ENTER_TYPE_USER_ID_CARD_VERIFIED -> {
                fileStorePath = "verify/android/" + getYear()
                    .toString() + "/" + getDay().toString() + "/"
            }
        }
        return fileStorePath
    }

    private fun bucketName(enterType: EnterType): String {
        var bucketName = ""
        when (enterType) {
            EnterType.ENTER_TYPE_HOMEWORK,
            EnterType.ENTER_TYPE_CLASSMATE -> {
                bucketName = "lndx-app"
            }
            EnterType.ENTER_TYPE_GROUP -> {
                bucketName = "jinling-xcx-dev"
            }
            EnterType.ENTER_TYPE_USER_ID_CARD_VERIFIED -> {
                bucketName = "lndx-app"
            }
        }
        return bucketName
    }

    /**
     * 获取年
     * @return
     */
    private fun getYear(): Int {
        val cd = Calendar.getInstance()
        return cd[Calendar.YEAR]
    }


    /**
     * 获取日
     * @return
     */
    private fun getDay(): Int {
        val cd = Calendar.getInstance()
        return cd[Calendar.DATE]
    }

    interface HuaweiResultListener {
        fun onResult(resultList: MutableList<PutObjectResult>)
        fun onFail(errorType: UploadType)
    }
}


