package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.adapter.CommonRvMultiItemAdapter
import com.common.utils.GlideUtils
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.ItemHomeBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeAdapter(var context: Context?) :
    CommonRvMultiItemAdapter<ClassmateCircleBean>() {

    override fun setItemViewType(int: Int): Int {
        return ItemViewType.CONTEN.ordinal
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(
            ItemHomeBinding.inflate(
                inflater, parent, false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: ClassmateCircleBean
    ) {
        val holder = holder as ItemHomeBinding
        holder.title.text = bean.nickName
        holder.desc.text = bean.content
        holder.time.text = "05月23日 15:20"
        GlideUtils.load(context, bean.image?.url, holder.cover)
        if (position == 0) {
            holder.headerTitleSplit.visibility = View.VISIBLE
            holder.headerTitle.visibility = View.VISIBLE
            holder.headerTitleLine.visibility = View.VISIBLE
        } else {
            holder.headerTitleSplit.visibility = View.GONE
            holder.headerTitle.visibility = View.GONE
            holder.headerTitleLine.visibility = View.GONE
        }
    }


    override fun addChildClickViewIds(holder: ViewBinding): LinkedHashSet<View> {
        val holder = holder as ItemHomeBinding
        return linkedSetOf(holder.item)
    }


}
