package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.listener.OnItemListener
import com.common.utils.extension.click
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
    var mData: MutableList<T> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }
    private var onItemListener: OnItemListener<V>? = null


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
        addViewList(addChildClickViewIds(holder.binding))
        if (ObjectUtils.isNotEmpty(childClickViews)) {
            onItemListener?.let {
                for (v in childClickViews) {
                    v?.let {
                        it.click {
                            setOnItemChildClick(v, holder.binding, holder.adapterPosition)
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


    protected open fun setOnItemChildClick(v: View, binding: V, position: Int) {
        onItemListener?.onItemClick(v, binding, position)
    }

    fun setOnItemChildClickListener(listener: OnItemListener<V>) {
        this.onItemListener = listener
    }


}

open class CommonRvHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root) {

}


