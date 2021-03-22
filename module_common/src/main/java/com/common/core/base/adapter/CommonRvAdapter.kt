package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.listener.OnItemClickListener
import com.common.utils.extension.clickEnable
import java.util.*


/**
 * @desc: RecyclerView单一item Adapter的简单封装
 * @author: tanlifei
 * @date: 2021/2/24 15:50
 */
abstract class CommonRvAdapter<T : Any, V : ViewBinding> :
    RecyclerView.Adapter<CommonRvHolder<V>>() {

    /**
     * 数据源
     */
    var mData: MutableList<Any> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }
    private var mOnItemClickListener: OnItemClickListener<V, T>? = null


    /**
     * 用于保存需要设置点击事件的 item
     */
    private val mChildClickViews = LinkedHashSet<View>()


    /**
     * 设置需要点击事件的子view
     * @param views viewArray
     */
    private fun addViewList(views: LinkedHashSet<View>) {
        mChildClickViews.clear()
        if (ObjectUtils.isNotEmpty(views)) {
            mChildClickViews.addAll(views)
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
            mData[holder.adapterPosition] as T
        )
        addViewList(addChildClickViewIds(holder.binding))
        if (ObjectUtils.isNotEmpty(mChildClickViews)) {
            mOnItemClickListener?.let {
                for (v in mChildClickViews) {
                    v.setOnClickListener {
                        if (it.clickEnable()) {
                            setItemClick(
                                holder.binding,
                                mData[holder.adapterPosition] as T,
                                v,
                                holder.adapterPosition
                            )
                        }
                    }
                }
            }
        }

    }

    fun setOnClickListener(clickListener: View.OnClickListener, vararg views: View) {
        for (i in views.indices) {
            views[i].setOnClickListener(clickListener)
        }
    }

    // 为什么不把这个方法也封装起来？ 因为不想使用反射~
    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<V>

    abstract fun onBindViewHolder(holder: CommonRvHolder<V>, position: Int, binding: V, bean: T)

    abstract fun addChildClickViewIds(binding: V): LinkedHashSet<View>


    protected open fun setItemClick(binding: V, bean: T, v: View, position: Int) {
        mOnItemClickListener?.click(binding, bean, v, position)
    }

    fun setItemClickListener(clickListener: OnItemClickListener<V, T>) {
        this.mOnItemClickListener = clickListener
    }


}

open class CommonRvHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root) {

}


