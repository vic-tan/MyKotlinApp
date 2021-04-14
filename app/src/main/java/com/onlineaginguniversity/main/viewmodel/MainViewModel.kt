package com.onlineaginguniversity.main.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.base.bean.UserBean
import com.common.base.viewmodel.BaseViewModel
import com.common.core.navigator.NavigatorAdapter
import com.common.core.navigator.NavigatorFragmentManager
import com.common.widget.component.extension.newInstanceFragment
import com.onlineaginguniversity.R
import com.onlineaginguniversity.circle.ui.fragment.CircleFragment
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.common.utils.UserInfoUtils
import com.onlineaginguniversity.home.ui.fragment.ClassFragment
import com.onlineaginguniversity.home.ui.fragment.HomeFragment
import com.onlineaginguniversity.home.ui.fragment.StudyFragment
import com.onlineaginguniversity.profile.ui.fragment.ProfileFragment
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
    var mHomeCurrentTabPosition = EnumConst.HomeTabTag.HOME.value
    val mCurrentTabPosition: LiveData<Int> get() = currentTabPosition
    private val currentTabPosition = MutableLiveData<Int>()

    /**
     * 同学圈关注/推荐tab监听，子类视频
     */
    var mCircleCurrentTabPosition = EnumConst.CircleTabTag.RECOMMEND.value//默认选择推荐
    val mShowFollowFragment: LiveData<Int> get() = showfollowFragment
    private val showfollowFragment = MutableLiveData<Int>()

    /**
     * 刷新用户信息
     */
    val mRefreshUserInfo: LiveData<UserBean> get() = refreshUserInfo
    private val refreshUserInfo = MutableLiveData<UserBean>()


    var mUserBean: UserBean? = null

    init {
        mHomeCurrentTabPosition = EnumConst.HomeTabTag.HOME.value
        currentTabPosition.value = EnumConst.HomeTabTag.HOME.value
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
        showFragment(EnumConst.HomeTabTag.HOME.value)
    }


    fun showFragment(position: Int) {
        currentTabPosition.value = position
        mHomeCurrentTabPosition = position
        mNavigator.showFragment(position) //显示点击Fargment
    }

    /**
     * 同学圈关注/推荐tab监听，子类视频
     */
    fun showRecommendPageFragment(position: Int) {
        mCircleCurrentTabPosition = position
        postLiveDataRecommendPageFragment(mCircleCurrentTabPosition)
    }

    fun postLiveDataRecommendPageFragment(type: Int) {
        showfollowFragment.value = type
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
        mFragments.add(newInstanceFragment<HomeFragment> {})
        mFragments.add(newInstanceFragment<ClassFragment> {})
        mFragments.add(newInstanceFragment<CircleFragment> {})
        mFragments.add(newInstanceFragment<StudyFragment> {})
        mFragments.add(newInstanceFragment<ProfileFragment> {})
    }
}