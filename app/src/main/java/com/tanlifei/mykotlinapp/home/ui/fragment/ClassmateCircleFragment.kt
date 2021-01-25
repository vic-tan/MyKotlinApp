package com.tanlifei.mykotlinapp.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tanlifei.mykotlinapp.R
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassmateCircleFragment : Fragment() {

    lateinit var mView: View

    companion object {
        fun newInstance(): ClassmateCircleFragment {
            val args = Bundle()
            val fragment = ClassmateCircleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        txtBtn.text = "CLASSMATECIRCLE"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)
        return mView
    }
}