package com.tanlifei.app.common.bean

/**
 * @desc:app版本升级
 * @author: tanlifei
 * @date: 2021/2/19 15:22
 */
data class UpdateAppBean(
    val id: Long,
    val appId: String,
    val appName: String,
    val systemType: Int,//系统类型（0、IOS,1、Andorid）
    val isTes: Boolean,//测试类型（0不是,1是）
    val clientVersion: String,// 客户端版本号
    val clientUseragent: String,//渠道各渠道版本，以逗号分隔，可升级多渠道，全渠道用all，
    val clientUseragentName: String,// 渠道名称
    val downloadUrl: String,// 升级下载网址
    val updateVersion: String,// 升级版本
    val updateLog: String,// 升级日志
    val updateInstall: Boolean,// 是否强制安装
    val createtime: Long,// 创建时间
    val status: Int,// 状态
    val updatetimeval: Long//更新时间
)