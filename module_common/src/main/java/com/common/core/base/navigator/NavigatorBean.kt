package com.common.core.base.navigator

class NavigatorBean constructor() {


    var userId: Long = 0

    var nickname: String = ""

    var description: String = ""


    constructor(userId: Long, nickname: String, description: String) : this() {
        this.userId = userId
        this.nickname = nickname
        this.description = description
    }


}