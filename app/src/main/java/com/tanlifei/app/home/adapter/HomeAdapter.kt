package com.tanlifei.app.home.adapter

import android.content.Context
import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.ComFun
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.adapter.CommonRvMultiItemAdapter
import com.common.utils.GlideUtils
import com.common.widget.ExpandTextView
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ItemCommentBinding
import com.tanlifei.app.databinding.ItemFollowBinding
import com.tanlifei.app.databinding.ItemHomeBinding
import com.tanlifei.app.databinding.ItemHomeRecommentBinding
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
        holder: CommonRvHolder<ViewBinding>,
        position: Int,
        bean: ClassmateCircleBean
    ) {
        val holder = holder.binding as ItemHomeBinding
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


    override fun addChildClickViewIds(holder: CommonRvHolder<ViewBinding>): LinkedHashSet<View> {
        val holder = holder.binding as ItemHomeBinding
        return linkedSetOf(holder.item)
    }


}
