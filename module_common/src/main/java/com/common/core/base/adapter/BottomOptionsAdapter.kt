package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.R
import com.common.databinding.ItemOptionBinding
import com.common.utils.extension.dimen
import java.util.*

/**
 * @desc:底部选项适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class BottomOptionsAdapter(private val mHasTitleView: Boolean) :
    CommonRvAdapter<String>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(ItemOptionBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        title: String
    ) {
        holder as ItemOptionBinding
        holder.title.apply {
            text = title
            if (position == 0 && !mHasTitleView) {
                helper.cornerRadiusTopLeft =
                    dimen(R.dimen.dialog_corner_radius)
                helper.cornerRadiusTopRight =
                    dimen(R.dimen.dialog_corner_radius)
            } else {
                helper.cornerRadiusTopLeft = 0f
                helper.cornerRadiusTopRight = 0f
            }
        }

    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemOptionBinding
        return linkedSetOf(holder.title)
    }
}