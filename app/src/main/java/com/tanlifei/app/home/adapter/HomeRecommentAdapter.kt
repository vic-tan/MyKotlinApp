package com.tanlifei.app.home.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
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
    CommonRvAdapter<ClassmateCircleBean, ItemHomeRecommentBinding>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemHomeRecommentBinding> {
        mContext = parent.context
        return CommonRvHolder(
            ItemHomeRecommentBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CommonRvHolder<ItemHomeRecommentBinding>,
        position: Int,
        binding: ItemHomeRecommentBinding,
        bean: ClassmateCircleBean
    ) {
        holder.binding.title.text = bean.nickName
        holder.binding.desc.text = "05月23日 15:20"
        GlideUtils.load(mContext, bean.image?.url, holder.binding.cover)
    }

    override fun addChildClickViewIds(binding: ItemHomeRecommentBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.item)
    }

}
