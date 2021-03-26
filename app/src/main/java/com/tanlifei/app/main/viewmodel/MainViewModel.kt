package com.tanlifei.app.main.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.core.base.bean.UserBean
import com.common.core.base.navigator.NavigatorAdapter
import com.common.core.base.navigator.NavigatorFragmentManager
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.ui.fragment.ClassmateCircleFragment
import com.tanlifei.app.common.network.Repository
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.home.ui.fragment.ClassFragment
import com.tanlifei.app.home.ui.fragment.HomeFragment
import com.tanlifei.app.home.ui.fragment.StudyFragment
import com.tanlifei.app.profile.ui.fragment.ProfileFragment
import org.litepal.LitePal
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/4 15:01
 */
class MainViewModel : BaseViewModel() {

    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mNavigator: NavigatorFragmentManager

    /**
     * 点击TAB时LveData
     */
    val mCurrTabPosition: LiveData<Int> get() = currTabPosition
    private val currTabPosition = MutableLiveData<Int>()

    /**
     * 刷新用户信息
     */
    val mRefreshUserInfo: LiveData<UserBean> get() = refreshUserInfo
    private val refreshUserInfo = MutableLiveData<UserBean>()


    var mUserBean: UserBean? = null

    init {
        currTabPosition.value = 0
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
        currTabPosition.value = position
        mNavigator.showFragment(position) //显示点击Fargment
    }


    /**
     * 请求用户信息
     */
    fun requestUser() = comRequest({
        mUserBean = Repository.requestUserInfo()
        if (ObjectUtils.isNotEmpty(mUserBean)) {
            mUserBean!!.token = ComFun.mToken.toString()
            LitePal.deleteAll(UserBean::class.java)
            mUserBean!!.save()
            refreshUserInfo.value = mUserBean
        } else {
            findUserByDB()
        }
    }, onError = {
        findUserByDB()
    })

    /**
     * 获取用户信息
     */
    fun getUser() {
        if (ObjectUtils.isNotEmpty(mUserBean)) {
            refreshUserInfo.value = mUserBean
        } else {
            requestUser()
        }
    }

    /**
     * 查找数据库中是否保存广告
     */
    private fun findUserByDB() {
        if (ObjectUtils.isEmpty(mUserBean)) {
            mUserBean = UserInfoUtils.getUser()
            if (ObjectUtils.isNotEmpty(mUserBean)) {
                refreshUserInfo.value = mUserBean
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
        mFragments.add(ProfileFragment.newInstance())
    }
}