package com.tanlifei.app.circle.utils

import androidx.recyclerview.widget.RecyclerView
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.common.event.PraiseEvent

/**
 * @desc:同学圈共用工具类
 * @author: tanlifei
 * @date: 2021/4/1 9:54
 */
object CircleComUtils {

    /**
     * 同步点赞状态
     */
    fun syncPraise(
        event: PraiseEvent,
        mData: MutableList<Any>,
        mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        for ((index, bean) in mData.withIndex()) {
            bean as CircleBean
            when (bean.publishId) {
                event.circleBean.publishId -> {
                    bean.isStar = event.circleBean.isStar
                    bean.star = event.circleBean.star
                    mAdapter.notifyItemChanged(index)
                    return
                }
            }
        }
    }
}