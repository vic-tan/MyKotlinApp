package com.common.environment

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.R
import com.ruffian.library.widget.RTextView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 17:58
 */
class EnvironmentAdapter(data: MutableList<EnvironmentBean>) :
    BaseMultiItemQuickAdapter<EnvironmentBean, BaseViewHolder>(data) {

    init {
        addItemType(EnvironmentBean.TITLE, R.layout.item_environment_title)
        addItemType(EnvironmentBean.CONTENT, R.layout.item_environment_content)
    }

    override fun convert(holder: BaseViewHolder, item: EnvironmentBean) {
        when (holder.itemViewType) {
            EnvironmentBean.TITLE -> {
                holder.setText(R.id.title, item.alias)
            }
            EnvironmentBean.CONTENT -> {
                holder.setText(R.id.title, item.alias)
                holder.setText(R.id.url, item.url)
                holder.getView<RTextView>(R.id.radio).isSelected = item.check
            }
        }
    }
}