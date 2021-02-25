package com.common.core.environment.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.adapter.viewholder.ContentViewHolder
import com.common.core.base.adapter.viewholder.TitleViewHolder
import com.common.core.environment.bean.EnvironmentBean
import com.common.databinding.ItemEnvironmentContentBinding
import com.common.databinding.ItemEnvironmentTitleBinding


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 17:58
 */
class EnvironmentMultiAdapter(var data: MutableList<EnvironmentBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EnvironmentBean.TITLE ->
                TitleViewHolder(
                    ItemEnvironmentTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else -> ContentViewHolder(
                ItemEnvironmentContentBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TitleViewHolder) {
            holder.binding().title.text = data[position].alias
        } else if (holder is ContentViewHolder) {
            holder.binding().title.text = data[position].alias
            holder.binding().url.text = data[position].url
            holder.binding().radio.setImageResource(if (data[position].defaultCheck) com.common.R.mipmap.ic_select_pre else com.common.R.mipmap.ic_select)
        }
    }

}