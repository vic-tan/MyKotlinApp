package com.tanlifei.app.circle.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.ConvertUtils

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/23 12:38
 */
class GridItemDecoration(interval: Int = 0) : ItemDecoration() {
    private var mInterval = interval

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view)
        val interval = ConvertUtils.dp2px(mInterval.toFloat())
        outRect.left = interval / 2
        outRect.right = interval / 2
        outRect.bottom = interval
    }
}