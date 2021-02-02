package com.common.environment

import java.io.Serializable

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 15:57
 */
data class EnvironmentBean(val alias: String, val url: String, val check: Boolean) :
    Serializable {

}