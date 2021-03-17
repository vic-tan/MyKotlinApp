package com.common.core.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.listener.OnMultiItemListener
import com.common.utils.extension.click
import java.util.*


/**
 * @desc: RecyclerView 多布局 Adapter的简单封装
 * @author: tanlifei
 * @date: 2021/2/24 15:50
 */
abstract class CommonRvMultiItemAdapter<T : Any> :
    RecyclerView.Adapter<CommonRvHolder<ViewBinding>>() {

    /**
     * UI加载框状态显示
     */
    enum class ItemViewType(val value: Int) {
        HEADER(1000),//头部
        FOOTER(2000),//尾部
        CONTEN(3000),//正常
    }

    /**
     * 数据源
     */
    var mData: MutableList<T> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }
    private var onItemListener: OnMultiItemListener<T>? = null


    /**
     * 添加头部尾部
     */
    var mHeaderViews: MutableList<ViewBinding> = mutableListOf()
    var mFooterViews: MutableList<ViewBinding> = mutableListOf()

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
        if (isHeaderPosition(position)) {
            return ItemViewType.HEADER.value + position
        }
        if (isFooterPosition(position)) {
            return ItemViewType.FOOTER.value + position
        }
        return setItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonRvHolder<ViewBinding> {
        return when {
            viewType >= ItemViewType.HEADER.value && viewType < ItemViewType.FOOTER.value -> {
                CommonRvHolder(mHeaderViews[viewType - ItemViewType.HEADER.value])
            }
            viewType >= ItemViewType.FOOTER.value -> {
                CommonRvHolder(mFooterViews[viewType - ItemViewType.FOOTER.value - mData.size - 1])
            }
            else -> {
                onCreateViewHolder(
                    LayoutInflater.from(parent.context),
                    parent,
                    viewType
                )
            }
        }
    }


    override fun getItemCount(): Int {
        var size: Int = mData.size
        if (ObjectUtils.isNotEmpty(mHeaderViews)) {
            size += mHeaderViews!!.size
        }
        if (ObjectUtils.isNotEmpty(mFooterViews)) {
            size += mFooterViews!!.size
        }
        return size
    }


    /**
     * 是不是头部类型
     */
    private fun isHeaderPosition(position: Int): Boolean {
        return position < mHeaderViews.size
    }

    /**
     * 是不是尾部类型
     */
    private fun isFooterPosition(position: Int): Boolean {
        return position >= (mHeaderViews.size + mData.size)
    }

    /**
     * 添加头部
     */
    fun addHeaderView(view: ViewBinding) {
        mHeaderViews.add(view)
    }

    /**
     * 移除头部
     */
    fun removeHeaderView(view: ViewBinding) {
        if (mHeaderViews.isNotEmpty() && mHeaderViews.contains(view)) {
            mHeaderViews.remove(view)
        }
    }


    /**
     * 添加底部
     */
    fun addFooterView(view: ViewBinding) {
        mFooterViews.add(view)
    }

    /**
     * 移除底部
     */
    fun removeFooterView(view: ViewBinding) {
        if (mFooterViews.isNotEmpty() && mFooterViews.contains(view)) {
            mFooterViews.remove(view)
        }

    }


    override fun onBindViewHolder(holder: CommonRvHolder<ViewBinding>, position: Int) {
        //是头部或者尾部
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        // 计算一下位置
        val adapterPosition: Int = position - mHeaderViews.size
        onBindViewHolder(
            holder,
            adapterPosition,
            mData[adapterPosition]
        )
        addViewList(addChildClickViewIds(holder))
        if (ObjectUtils.isNotEmpty(childClickViews)) {
            onItemListener?.let {
                for (v in childClickViews) {
                    v?.let {
                        it.click {
                            setOnItemClick(holder, mData[adapterPosition], v, adapterPosition)
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
    ): CommonRvHolder<ViewBinding>

    abstract fun onBindViewHolder(holder: CommonRvHolder<ViewBinding>, position: Int, bean: T)
    abstract fun setItemViewType(int: Int): Int

    abstract fun addChildClickViewIds(holder: CommonRvHolder<ViewBinding>): LinkedHashSet<View>


    protected open fun setOnItemClick(
        holder: CommonRvHolder<ViewBinding>,
        bean: T,
        v: View,
        position: Int
    ) {
        onItemListener?.click(holder.binding, bean, v, position)
    }

    fun setItemClickListener(listener: OnMultiItemListener<T>) {
        this.onItemListener = listener
    }


    /**
     * 有头部尾部刷新请用这个
     */
    fun refreshItemRange() {
        notifyItemRangeChanged(
            mHeaderViews.size,
            mHeaderViews.size + mData.size - 1
        )
    }

    /**
     * 有头部尾部加载更多时请用这个
     */
    fun loadmoreItemRange(startPos: Int) {
        notifyItemRangeChanged(
            startPos + mHeaderViews.size,
            mHeaderViews.size + mData.size - 1
        )
    }
}





