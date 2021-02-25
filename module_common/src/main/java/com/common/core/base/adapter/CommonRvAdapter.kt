package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.utils.AntiShakeUtils
import java.util.LinkedHashSet


/**
 * @desc: RecyclerView Adapter的简单封装
 * @author: tanlifei
 * @date: 2021/2/24 15:50
 */
abstract class CommonRvAdapter<T : Any, V : ViewBinding> :
    RecyclerView.Adapter<CommonRvHolder<V>>() {

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

    private fun getChildClickViews(): LinkedHashSet<View> {
        return childClickViews
    }


    /**
     * 设置需要点击事件的子view
     * @param views viewArray
     */
    fun addChildClickViewIds(vararg views: View) {
        for (viewId in views) {
            childClickViews.add(viewId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonRvHolder<V> {
        return onCreateViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            viewType
        )
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: CommonRvHolder<V>, position: Int) {
        onBindViewHolder(
            holder,
            holder.adapterPosition,
            holder.binding,
            mData[holder.adapterPosition]
        )

        onItemListener?.let {
            for (v in getChildClickViews()) {
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

    // 为什么不把这个方法也封装起来？ 因为不想使用反射~
    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<V>

    abstract fun onBindViewHolder(holder: CommonRvHolder<V>, position: Int, binding: V, bean: T)


    protected open fun setOnItemChildClick(v: View, position: Int) {
        onItemListener?.onItemClick(v, position)
    }

    fun setOnItemChildClickListener(listener: OnItemListener) {
        this.onItemListener = listener
    }


}

open class CommonRvHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root) {

}


