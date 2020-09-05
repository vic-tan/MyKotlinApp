package com.tanlifei.mykotlinapp.core.model

import com.chad.library.adapter.base.entity.MultiItemEntity

class User constructor() : Model(), MultiItemEntity {

    override val modelId: Long
        get() = userId

    var userId: Long = 0

    var nickname: String = ""

    var description: String = ""

    var type: Int = TEXT


    constructor(type: Int, userId: Long, nickname: String, description: String) : this() {
        this.type = type
        this.userId = userId
        this.nickname = nickname
        this.description = description
    }

    constructor(userId: Long, nickname: String, description: String) : this() {
        this.userId = userId
        this.nickname = nickname
        this.description = description
    }

    override val itemType: Int
        get() = type

    companion object {
        val TEXT = 1
        val IMG = 2
        val IMG_TEXT = 3
        val TEXT_SPAN_SIZE = 3
        val IMG_SPAN_SIZE = 1
        val IMG_TEXT_SPAN_SIZE = 4
        val IMG_TEXT_SPAN_SIZE_MIN = 2
    }
}