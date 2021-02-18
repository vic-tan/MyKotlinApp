package com.tanlifei.app.home.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComApplication
import com.common.core.base.navigator.NavigatorAdapter
import com.common.core.base.navigator.NavigatorFragmentManager
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.frgment.ClassmateCircleFragment
import com.tanlifei.app.common.bean.UserBean
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.home.ui.fragment.ClassFragment
import com.tanlifei.app.home.ui.fragment.HomeFragment
import com.tanlifei.app.home.ui.fragment.StudyFragment
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.main.utils.AdsUtils
import com.tanlifei.app.main.viewmodel.SplashViewModel
import com.tanlifei.app.persenal.fragment.PersonalFragment
import com.tencent.bugly.crashreport.biz.UserInfoBean
import org.litepal.LitePal
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/4 15:01
 */
class HomeViewModel() : BaseViewModel() {

    private val repository: ApiNetwork = ApiNetwork.getInstance()

    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mNavigator: NavigatorFragmentManager

    /**
     * 点击TAB时LveData
     */
    val currTabPosition: LiveData<Int> get() = _currTabPosition
    private val _currTabPosition = MutableLiveData<Int>()

    /**
     * 刷新用户信息
     */
    val refreshUserInfo: LiveData<UserBean> get() = _refreshUserInfo
    private val _refreshUserInfo = MutableLiveData<UserBean>()


    var userBean: UserBean? = null

    init {
        _currTabPosition.value = 0
    }

    /**
     * 初始化 Navigator
     */
    fun initNavigator(fragmentManager: FragmentManager) {
        mNavigator = NavigatorFragmentManager(
            fragmentManager,
            NavigatorAdapter(mFragments),
            R.id.tabContainer
        )
        showFragment(0)
    }


    fun showFragment(position: Int) {
        _currTabPosition.value = position
        mNavigator.showFragment(position) //显示点击Fargment
    }

    fun getUser() = launchBySilence({
        userBean = repository.requestUserInfo()
        if (ObjectUtils.isNotEmpty(userBean)) {
            userBean!!.token = ComApplication.token.toString()
            LitePal.deleteAll(UserBean::class.java)
            userBean!!.save()
        }
        _refreshUserInfo.value = userBean
    }, {
        findUserByDB()
    })

    /**
     * 查找数据库中是否保存广告
     */
    private fun findUserByDB() {
        if (ObjectUtils.isEmpty(userBean)) {
            userBean = UserInfoUtils.getUser()
            if (ObjectUtils.isNotEmpty(userBean)) {
                _refreshUserInfo.value = userBean
            }
        }
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    fun bindFragments() {
        mFragments.add(HomeFragment.newInstance())
        mFragments.add(ClassFragment.newInstance())
        mFragments.add(ClassmateCircleFragment.newInstance())
        mFragments.add(StudyFragment.newInstance())
        mFragments.add(PersonalFragment.newInstance())
    }
}