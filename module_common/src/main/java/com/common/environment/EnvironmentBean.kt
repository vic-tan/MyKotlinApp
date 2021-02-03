package com.common.environment

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.common.base.bean.BaseLitePalBean
import org.litepal.annotation.Column
import java.io.Serializable
import kotlin.random.Random

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 15:57
 */
data class EnvironmentBean(
    val alias: String,
    val url: String,
    @Column(ignore = true)
    var defaultCheck: Boolean = false
) :
    Serializable, MultiItemEntity, BaseLitePalBean() {
    override val modelId: Long
        get() = group

    @Column(ignore = true)
    var type: Int = 0
    var group: Long = 0

    init {
        type = 0
    }

    override val itemType: Int
        get() = type

    companion object {
        const val TITLE = 1
        const val CONTENT = 2
        const val GROUP_API = 100L//api 分组标识
        const val GROUP_SHARE = 200L//分享API标识
    }
}