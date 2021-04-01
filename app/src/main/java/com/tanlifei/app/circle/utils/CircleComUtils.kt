package com.tanlifei.app.circle.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.common.constant.GlobalConst
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.viewmodel.CircleViewModel
import com.tanlifei.app.common.event.PraiseEvent
import org.greenrobot.eventbus.EventBus

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
                    mAdapter.notifyItemChanged(index, GlobalConst.Adapter.PAYLOAD)
                    return
                }
            }
        }
    }

    /**
     * 刷新列表点赞状态及数显
     */
    fun notifyPraiseObserve(
        circleViewModel: CircleViewModel,
        owner: LifecycleOwner,
        mData: MutableList<Any>,
        synchNotifyChanged: Boolean = false,
        mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null//synchNotifyChanged 时传
    ) {
        circleViewModel.mPraiseChanged.observe(owner, Observer { praiseResult ->
            praiseResult.position?.let { pos ->
                var cbean = mData[pos] as CircleBean
                cbean.apply {
                    when {
                        cbean.isStar -> {
                            if (cbean.star > 0)
                                cbean.star--
                        }
                        else -> {
                            cbean.star++
                        }
                    }
                    cbean.isStar = !cbean.isStar

                }
                if (synchNotifyChanged) {
                    mAdapter?.let {
                        it.notifyItemChanged(pos, GlobalConst.Adapter.PAYLOAD)
                    }
                }
                EventBus.getDefault().post(
                    PraiseEvent(cbean)
                )
            }
        })
    }
}