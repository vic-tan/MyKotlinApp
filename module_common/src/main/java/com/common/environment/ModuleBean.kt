package com.common.environment

import java.io.Serializable

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 15:57
 */
class ModuleBean(val alias: String, var groupId: Long, val list: List<EnvironmentBean>) :
    Serializable {
}