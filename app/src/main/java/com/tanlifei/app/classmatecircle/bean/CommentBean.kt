package com.tanlifei.app.classmatecircle.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @desc:评论实体
 * @author: tanlifei
 * @date: 2021/3/1 13:56
 */
data class CommentBean(
    @SerializedName(value = "id", alternate = ["commentId"])
    var id: Long,
    @SerializedName(value = "uid", alternate = ["commentUid"])
    var uid: Long,
    @SerializedName(value = "universityName", alternate = ["university"])
    var universityName: String? = null,
    @SerializedName(value = "lessonId", alternate = ["sublessonId", "subjectId"])
    var lessonId: Long = 0,
    var status: Int = 0,
    @SerializedName(value = "createtimeStr", alternate = ["createTimeStr"])
    var createTimeStr: String? = null,
    var content: String? = null,
    var avatar: String? = null,
    @SerializedName(value = "nickname", alternate = ["name"])
    var nickname: String? = null,
    var replyNum: Int = 0,
    var replyPageNum: Int = 0,
    var replyRecyclerState: Int = 0,
    var replyList: MutableList<ReplyBean?>? = null,
    var role: Int = 0,
    var duration: Long = 0
) : Serializable {
}