package com.tanlifei.app.core.bean

import com.tanlifei.app.common.bean.BaseLitePalBean

class Banner : BaseLitePalBean(){
//    {
//        "desc":"享学~",
//        "id":29,
//        "imagePath":"https://www.wanandroid.com/blogimgs/8e95ad05-a6f5-4c65-8a89-f8d4b819aa80.jpeg",
//        "isVisible":1,
//        "order":0,
//        "title":"做了5年Android，靠这份面试题和答案从12K涨到30K",
//        "type":0,
//        "url":"https://mp.weixin.qq.com/s/oxoocfuPBS-fYI1Y0HU5QQ"
//    }

    override val modelId: Long
        get() = id

    var id: Long = 0

    var desc: String = ""
    var imagePath: String = ""
    var isVisible: Int = 0
    var order: Int = 0
    var title: String = ""
    var type: Int = 0
    var url: String = ""


}