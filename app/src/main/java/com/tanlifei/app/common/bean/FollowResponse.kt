package com.tanlifei.app.common.bean


/**
 * @desc: 关注接口结果实现
 * @author: tanlifei
 * @date: 2021/3/31 17:51
 * position 为列表中记录下标
 */
data class FollowResponse(val isFollowing: Int, val isFollower: Int, var position: Int?) {
}