package com.common.cofing.constant

/**
 * 项目所有全局通用常量的管理类。
 *
 * @author tanlifei
 */
interface GlobalConst {

    /**
     * SharedPreferences KEY 常量
     */
    interface Http {
        companion object {
            const val NOT_LOAD_DATA = 12000//分页时没有数据了
        }
    }

}