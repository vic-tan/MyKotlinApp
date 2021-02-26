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
            const val PAGE_NUM_KEY = "pageNum"//分码
            const val PAGE_SIZE_kEY = "pageSize"//分页每页显示数
            const val PAGE_SIZE_VALUE = 20//分页每页显示数
        }
    }

    /**
     * Intent KEY 传值的
     */
    interface Extras {
        companion object {
            const val ID = "id"//传ID的Key
            const val DATA = "data"//传数据的Key
            const val BEAN = "bean"//传实体的Key
            const val POSITION = "position"//传下标的Key
            const val LIST = "list"//传列表的Key
            const val JSON = "json"//传json的Key
            const val URL = "url"//传json的Key
            const val NAME = "name"//传json的Key
            const val TITLE = "title"//传json的Key
        }
    }


}