package com.tanlifei.app.classmatecircle.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @desc:回复实体
 * @author: tanlifei
 * @date: 2021/3/1 13:56
 */
data class ReplyBean(
    var id: Long,
    var commentId: Long,

    @SerializedName(value = "replyUserId", alternate = ["replyUid"])
    var replyUserId: Int = 0,

    @SerializedName(value = "fromUser", alternate = ["nickname", "replyUserName"])
    val replyUserName: String? = null,

    @SerializedName(value = "fromUserAvatar", alternate = ["avatar", "replyUserAvatar"])
    val replyUserAvatar: String? = null,

    @SerializedName(value = "replyContent", alternate = ["content"])
    private var replyContent: String? = null,

    @SerializedName(value = "toUser", alternate = ["toUserName"])
    var userName: String? = null,

    @SerializedName(value = "toUid")
    var userUid: Int = 0,

    @SerializedName(value = "toUserAvatar")
    val userAvatar: String? = null,

    @SerializedName(value = "createTimeStr", alternate = ["createtimeStr"])
    val createTimeStr: String? = null,
    var universityName: String? = null
) : Serializable {
}