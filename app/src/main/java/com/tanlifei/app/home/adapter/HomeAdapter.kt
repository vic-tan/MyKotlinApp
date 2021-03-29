package com.tanlifei.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.common.widget.extension.setVisible
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.databinding.ItemHomeBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeAdapter :
    CommonRvAdapter<CircleBean>() {


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
        bean: CircleBean
    ) {
        val holder = holder as ItemHomeBinding
        holder.apply {
           title.text = bean.nickName
           desc.text = bean.content
           time.text = "05月23日 15:20"
            GlideUtils.load(mContext, bean.image?.url,cover)
           headerTitleSplit.setVisible(position == 0)
           headerTitle.setVisible(position == 0)
           headerTitleLine.setVisible(position == 0)
        }

    }


    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        val holder = holder as ItemHomeBinding
        return linkedSetOf(holder.item)
    }


}
