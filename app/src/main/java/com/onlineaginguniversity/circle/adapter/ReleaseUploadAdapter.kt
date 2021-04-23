package com.onlineaginguniversity.circle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.core.environment.bean.EnvironmentBean
import com.common.databinding.ItemEnvironmentContentBinding
import com.common.databinding.ItemEnvironmentTitleBinding
import com.common.utils.GlideUtils
import com.common.utils.PermissionUtils
import com.common.utils.PictureSelectorUtils
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.log
import com.common.widget.component.extension.setVisible
import com.common.widget.component.extension.visible
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.onlineaginguniversity.R
import com.onlineaginguniversity.databinding.ItemReleaseAddBinding
import com.onlineaginguniversity.databinding.ItemReleaseUploadBinding
import java.util.*

/**
 * @desc:关注
 * @author: tanlifei
 * @date: 2021/2/24 16:02
 */
class ReleaseUploadAdapter(var isVideo: Boolean) :
    BaseRvAdapter<LocalMedia>() {
    private var selectMax = 9


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

    fun isShowAddItem(): Boolean {
        return mData.size < selectMax
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(
            ItemReleaseUploadBinding.inflate(
                inflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: LocalMedia
    ) {
        holder as ItemReleaseUploadBinding
        holder.apply {
            coverMask.setVisible(isVideo)
            holder.delete.setVisible(!isVideo)
            GlideUtils.load(mContext, if (isVideo) bean.path else bean.compressPath, holder.cover)
        }
    }


    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        return when (holder) {
            is ItemReleaseUploadBinding -> {
                linkedSetOf(holder.delete, holder.cover)
            }
            else -> {
                linkedSetOf()
            }
        }
    }

    fun getViewHolder(rv: RecyclerView, position: Int): ItemReleaseUploadBinding {
        var holderView = rv.getChildAt(position)
        var baseRvHolder =
            rv.getChildViewHolder(holderView) as BaseRvHolder<ViewBinding>
        return baseRvHolder.binding as ItemReleaseUploadBinding
    }
}
