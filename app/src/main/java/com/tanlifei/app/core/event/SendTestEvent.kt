package com.tanlifei.app.core.event

class SendTestEvent : MessageEvent() {
    var name: String? = ""

    var isLogin = false

    var age: Int = 0

    var commentId: Long = 0

    var type = -1//与下面常对应

    companion object {

        const val LOGIN_SUCCESS = 0

        const val AUTH_DENIED = 1

        const val USER_CANCEL = 2

        const val UNKNOWN_ERROR = 3

    }
}