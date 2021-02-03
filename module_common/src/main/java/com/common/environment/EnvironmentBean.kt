package com.common.environment

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 15:57
 */
data class EnvironmentBean(val alias: String, val url: String, var check: Boolean) :
    Serializable, MultiItemEntity {
    var type: Int = 0
    var group: String = ""

    init {
        type = 0
    }

    override val itemType: Int
        get() = type

    companion object {
        val TITLE = 1
        val CONTENT = 2
    }
}