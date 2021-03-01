package com.common.core.base.listener

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * @desc:Rv item 点击事件回调接口
 * @author: tanlifei
 * @date: 2021/2/24 16:55
 */
interface OnMultiItemListener {
    /**
     * Callback method to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param adapter  the adapter
     * @param view     The itemView within the RecyclerView that was clicked (this
     * will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    fun onItemClick(
        v: View,
        position: Int
    )
}