package com.tanlifei.app.classmatecircle.bean

import java.io.Serializable

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/8 10:32
 */
class ClassmateCircleBean : Serializable {

    var uid: Long = 0
    var publishId = 0
    var nickName: String? = null
    var avatar: String? = null
    var universityName: String? = null
    var mediaType = 0
    var videoImage: String? = null
    var imageUrl: String? = null
    var image: ImageBean? = null
    var imagesUrlList: List<ImageBean>? = null
    var videoUrl: String? = null
    var content: String? = null
    var checkStatus = 1 //审核状态:0=无效,1=有效 ,2 审核中
    var star = 0
    var share = 0
    var comment = 0
    var isStar = false
    var isFollowing = 0
    var isFollower = 0
    var createtimeStr: String? = null
    var collapseState = false //是否展开
    var maxLines = 0
    var entertainmentTopicName: String? = null
    var entertainmentTagName: String? = null
    var entertainmentTopicId = 0
    var goodsImage: String? = null
    var goodsTitle: String? = null
    var goodsPrice: String? = null
    var jumpCode //0,表示没有，1，表示来源于商品，2，表示来源于课程3.表示来源来旅游
            : String? = null
    var goodsParam: String? = null
    var goodsId = 0
    override fun toString(): String {
        return "ClassmateCircleBean(uid=$uid, publishId=$publishId, nickName=$nickName, avatar=$avatar, universityName=$universityName, mediaType=$mediaType, videoImage=$videoImage, imageUrl=$imageUrl, image=$image, imagesUrlList=$imagesUrlList, videoUrl=$videoUrl, content=$content, checkStatus=$checkStatus, star=$star, share=$share, comment=$comment, isStar=$isStar, isFollowing=$isFollowing, isFollower=$isFollower, createtimeStr=$createtimeStr, collapseState=$collapseState, maxLines=$maxLines, entertainmentTopicName=$entertainmentTopicName, entertainmentTagName=$entertainmentTagName, entertainmentTopicId=$entertainmentTopicId, goodsImage=$goodsImage, goodsTitle=$goodsTitle, goodsPrice=$goodsPrice, jumpCode=$jumpCode, goodsParam=$goodsParam, goodsId=$goodsId)"
    }
}