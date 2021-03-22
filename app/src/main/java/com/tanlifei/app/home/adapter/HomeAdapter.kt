package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.adapter.CommonRvAdapter
import com.common.utils.GlideUtils
import com.common.utils.extension.setVisible
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.ItemHomeBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeAdapter :
    CommonRvAdapter<ClassmateCircleBean>() {
    private lateinit var mContext: Context


    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        mContext = parent.context
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
        GlideUtils.load(mContext, bean.image?.url, holder.cover)
        holder.headerTitleSplit.setVisible(position == 0)
        holder.headerTitle.setVisible(position == 0)
        holder.headerTitleLine.setVisible(position == 0)
    }


    override fun addChildClickViewIds(holder: ViewBinding): LinkedHashSet<View> {
        val holder = holder as ItemHomeBinding
        return linkedSetOf(holder.item)
    }


}
