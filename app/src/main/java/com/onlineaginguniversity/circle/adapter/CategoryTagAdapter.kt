package com.onlineaginguniversity.circle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.databinding.ItemCategoryTagBinding
import java.util.*

/**
 * @desc:发布同学圈选择分类
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class CategoryTagAdapter :
    BaseRvAdapter<CategoryBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
            ItemCategoryTagBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: CategoryBean
    ) {
        holder as ItemCategoryTagBinding
        holder.apply {
            holder.radioBtn.text = bean.name
            holder.radioBtn.isSelected = bean.check
        }
    }

    /**
     * 清空所有选择
     */
    private fun clearCheck() {
        for (c in mData as MutableList<CategoryBean>) {
            if (c.check) {
                c.check = false
            }
        }
    }

    /**
     * 选中
     */
    fun check(index: Int) {
        var bean = mData[index] as CategoryBean
        if (bean.check) {
            bean.check = false
        } else {
            clearCheck()
            bean.check = true
        }
        notifyDataSetChanged()
    }

    /**
     * 获取选中的
     */
    fun getCheck(): CategoryBean? {
        for (bean in mData as MutableList<CategoryBean>) {
            if (bean.check) {
                return bean
            }
        }
        return null
    }


    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemCategoryTagBinding -> linkedSetOf(
                holder.rootView,
            )
            else -> linkedSetOf()
        }
    }
}
