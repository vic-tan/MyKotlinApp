package com.common.core.environment.adapter.holder

import android.view.View
import com.common.core.base.adapter.CommonRvMultiHolder
import com.common.databinding.ItemEnvironmentTitleBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/25 10:41
 */
class TitleViewHolder(holder: View) : CommonRvMultiHolder(holder) {
    private lateinit var binding: ItemEnvironmentTitleBinding

    constructor(binding: ItemEnvironmentTitleBinding) : this(binding.root) {
        this.binding = binding
    }

    fun binding(): ItemEnvironmentTitleBinding {
        return binding
    }
}