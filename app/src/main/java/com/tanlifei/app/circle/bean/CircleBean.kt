package com.tanlifei.app.circle.bean

import java.io.Serializable

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/8 10:32
 */
class CircleBean : Serializable {

    var uid: Long = 0
    var publishId: Long = 0
    var nickName: String? = null
    var avatar: String? = null
    var universityName: String? = null
    var mediaType = 0// 0是图文，1、是视频
    var videoImage: String? = null
    var imageUrl: String? = null
    var image: ImageBean? = null
    var imagesUrlList: List<ImageBean>? = null
    var videoUrl: String? = null
    var content: String? = null
    var checkStatus = 1 //审核状态:0=无效,1=有效 ,2 审核中
    var star: Long = 0
    var share: Int = 0
    var comment: Long = 0
    var isStar: Boolean = false
    var isFollowing: Int = 0
    var isFollower: Int = 0
    var createtimeStr: String? = null
    var collapseState = false //是否展开
    var maxLines: Int = 0
    var entertainmentTopicName: String? = null
    var entertainmentTagName: String? = null
    var entertainmentTopicId: Int = 0
    var goodsImage: String? = null
    var goodsTitle: String? = null
    var goodsPrice: String? = null
    var jumpCode //0,表示没有，1，表示来源于商品，2，表示来源于课程3.表示来源来旅游
            : String? = null
    var goodsParam: String? = null
    var goodsId: Int = 0
}