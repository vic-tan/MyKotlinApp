package com.tanlifei.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.ItemHomeRecommentBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeRecommentAdapter :
    CommonRvAdapter<ClassmateCircleBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        return CommonRvHolder(
            ItemHomeRecommentBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: ClassmateCircleBean
    ) {
        holder as ItemHomeRecommentBinding
        holder.title.text = bean.nickName
        holder.desc.text = "05月23日 15:20"
        GlideUtils.load(mContext, bean.image?.url, holder.cover)
    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemHomeRecommentBinding
        return linkedSetOf(holder.item)
    }

}
