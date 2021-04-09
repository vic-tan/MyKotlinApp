package com.onlineaginguniversity.common.event

import com.common.core.event.BaseEvent
import com.onlineaginguniversity.circle.bean.CircleBean

/**
 * @desc:同学圈点赞同步其它地方刷新
 * @author: tanlifei
 * @date: 2021/2/3 11:20
 */
data class PraiseEvent(val circleBean: CircleBean) : BaseEvent()