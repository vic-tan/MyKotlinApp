package com.tanlifei.app.persenal.fragment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentPersonalBinding
import com.tanlifei.app.persenal.activity.SettingActivity

/**
 * @desc:我的
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class PersonalFragment : BaseLazyFragment<FragmentPersonalBinding>() {


    companion object {
        fun newInstance(): PersonalFragment {
            val args = Bundle()
            val fragment = PersonalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onFirstVisibleToUser() {
        binding.setting.setOnClickListener {
            SettingActivity.actionStart()
        }
    }


}