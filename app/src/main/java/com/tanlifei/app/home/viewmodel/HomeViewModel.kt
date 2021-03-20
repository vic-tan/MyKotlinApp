package com.tanlifei.app.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.home.bean.AdsnoviceBean
import com.tanlifei.app.home.bean.BannerBean
import com.tanlifei.app.home.bean.HomeHeaderDataBean
import com.tanlifei.app.home.bean.MenuBean

/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class HomeViewModel() : BaseViewModel() {
    var mData: MutableList<ClassmateCircleBean> = mutableListOf()
    var bannerData: MutableList<BannerBean> = mutableListOf()
    var menuData: MutableList<MenuBean> = mutableListOf()
    var adsnoviceData: MutableList<AdsnoviceBean> = mutableListOf()
    val homeHeaderDataChanged: LiveData<HomeHeaderDataBean> get() = _homeHeaderDataChanged
    private var _homeHeaderDataChanged = MutableLiveData<HomeHeaderDataBean>()

    fun requestRefresh() {
        launchByLoading {
            var homeHeaderData = ApiNetwork.requestHomeBanner()
            var listData = ApiNetwork.requestFriendsEntertainmentListByType(1, 1)
            bannerData.clear()
            menuData.clear()
            mData.clear()
            if (ObjectUtils.isNotEmpty(homeHeaderData))
                if (ObjectUtils.isNotEmpty(homeHeaderData.banner)) {
                    bannerData.addAll(homeHeaderData.banner)
                }
            if (ObjectUtils.isNotEmpty(homeHeaderData.menu)) {
                menuData.addAll(homeHeaderData.menu)
            }
            if (ObjectUtils.isNotEmpty(homeHeaderData.adsnovice)) {
                adsnoviceData.addAll(homeHeaderData.adsnovice)
            }
            if (ObjectUtils.isNotEmpty(listData)) {
                mData.addAll(listData)
            }
            _homeHeaderDataChanged.value = homeHeaderData
        }
    }
}