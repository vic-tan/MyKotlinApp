package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentFollowBinding

/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseLazyFragment<FragmentFollowBinding>() {

    companion object {
        fun newInstance(): FollowFragment {
            val args = Bundle()
            val fragment =
                FollowFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
    }


}