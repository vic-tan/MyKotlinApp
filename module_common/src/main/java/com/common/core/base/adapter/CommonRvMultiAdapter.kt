package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.listener.OnItemListener
import com.common.utils.AntiShakeUtils
import java.util.*


/**
 * @desc: RecyclerView 多布局 Adapter的简单封装
 * @author: tanlifei
 * @date: 2021/2/24 15:50
 */
abstract class CommonRvMultiItemAdapter<T : Any> :
    RecyclerView.Adapter<CommonRvMultiHolder>() {


    /**
     * 数据源
     */
    var mData: MutableList<T> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }
    private var onItemListener: OnItemListener? = null


    /**
     * 用于保存需要设置点击事件的 item
     */
    private val childClickViews = LinkedHashSet<View>()


    /**
     * 设置需要点击事件的子view
     * @param views viewArray
     */
    private fun addViewList(views: LinkedHashSet<View>) {
        childClickViews.clear()
        if (ObjectUtils.isNotEmpty(views)) {
            childClickViews.addAll(views)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return setItemViewType(mData[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonRvMultiHolder {
        return onCreateViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            viewType
        )
    }


    /**
     * 创建头部或者底部的ViewHolder
     */
    fun createHeaderFooterViewHolder(parent: ViewGroup, viewType: Int): CommonRvMultiHolder {
        return onCreateViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            viewType
        )
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: CommonRvMultiHolder, position: Int) {
        onBindViewHolder(
            holder,
            holder.adapterPosition,
            mData[holder.adapterPosition]
        )
        addViewList(addChildClickViewIds(holder))
        if (ObjectUtils.isNotEmpty(childClickViews)) {
            onItemListener?.let {
                for (v in childClickViews) {
                    v?.let {
                        it.setOnClickListener {
                            if (AntiShakeUtils.isInvalidClick(v))
                                return@setOnClickListener
                            setOnItemChildClick(v, holder.adapterPosition)
                        }
                    }
                }
            }
        }
    }

    // 为什么不把这个方法也封装起来？ 因为不想使用反射~
    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvMultiHolder

    abstract fun onBindViewHolder(holder: CommonRvMultiHolder, position: Int, bean: T)
    abstract fun setItemViewType(bean: T): Int

    abstract fun addChildClickViewIds(holder: CommonRvMultiHolder): LinkedHashSet<View>


    protected open fun setOnItemChildClick(v: View, position: Int) {
        onItemListener?.onItemClick(v, position)
    }

    fun setOnItemChildClickListener(listener: OnItemListener) {
        this.onItemListener = listener
    }
}

open class CommonRvMultiHolder(holder: View) :
    ViewHolder(holder) {
}



