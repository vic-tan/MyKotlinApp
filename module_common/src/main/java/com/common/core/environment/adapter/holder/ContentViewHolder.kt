package com.common.core.environment.adapter.holder

import android.view.View
import com.common.core.base.adapter.CommonRvMultiHolder
import com.common.databinding.ItemEnvironmentContentBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/25 10:41
 */
class ContentViewHolder(holder: View) : CommonRvMultiHolder(holder) {
    private lateinit var binding: ItemEnvironmentContentBinding

    constructor(binding: ItemEnvironmentContentBinding) : this(binding.root) {
        this.binding = binding
    }

    fun binding(): ItemEnvironmentContentBinding {
        return binding
    }
}