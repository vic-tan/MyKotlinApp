package com.onlineaginguniversity.common.constant

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

            //一键登录Key
            const val ALILIBABA_AUTH_SECRET =
                "lXpYnVfr5x4G/8U5i53xeugkcJt7wpVWEeHo8l73Gb/i58AbixSSqTsvxZyj5SkWVtpALIf++6AFdozTPjhKBre/6NKv0xihI+h8DdkkorNFZr7D6YDOPgTv7sew0+odNhWlqDmYoSQ+o+rIPuBQ1U16hLq/MRaxzW9ZloRGA8q43o8WkP/tcVq1JKUbLfzd26TgBCw3ZH43hFLxXBQuBHS1bTaI7/IMSHnIiIIDOBTgvdEukLzJfkD5XTWpG1PCO/TLJDhPAFE2m7J/qrPMsjFRTgUM1wBDU5qvbM/iwCo="

            const val USER_ID = "u_d"

            const val TOKEN = "t_k"

            const val LOGIN_TYPE = "l_t"
        }

    }


}