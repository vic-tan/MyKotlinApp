package com.tanlifei.app.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.repository.Repository
import com.tanlifei.app.home.bean.AdsnoviceBean
import com.tanlifei.app.home.bean.BannerBean
import com.tanlifei.app.home.bean.HomeHeaderDataBean

/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class HomeViewModel : BaseListViewModel() {
    var mBannerData: MutableList<BannerBean> = mutableListOf()
    var mMenuData: MutableList<Any> = mutableListOf()
    var mAdsnoviceData: MutableList<AdsnoviceBean> = mutableListOf()
    val mHomeHeaderDataChanged: LiveData<HomeHeaderDataBean> get() = homeHeaderDataChanged
    private var homeHeaderDataChanged = MutableLiveData<HomeHeaderDataBean>()

    fun requestRefresh() {

    }

    override fun requestList() {
        comRequest({
            var homeHeaderData = Repository.requestHomeBanner()
            var listData = Repository.requestFriendsEntertainmentListByType(1, 1)
            mBannerData.clear()
            mMenuData.clear()
            if (ObjectUtils.isNotEmpty(homeHeaderData))
                if (ObjectUtils.isNotEmpty(homeHeaderData.banner)) {
                    mBannerData.addAll(homeHeaderData.banner)
                }
            if (ObjectUtils.isNotEmpty(homeHeaderData.menu)) {
                mMenuData.addAll(homeHeaderData.menu)
            }
            if (ObjectUtils.isNotEmpty(homeHeaderData.adsnovice)) {
                mAdsnoviceData.addAll(homeHeaderData.adsnovice)
            }
            complete(listData)
            homeHeaderDataChanged.value = homeHeaderData
        })
    }
}