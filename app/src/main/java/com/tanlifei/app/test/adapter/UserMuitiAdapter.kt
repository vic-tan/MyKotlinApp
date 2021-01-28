package com.tanlifei.app.test.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tanlifei.app.R
import com.tanlifei.app.core.bean.UserTest

class UserMuitiAdapter(data: MutableList<UserTest>) :
    BaseMultiItemQuickAdapter<UserTest, BaseViewHolder>(data) {

    init {
        addItemType(UserTest.TEXT, R.layout.test_user_item)
        addItemType(UserTest.IMG_TEXT, R.layout.test_user_img)
    }

    override fun convert(holder: BaseViewHolder, item: UserTest) {
        when(holder.itemViewType) {
            UserTest.TEXT ->{
                holder.setText(R.id.nickname, item.nickname)
                holder.setText(R.id.description, item.description)
            }
            UserTest.IMG_TEXT -> {
                holder.setText(R.id.description, item.description)
            }
        }

    }
}