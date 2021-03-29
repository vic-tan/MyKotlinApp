package com.common.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.listener.OnItemClickListener
import com.common.widget.component.extension.clickEnable
import java.util.*


/**
 * @desc: RecyclerView 多布局 Adapter的简单封装
 * @author: tanlifei
 * @date: 2021/2/24 15:50
 */
abstract class BaseRvAdapter<T> :
    RecyclerView.Adapter<BaseRvHolder<ViewBinding>>() {

    /** item 布局类型框状态显示**/
    enum class ItemViewType(val value: Int) {
        HEADER(1000),//头部布局类型
        FOOTER(2000),//尾部布局类型
        CONTEN(3000),//其它布局类型
    }

    /**上下文* */
    protected lateinit var mContext: Context

    /**数据源* */
    var mData: MutableList<Any> = mutableListOf()

    /** 添加头部部 **/
    var mHeaderViews: MutableList<ViewBinding> = mutableListOf()

    /** 添加尾部 **/
    var mFooterViews: MutableList<ViewBinding> = mutableListOf()


    /**用于保存需要设置点击事件的 item **/
    private val mChildClickViews = LinkedHashSet<View>()

    /**点击事件的监听器 **/
    private var mOnItemClickListener: OnItemClickListener<T>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder<ViewBinding> {
        mContext = parent.context
        return when {
            viewType >= ItemViewType.HEADER.value && viewType < ItemViewType.FOOTER.value -> {
                BaseRvHolder(mHeaderViews[viewType - ItemViewType.HEADER.value])
            }
            viewType >= ItemViewType.FOOTER.value -> {
                BaseRvHolder(mFooterViews[viewType - ItemViewType.FOOTER.value - mData.size - 1])
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

    /**
     * item 多布局实现
     */
    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)) {
            return ItemViewType.HEADER.value + position
        }
        if (isFooterPosition(position)) {
            return ItemViewType.FOOTER.value + position
        }
        return setItemViewType(mData[position - mHeaderViews.size] as T)
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


    override fun onBindViewHolder(holder: BaseRvHolder<ViewBinding>, position: Int) {
        //是头部或者尾部
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        // 计算一下位置
        val adapterPosition: Int = position - mHeaderViews.size
        onBindViewHolder(
            holder.binding,
            adapterPosition,
            mData[adapterPosition] as T
        )
        addViewList(addChildClickView(holder.binding))
        if (ObjectUtils.isNotEmpty(mChildClickViews)) {
            mOnItemClickListener?.let {
                for (v in mChildClickViews) {
                    v.setOnClickListener {
                        if (it.clickEnable()) {
                            mOnItemClickListener?.click(
                                holder.binding,
                                mData[adapterPosition] as T,
                                v,
                                adapterPosition
                            )
                        }
                    }

                }
            }
        }
    }

    /**
     * 为什么不把这个方法也封装起来？ 因为不想使用反射~
     */
    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding>


    abstract fun onBindViewHolder(holder: ViewBinding, position: Int, bean: T)

    /**
     * 子类
     */
    abstract fun addChildClickView(holder: ViewBinding): LinkedHashSet<View>


    /**
     * 子类可以实现该方法实现多布局显示
     */
    open fun setItemViewType(bean: T): Int {
        return ItemViewType.CONTEN.ordinal
    }


    /**
     * 设置item 点击事件
     */
    fun setItemClickListener(clickListener: OnItemClickListener<T>) {
        this.mOnItemClickListener = clickListener
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
    fun loadMoreItemRange(startPos: Int) {
        notifyItemRangeChanged(
            startPos + mHeaderViews.size,
            mHeaderViews.size + mData.size - 1
        )
    }
}

/**
 * 适配器Holder 基本类
 */
open class BaseRvHolder<V : ViewBinding>(val binding: V) :
    RecyclerView.ViewHolder(binding.root) {}
