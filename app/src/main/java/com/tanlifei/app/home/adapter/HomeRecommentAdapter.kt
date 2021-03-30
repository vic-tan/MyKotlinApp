package com.tanlifei.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.databinding.ItemHomeRecommentBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeRecommentAdapter :
    BaseRvAdapter<CircleBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
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
        bean: CircleBean
    ) {
        holder as ItemHomeRecommentBinding
        holder.apply {
            title.text = bean.nickName
            desc.text = "05月23日 15:20"
            GlideUtils.load(mContext, bean.image?.url, cover)
        }

    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemHomeRecommentBinding
        return linkedSetOf(holder.item)
    }

}
