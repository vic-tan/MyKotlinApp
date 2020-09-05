package com.tanlifei.mykotlinapp.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.core.model.User

class UserMuitiAdapter(data: MutableList<User>) :
    BaseMultiItemQuickAdapter<User, BaseViewHolder>(data) {

    init {
        addItemType(User.TEXT, R.layout.user_item)
        addItemType(User.IMG_TEXT, R.layout.user_img)
    }

    override fun convert(holder: BaseViewHolder, item: User) {
        when(holder.itemViewType) {
            User.TEXT ->{
                holder.setText(R.id.nickname, item.nickname)
                holder.setText(R.id.description, item.description)
            }
            User.IMG_TEXT -> {
                holder.setText(R.id.description, item.description)
            }
        }

    }
}