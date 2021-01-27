package com.tanlifei.app.test.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tanlifei.app.R
import com.tanlifei.app.core.model.User

class UserSingleAdapter(layoutResId: Int, data: MutableList<User>) :
    BaseQuickAdapter<User, BaseViewHolder>(layoutResId, data) {
    
    override fun convert(holder: BaseViewHolder, item: User) {
        holder.setText(R.id.nickname, item.nickname)
        holder.setText(R.id.description, item.description)
    }
}