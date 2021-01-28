package com.tanlifei.app.test.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tanlifei.app.R
import com.tanlifei.app.core.bean.UserTest

class UserSingleAdapter(layoutResId: Int, data: MutableList<UserTest>) :
    BaseQuickAdapter<UserTest, BaseViewHolder>(layoutResId, data) {
    
    override fun convert(holder: BaseViewHolder, item: UserTest) {
        holder.setText(R.id.nickname, item.nickname)
        holder.setText(R.id.description, item.description)
    }
}