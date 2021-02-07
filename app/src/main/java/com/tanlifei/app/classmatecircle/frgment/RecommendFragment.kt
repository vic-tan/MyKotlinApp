package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentClassmatecircleBinding
import com.tanlifei.app.databinding.FragmentRecommendBinding

/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendFragment : BaseLazyFragment<FragmentRecommendBinding>() {

    companion object {
        fun newInstance(): RecommendFragment {
            val args = Bundle()
            val fragment =
                RecommendFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
    }


}