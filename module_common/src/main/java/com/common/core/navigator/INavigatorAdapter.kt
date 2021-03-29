package com.common.core.navigator

import androidx.fragment.app.Fragment

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 16:41
 */
open interface INavigatorAdapter {

    fun onCreateFragment(position: Int): Fragment

    fun getTag(position: Int): String

    fun getCount(): Int
}