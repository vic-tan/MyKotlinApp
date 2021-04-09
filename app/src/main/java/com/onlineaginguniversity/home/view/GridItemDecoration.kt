package com.onlineaginguniversity.home.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.ConvertUtils

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/17 15:01
 */
class GridHomeRecommentItemDecoration(var offsets: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var position = parent.getChildAdapterPosition(view)
        val mOffsets = ConvertUtils.dp2px(offsets.toFloat())
        val mHalf = (mOffsets * 0.5).toInt()
        if (position % 2 == 0) {
            outRect.left = mOffsets
            outRect.right = mHalf
        } else {
            outRect.left = mHalf
            outRect.right = mOffsets
        }
        outRect.bottom = mOffsets
    }
}