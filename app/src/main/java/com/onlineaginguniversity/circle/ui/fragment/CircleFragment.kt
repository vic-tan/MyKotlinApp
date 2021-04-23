package com.onlineaginguniversity.circle.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BasePagerAdapter
import com.common.base.ui.fragment.BaseLazyFragment
import com.common.base.viewmodel.EmptyViewModel
import com.common.utils.PermissionUtils
import com.common.utils.PictureSelectorUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.newInstanceFragment
import com.common.widget.component.magicindicator.MagicIndicatorUtils
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.onlineaginguniversity.circle.ui.activity.CircleReleaseActivity
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.FragmentClassmatecircleBinding
import com.onlineaginguniversity.main.ui.activity.MainActivity
import com.onlineaginguniversity.main.viewmodel.MainViewModel


/**
 * @desc:同学圈
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class CircleFragment : BaseLazyFragment<FragmentClassmatecircleBinding, EmptyViewModel>() {
    private val mTitleData =
        mutableListOf(EnumConst.CircleTabTag.CIRCLE.title, EnumConst.CircleTabTag.RECOMMEND.title)
    private lateinit var mFragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mHomeViewModel: MainViewModel


    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun onFirstVisibleToUser() {
        mHomeViewModel = (activity as MainActivity).mViewModel
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.tabIndicator)//为 view 增加 MarginTop 为状态栏高度
        bindFragments()
        MagicIndicatorUtils.initComMagicIndicator(
            activity,
            mBinding.tabIndicator,
            mBinding.viewPager,
            mTitleData
        )
        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mHomeViewModel.showRecommendPageFragment(position)
            }

        })
        clickListener(mBinding.circleRelease, clickListener = View.OnClickListener {
            when (it) {
                mBinding.circleRelease -> {
                    activity?.let { activity ->
                        PermissionUtils.requestCameraPermission(
                            activity,
                            callback = object : PermissionUtils.PermissionCallback {
                                override fun allGranted() {
                                    PictureSelectorUtils.createCircle(activity)
                                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                            override fun onResult(result: List<LocalMedia?>) {
                                                if (ObjectUtils.isNotEmpty(result)) {
                                                    CircleReleaseActivity.actionStart(
                                                        result,
                                                        (result.size == 1 && PictureMimeType.getMimeType(
                                                            result[0]?.mimeType
                                                        ) == PictureConfig.TYPE_VIDEO)
                                                    )
                                                }

                                            }

                                            override fun onCancel() {
                                            }
                                        })
                                }
                            })
                    }

                }
            }

        })
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    private fun bindFragments() {
        mFragments.add(newInstanceFragment<FollowFragment> {})
        mFragments.add(newInstanceFragment<RecommendTabFragment> {})
        mFragmentAdapter = BasePagerAdapter(childFragmentManager, mFragments)
        mBinding.viewPager.adapter = mFragmentAdapter
        mBinding.viewPager.currentItem = EnumConst.CircleTabTag.RECOMMEND.value
    }
}


