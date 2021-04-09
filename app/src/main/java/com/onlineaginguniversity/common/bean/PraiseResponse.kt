package com.onlineaginguniversity.common.bean


/**
 * @desc: 点赞/取消点赞接口结果实现
 * @author: tanlifei
 * @date: 2021/3/31 17:51
 * position 为列表中记录下标
 */
data class PraiseResponse(val obj: PraiseResult) {
    data class PraiseResult(var publishId: Long, var position: Int?) {}
}