package com.common.base.bean

import com.common.constant.EnumConst


/**
 * @desc:请求接口返回数据结果的LveData 参数
 * @author: tanlifei
 * @date: 2021/3/24 15:54
 */
data class ListDataChangePrams(val uiType: EnumConst.UiType, val size: Int = -1 ) {
}