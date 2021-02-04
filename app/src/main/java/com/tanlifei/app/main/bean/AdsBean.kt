package com.tanlifei.app.main.bean

import android.os.Parcel
import android.os.Parcelable
import com.common.base.bean.BaseLitePalBean
import java.io.Serializable

/**
 * @desc:广告实体
 * @author: tanlifei
 * @date: 2021/2/3 18:08
 */

class AdsBean(
    val name: String?,
    val type: Int,//跳转类型:0=站外H5,1=站外APP,2=精品课程,3=学习历史,4=本周课表,5=全部直播,6=联系客服
    val url: String?,
    val poster: String?,
    val tabletPoster: String?,
    val duration: Int,
    val status: Int//状态:0=隐藏,1=正常
) : BaseLitePalBean(), Parcelable {
    override val modelId: Long
        get() = 1

    constructor() : this(
        "",
        1,
        "",
        "",
        "",
        3,
        0
    ) {

    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(type)
        parcel.writeString(url)
        parcel.writeString(poster)
        parcel.writeString(tabletPoster)
        parcel.writeInt(duration)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdsBean> {
        override fun createFromParcel(parcel: Parcel): AdsBean {
            return AdsBean(parcel)
        }

        override fun newArray(size: Int): Array<AdsBean?> {
            return arrayOfNulls(size)
        }
    }
}