package com.tanlifei.app.common.config

/**
 * 项目所有全局通用常量的管理类。
 *
 * @author tanlifei
*/
interface Const {

    /**
     * SharedPreferences KEY 常量
     */
    interface SPKey{
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

    interface User {
        companion object {

            const val NICKNAME = "nk"

            const val AVATAR = "ar"

            const val BG_IMAGE = "bi"

            const val DESCRIPTION = "de"
        }

    }
}