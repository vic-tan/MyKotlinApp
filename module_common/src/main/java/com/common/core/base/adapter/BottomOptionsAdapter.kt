package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.R
import com.common.databinding.ItemOptionBinding
import com.common.utils.extension.dimen
import com.common.utils.extension.dp2px
import java.util.*

/**
 * @desc:底部选项适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class BottomOptionsAdapter(val hasTitleView: Boolean) :
    CommonRvAdapter<String, ItemOptionBinding>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemOptionBinding> {
        return CommonRvHolder(ItemOptionBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: CommonRvHolder<ItemOptionBinding>,
        position: Int,
        binding: ItemOptionBinding,
        title: String
    ) {
        holder.binding.title.text = title
        if (position == 0 && !hasTitleView) {
            holder.binding.title.helper.cornerRadiusTopLeft =
                dimen(R.dimen.dialog_corner_radius)
            holder.binding.title.helper.cornerRadiusTopRight =
                dimen(R.dimen.dialog_corner_radius)
        } else {
            holder.binding.title.helper.cornerRadiusTopLeft = 0f
            holder.binding.title.helper.cornerRadiusTopRight = 0f
        }
    }

    override fun addChildClickViewIds(binding: ItemOptionBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.title)
    }
}