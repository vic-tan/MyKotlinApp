package com.tanlifei.app.common.constant

/**
 * app 项目通用常量的管理类。
 *
 * @author tanlifei
 */
interface ComConst {

    /**
     * SharedPreferences KEY 常量
     */
    interface SPKey {
        companion object {
            const val GUIDE = "sp_guide"
        }
    }

    interface Auth {
        companion object {

            const val USER_ID = "u_d"

            const val TOKEN = "t_k"

            const val LOGIN_TYPE = "l_t"
        }

    }


}