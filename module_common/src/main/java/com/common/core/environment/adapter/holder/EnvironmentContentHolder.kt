package com.common.core.environment.adapter.holder

import com.common.core.base.adapter.CommonRvMultiHolder
import com.common.databinding.ItemEnvironmentContentBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/25 10:41
 */
class EnvironmentContentHolder(binding: ItemEnvironmentContentBinding) :
    CommonRvMultiHolder(binding.root) {
    var binding: ItemEnvironmentContentBinding = binding
}