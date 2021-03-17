package com.tanlifei.app.home.adapter

import android.content.Context
import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.common.widget.ExpandTextView
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.databinding.ItemFollowBinding
import com.tanlifei.app.databinding.ItemHomeRecommentBinding
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class HomeRecommentAdapter(var context: Context?) :
    CommonRvAdapter<ClassmateCircleBean, ItemHomeRecommentBinding>() {

    private var screenWidth = ScreenUtils.getScreenWidth()
    private var textViewWidth = screenWidth - ConvertUtils.dp2px(30f)
    private var mPositionsAndStates: SparseArray<Int> = SparseArray()

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemHomeRecommentBinding> {

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
        holder.binding.desc.text ="05月23日 15:20"
        GlideUtils.load(context, bean.image?.url, holder.binding.cover)
    }

    override fun addChildClickViewIds(binding: ItemHomeRecommentBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.item)
    }

}
