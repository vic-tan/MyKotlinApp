package com.common.core.base.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.common.databinding.ItemEnvironmentTitleBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/25 10:41
 */
class TitleViewHolder(holder: View) : RecyclerView.ViewHolder(holder) {
    private lateinit var binding: ItemEnvironmentTitleBinding

    constructor(binding: ItemEnvironmentTitleBinding) : this(binding.root) {
        this.binding = binding
    }

    fun binding(): ItemEnvironmentTitleBinding {
        return binding
    }
}