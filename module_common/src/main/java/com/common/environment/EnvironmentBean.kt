package com.common.environment

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.common.base.bean.BaseLitePalBean
import org.litepal.annotation.Column
import java.io.Serializable
import kotlin.random.Random

/**
 * @desc:组成员实体
 * @author: tanlifei
 * @date: 2021/2/2 15:57
 */
data class EnvironmentBean(
    val alias: String,
    val url: String,
    val activityAlias: String = "",//切换app 图标跟EnvironmentChangeManager类中的map保持一致
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
        const val DB_GROUP = "group"//数据库中的分组字段
        const val TITLE = 1
        const val CONTENT = 2
        const val GROUP_API = 100L//api 分组标识,这个尽量不要改变
        const val GROUP_SHARE = 200L//分享API标识
    }
}