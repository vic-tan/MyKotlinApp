package com.tanlifei.app.persenal.fragment

import android.os.Bundle
import android.view.View
import com.common.base.ui.fragment.BaseLazyFragment
import com.hjq.toast.ToastUtils
import com.tanlifei.app.databinding.FragmentPersonalBinding

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
        binding.msg.setOnClickListener(View.OnClickListener {
            ToastUtils.show("消息")
        })
    }


}