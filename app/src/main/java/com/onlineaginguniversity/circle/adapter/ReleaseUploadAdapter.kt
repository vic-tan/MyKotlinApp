package com.onlineaginguniversity.circle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.log
import com.common.widget.component.extension.setVisible
import com.common.widget.component.extension.visible
import com.luck.picture.lib.entity.LocalMedia
import com.onlineaginguniversity.R
import com.onlineaginguniversity.databinding.ItemReleaseUploadBinding
import java.util.*

/**
 * @desc:关注
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class ReleaseUploadAdapter(var isVideo: Boolean) :
    BaseRvAdapter<LocalMedia>() {
    private val cameraType = 1
    private val pictureType = 2
    private var selectMax = 9


    override fun getItemViewType(position: Int): Int {
        return if (isShowAddItem(position)) {
            cameraType
        } else {
            pictureType
        }
    }

    /**
     * 设置最大显示个数
     */
    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    /**
     * 删除
     */
    fun delete(position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION && mData.size > position) {
                mData.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, mData.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun remove(position: Int) {
        if (mData != null && position < mData.size) {
            mData.removeAt(position)
        }
    }

    override fun getItemCount(): Int {
        return if (mData.size < selectMax) {
            mData.size + 1
        } else {
            mData.size
        }
    }

    private fun isShowAddItem(position: Int): Boolean {
        val size: Int = mData.size
        return position == size
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
            ItemReleaseUploadBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: LocalMedia
    ) {
        holder as ItemReleaseUploadBinding

        if (getItemViewType(position) == cameraType) {
            holder.cover.setImageResource(R.mipmap.ic_add_image)
            holder.delete.gone()
            holder.coverMask.gone()
        } else {
            holder.apply {
                coverMask.setVisible(isVideo)
                holder.delete.visible()
                GlideUtils.load(mContext, bean.compressPath, holder.cover)
            }
        }
    }


    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemReleaseUploadBinding -> linkedSetOf(
                holder.cover
            )
            else -> linkedSetOf()
        }
    }
}
